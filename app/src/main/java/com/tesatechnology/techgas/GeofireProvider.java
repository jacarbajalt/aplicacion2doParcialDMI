package com.tesatechnology.techgas;

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

    public GeofireProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("active_repartidor");
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

    public GeoQuery getActiveRepartidor(LatLng latlng){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latlng.latitude, latlng.longitude), 5);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
}
