package com.tesatechnology.techgas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import clientes.Registro;
import repartidores.RegistroRep;

public class Seleccion extends AppCompatActivity {
    ImageView logoSeleccion;
    Button tengo, notengo;
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);
        logoSeleccion=findViewById(R.id.logoSeleccion);
        tengo=findViewById(R.id.btnInicioSeleccion);
        notengo=findViewById(R.id.btnCrearCuentaSeleccion);
        tengo.setOnClickListener(this::tengoCuenta);
        notengo.setOnClickListener(this::noTengoCuenta);
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
    }
    public void tengoCuenta(View v){
        Intent intent = new Intent(Seleccion.this, Login.class);
        startActivity(intent);
    }

    public void noTengoCuenta(View v){
        String typeUser = mPref.getString("user", "");
        if (typeUser.equals("Cliente")) {
            Toast.makeText(this,"Tu seleccion fue: "+typeUser, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Seleccion.this, Registro.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Tu seleccion fue: "+typeUser, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Seleccion.this, RegistroRep.class);
            startActivity(intent);
        }
    }
}