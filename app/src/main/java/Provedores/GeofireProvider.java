package Provedores;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCliente;
    private GeoFire mGeofire;
    private GeoFire mGeofireCliente;

    public GeofireProvider(String reference){
        mDatabase = FirebaseDatabase.getInstance().getReference().child(reference);
        mDatabaseCliente = FirebaseDatabase.getInstance().getReference().child("ubi_cliente");

        mGeofire = new GeoFire(mDatabase);
        mGeofireCliente = new GeoFire(mDatabaseCliente);
    }

    public void saveLocation(String idRepartidor, LatLng latLng){
        mGeofire.setLocation(idRepartidor, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void saveLocationCliente(String idCliente, LatLng latLng){
        mGeofireCliente.setLocation(idCliente, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idRepartidor){
        mGeofire.removeLocation(idRepartidor);
    }

    public DatabaseReference getDriverLocation(String idDriver) {
        return mDatabase.child(idDriver).child("l");
    }

    public GeoQuery getActiveDrivers(LatLng latLng, double radius) {
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
    public DatabaseReference isDriverWorking(String idDriver) {
        return FirebaseDatabase.getInstance().getReference().child("drivers_working").child(idDriver);
    }
}
