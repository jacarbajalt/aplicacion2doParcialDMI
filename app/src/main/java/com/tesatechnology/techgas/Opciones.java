package com.tesatechnology.techgas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class Opciones extends AppCompatActivity {
    Button efectivo, litros;
    TextView titulo;
    private String mExtraOrigin;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private String mOrigin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        titulo=findViewById(R.id.lblTitulo);
        efectivo=findViewById(R.id.btnEfectivo);
        litros=findViewById(R.id.btnLitros);
        efectivo.setOnClickListener(this::efectivo);
        litros.setOnClickListener(this::litros);
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraOriginLat=getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng=getIntent().getDoubleExtra("origin_lng", 0);
    }

    private void litros(View v) {
        Intent i=new Intent(this, RegistroLitro.class);
        i.putExtra("origin_lat", mExtraOriginLat);
        i.putExtra("origin_lng", mExtraOriginLng);
        i.putExtra("origin", mExtraOrigin);
        startActivity(i);
    }

    private void efectivo(View v) {
        Intent i=new Intent(this, RegistroEfectivo.class);
        i.putExtra("origin_lat", mExtraOriginLat);
        i.putExtra("origin_lng", mExtraOriginLng);
        i.putExtra("origin", mExtraOrigin);
        startActivity(i);
    }
}