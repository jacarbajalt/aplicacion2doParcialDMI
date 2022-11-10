package clientes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tesatechnology.techgas.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Objetos.ClientBooking;
import Objetos.FCMBody;
import Objetos.FCMResponse;
import Objetos.PedidosEfectivo;
import Provedores.ClientBookingProvider;
import Provedores.GeofireProvider;
import Provedores.GoogleApiProvider;
import Provedores.NotificationProvider;
import Provedores.TokenProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaCarga extends AppCompatActivity {
    private LottieAnimationView mAnimation;
    private TextView buscando;
    private Button cancelarPedido;
    private GeofireProvider mGeofireProvider;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private GoogleApiProvider mGoogleApiProvider;
    private ValueEventListener mListener;

    //Variables
    private String mExtraOrigin;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private LatLng mOriginLatLng;
    private double mRadius=0.1;
    private boolean mDriverFound = false;
    private String  mIdDriverFound = "";
    private LatLng mDriverFoundLatLng;
    private TokenProvider mTokenProvider;
    private NotificationProvider mNotificationProvider;
    private ClientBookingProvider mClientBookingProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        mAuth=FirebaseAuth.getInstance();

        mAnimation=findViewById(R.id.animacionCarga);
        buscando=findViewById(R.id.lblBuscandoCon);
        cancelarPedido=findViewById(R.id.btnCancelarPedido);
        cancelarPedido.setOnClickListener(this::cancelarPedidoGas);

        mGeofireProvider=new GeofireProvider("active_drivers");

        mAnimation.playAnimation();

        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraOriginLat=getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng=getIntent().getDoubleExtra("origin_lng", 0);
        mOriginLatLng=new LatLng(mExtraOriginLat, mExtraOriginLng);
        mTokenProvider=new TokenProvider();
        mNotificationProvider = new NotificationProvider();
        mClientBookingProvider=new ClientBookingProvider();
        mGoogleApiProvider = new GoogleApiProvider(PantallaCarga.this);
        getClosestDriver();
    }

    public void cancelarPedidoGas(View v) {
        PedidosEfectivo pE=new PedidosEfectivo();
        AlertDialog.Builder m=new AlertDialog.Builder(this);
        m.setTitle("Aviso!");
        m.setMessage("¿Estas seguro de cancelar este pedido?");
        m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase= FirebaseDatabase.getInstance().getReference();
                String id= Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo").child(id).removeValue();
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos Cancelados").child(id).setValue(pE);
                Intent i=new Intent(PantallaCarga.this, Principal.class);
                startActivity(i);
                finish();
            }
        });
        m.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog d=m.create();
        d.show();
    }

    private void getClosestDriver(){
        mGeofireProvider.getActiveDrivers(mOriginLatLng, mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mDriverFound){
                    mDriverFound = true;
                    mIdDriverFound = key;
                    mDriverFoundLatLng = new LatLng(location.latitude, location.longitude);
                    buscando.setText("REPARTIDOR ENCONTRADO\nESPERANDO RESPUESTA");
                    sendNotification();
                    Log.d("DRIVER", "ID: " + mIdDriverFound);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if(!mDriverFound){
                    mRadius = mRadius + 0.1f;
                    // NO ENCONTRO NINGUN CONDUCTOR
                    if (mRadius > 5) {
                        buscando.setText("NO SE ENCONTRO UN CONDUCTOR");
                        Toast.makeText(PantallaCarga.this, "NO SE ENCONTRO UN CONDUCTOR", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else{
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


    public void sendNotification() {
        mTokenProvider.getToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String token = snapshot.child("token").getValue().toString();
                        Map<String, String> map=new HashMap<>();
                        map.put("title", "SOLICITUD DE SERVICIO");
                        map.put("body", "UN CLIENTE ESTA SOLICITANDO SERVICIO EN: "+mExtraOrigin);
                        map.put("idClient", mAuth.getCurrentUser().getUid());
                        FCMBody fcmBody=new FCMBody(token, "high", "4500s", map);
                        mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if(response.body() != null){
                                    if(response.body().getSuccess()==1){
                                        ClientBooking cB=new ClientBooking(
                                                mAuth.getUid(),
                                                mIdDriverFound,
                                                mExtraOrigin,
                                                "create",
                                                mExtraOriginLat,
                                                mExtraOriginLng
                                        );
                                        mClientBookingProvider.create(cB).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                checkStatusClientBooking();
                                                //Toast.makeText(PantallaCarga.this, "Se envio la peticion", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(PantallaCarga.this, "Se envió la notificación", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(PantallaCarga.this, "No se envio la notificación", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(PantallaCarga.this, "No se envio la notificación!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                Log.d("Error", "Error: "+t.getMessage());
                            }
                        });
                    }else{
                        Toast.makeText(PantallaCarga.this, "No se envio la notificación por que el token de repartidor no existe", Toast.LENGTH_SHORT).show();
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkStatusClientBooking() {
        mListener=mClientBookingProvider.getStatus(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status = snapshot.getValue().toString();
                    if (status.equals("accept")) {
                        Intent intent = new Intent(PantallaCarga.this, DetallesPedidoUser.class);
                        startActivity(intent);
                        finish();
                    } else if (status.equals("cancel")) {
                        Toast.makeText(PantallaCarga.this, "El repartidor rechazo tu pedido!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PantallaCarga.this, Principal.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mClientBookingProvider.getStatus(mAuth.getCurrentUser().getUid()).removeEventListener(mListener);
        }
    }

}