package repartidores;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tesatechnology.techgas.BottomSheetBehavior;
import com.tesatechnology.techgas.DecodePoints;
import com.tesatechnology.techgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Objetos.ClientBooking;
import Objetos.FCMBody;
import Objetos.FCMResponse;
import Provedores.ClientBookingProvider;
import Provedores.GeofireProvider;
import Provedores.GoogleApiProvider;
import Provedores.NotificationProvider;
import Provedores.PedidoEfectivoProvider;
import Provedores.PedidoLitrosProvider;
import Provedores.TokenProvider;
import Provedores.UsuarioProvider;
import clientes.PantallaCarga;
import clientes.Principal;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesPedidoRep extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private GeofireProvider mGeofireProvider;
    private TokenProvider mTokenProvider;

    private boolean isActive;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;

    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;

    private boolean mIsFirstTime = true;
    private boolean mIsCloseToClient=false;
    private boolean seleccionCliente=true;

    private Marker mMarker;
    private LatLng mCurrentLatLng;

    private ValueEventListener mListener;

    private TextView nombreCliente;
    private TextView correoCliente;
    private TextView origenCliente;
    private TextView caracterCliente;

    private ImageView perfil;

    private String mExtraClientId;

    private PedidoEfectivoProvider mPedidosEfectivoProvider;
    private PedidoLitrosProvider mPedidosLitrosProvider;

    private ClientBookingProvider mClientBookingProvider;

    private LatLng mOriginLatLng;

    private GoogleApiProvider mGoogleApiProvider;

    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;

    private Button iniciar, finalizar;

    private NotificationProvider mNotificationProvider;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location: locationResult.getLocations()) {
                if (getApplicationContext() != null) {

                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    if (mMarker != null) {
                        mMarker.remove();
                    }

                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                                    .title("Tu posicion")
                    );
                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(16f)
                                    .build()
                    ));

                    updateLocation();

                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        getClientBooking();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido_rep);

        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRep);
        mapFragment.getMapAsync(this);

        mGeofireProvider = new GeofireProvider("drivers_working");
        mTokenProvider = new TokenProvider();
        mPedidosEfectivoProvider=new PedidoEfectivoProvider();
        mPedidosLitrosProvider=new PedidoLitrosProvider();
        mClientBookingProvider=new ClientBookingProvider();
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRep);
        mMapFragment.getMapAsync(this);

        nombreCliente=findViewById(R.id.lblClientBooking);
        correoCliente=findViewById(R.id.lblCorreoClientBooking);
        origenCliente=findViewById(R.id.lblOrigen);
        caracterCliente=findViewById(R.id.lblCaracterClient);

        iniciar=findViewById(R.id.btnInicioRep);
        finalizar=findViewById(R.id.btnFinalizarRep);

        //iniciar.setEnabled(false);

        iniciar.setOnClickListener(this::iniciarReparto);
        finalizar.setOnClickListener(this::finalizarReparto);

        perfil=findViewById(R.id.fotoUsuario);

        mExtraClientId=getIntent().getStringExtra("idClient");

        mGoogleApiProvider = new GoogleApiProvider(DetallesPedidoRep.this);

        mNotificationProvider=new NotificationProvider();

        getClient();
        getLitros();
    }

    public void finalizarReparto(View v) {
        finishBooking();
    }

    private void finishBooking() {
        mClientBookingProvider.updateStatus(mExtraClientId, "finish");
        mClientBookingProvider.updateIdHistoryBooking(mExtraClientId);
        sendNotification("Finalizado");
        if(mFusedLocation!=null){
            mFusedLocation.removeLocationUpdates(mLocationCallback);
        }
        mGeofireProvider.removeLocation(mAuth.getCurrentUser().getUid());
        Intent i=new Intent(DetallesPedidoRep.this, CalificacionCliente.class);
        i.putExtra("idClient", mExtraClientId);
        startActivity(i);
        finish();
    }

    public void iniciarReparto(View v) {
        if(mIsCloseToClient){
            startBooking();
        }else{
            Toast.makeText(DetallesPedidoRep.this, "Debes de estar mas cerca del cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void startBooking() {
        mClientBookingProvider.updateStatus(mExtraClientId, "start");
        iniciar.setVisibility(View.GONE);
        finalizar.setVisibility(View.VISIBLE);
        sendNotification("Iniciado");
    }

    private double getDistanceBetween(LatLng clientLatLng, LatLng driverLatLng){
        double distance=0;
        Location clientLocation=new Location("");
        Location driverLocation=new Location("");
        clientLocation.setLatitude(clientLatLng.latitude);
        clientLocation.setLongitude(clientLatLng.longitude);
        driverLocation.setLatitude(driverLatLng.latitude);
        driverLocation.setLongitude(driverLatLng.longitude);
        distance=clientLocation.distanceTo(driverLocation);
        return distance;
    }

    private void getClientBooking() {
        //Toast.makeText(DetallesPedidoRep.this, mExtraClientId, Toast.LENGTH_SHORT).show();
        mClientBookingProvider.getClientBooking(mExtraClientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String origin = snapshot.child("origin").getValue().toString();
                    double originLat = Double.parseDouble(snapshot.child("originLat").getValue().toString());
                    double originLng= Double.parseDouble(snapshot.child("originLng").getValue().toString());
                    mOriginLatLng = new LatLng(originLat, originLng);
                    origenCliente.setText("Ir a: " +origin);
                    mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Recoger aqui"));
                    drawRoute();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void drawRoute() {
        mGoogleApiProvider.getDirections(mCurrentLatLng, mOriginLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(13f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions);

                    JSONArray legs =  route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");


                } catch(Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void getClient() {
        mPedidosEfectivoProvider.getClient(mExtraClientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String cOe = snapshot.child("cilOest").getValue().toString();
                    String eOt = snapshot.child("efecOTarj").getValue().toString();
                    String efectivoPedido = snapshot.child("efectivoPedido").getValue().toString();
                    correoCliente.setText(efectivoPedido+" pesos");
                    if(cOe.equals("true")){
                        nombreCliente.setText("Cilindro");
                    }else{
                        nombreCliente.setText("Estacionario");
                    }
                    if(eOt.equals("true")){
                        caracterCliente.setText("Pago: Efectivo");
                    }else{
                        caracterCliente.setText("Pagado con Tarjeta");
                    }
                }else{
                    Toast.makeText(DetallesPedidoRep.this, "La informacion no existe!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLitros(){
        mPedidosLitrosProvider.getClient(mExtraClientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String cOe = snapshot.child("ciliOesta").getValue().toString();
                    String eOt = snapshot.child("efectOTarje").getValue().toString();
                    String efectivoPedido = snapshot.child("litrosPedidos").getValue().toString();
                    String tarifa=snapshot.child("preciodelGas").getValue().toString();
                    correoCliente.setText(efectivoPedido+" litros"+"\nTotal: "+tarifa);
                    if(cOe.equals("true")){
                        nombreCliente.setText("Cilindro");
                    }else{
                        nombreCliente.setText("Estacionario");
                    }
                    if(eOt.equals("true")){
                        caracterCliente.setText("Pago: Efectivo");
                    }else{
                        caracterCliente.setText("Pagado con Tarjeta");
                    }
                }else{
                    Toast.makeText(DetallesPedidoRep.this, "La informacion no existe!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isDriverWorking() {
        mListener = mGeofireProvider.isDriverWorking(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    disconnect();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateLocation() {
        if (mAuth.getCurrentUser().getUid() != null && mCurrentLatLng != null) {
            mGeofireProvider.saveLocation(mAuth.getUid(), mCurrentLatLng);
            if(!mIsCloseToClient){
                if(mOriginLatLng!=null && mCurrentLatLng!=null){
                    double distance=getDistanceBetween(mOriginLatLng, mCurrentLatLng);
                    //Calculo de Metros
                    if(distance<=1000){
                        //iniciar.setEnabled(true);
                        mIsCloseToClient=true;
                        Toast.makeText(DetallesPedidoRep.this, "Estas cerca a la posicion del cliente", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else {
            showAlertDialogNOGPS();
        }
    }
    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActived() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void disconnect() {
        if (mFusedLocation != null) {
            mFusedLocation.removeLocationUpdates(mLocationCallback);
            if (mAuth.getCurrentUser() != null) {
                mGeofireProvider.removeLocation(mAuth.getUid());
            }
        }
        else {
            Toast.makeText(this, "No te puedes desconectar", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                }
                else {
                    Toast.makeText(DetallesPedidoRep.this, "No se puede conectar", Toast.LENGTH_SHORT).show();
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }
    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(DetallesPedidoRep.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(DetallesPedidoRep.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }
    public void generateToken() {
        mTokenProvider.create(mAuth.getUid());
    }

    public void sendNotification(String status) {
        mTokenProvider.getToken(mExtraClientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String token = snapshot.child("token").getValue().toString();
                    Map<String, String> map=new HashMap<>();
                    map.put("title", "ESTADO DE TU VIAJE");
                    map.put("body", "El estado de tu viaje es: "+status);
                    FCMBody fcmBody=new FCMBody(token, "high", "4500s", map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() != null){
                                if(response.body().getSuccess()!=1){
                                    Toast.makeText(DetallesPedidoRep.this, "No se envio la notificacion", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(DetallesPedidoRep.this, "No se envio la notificacion!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error", "Error: "+t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(DetallesPedidoRep.this, "No se envio la notificacion por que el token de repartidor no existe", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}