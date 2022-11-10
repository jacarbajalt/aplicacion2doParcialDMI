package com.tesatechnology.techgas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import clientes.Principal;
import repartidores.PrincipalRep;

public class Bienvenida extends AppCompatActivity {
    TextView mBienvenida, mSeleccion;
    Button us, rep;
    ImageView lB;
    SharedPreferences mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        lB=findViewById(R.id.logoBienvenida);
        mBienvenida=findViewById(R.id.lblMensajeBien);
        mSeleccion=findViewById(R.id.lblMensajeSeleccion);
        us=findViewById(R.id.btnUsuario);
        rep=findViewById(R.id.btnRepartidor);
        us.setOnClickListener(this::botonUsuario);
        rep.setOnClickListener(this::botonRepartidor);
    }
    public void botonUsuario(View v){
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putString("user", "Cliente");
        editor.apply();
        goToSelectAuth();
    }

    public void botonRepartidor(View v){
        final SharedPreferences.Editor editor = mPref.edit();
        editor.putString("user", "Repartidor");
        editor.apply();
        goToSelectAuth();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String user = mPref.getString("user", "");
            if (user.equals("Cliente")) {
                Intent intent = new Intent(Bienvenida.this, Principal.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(Bienvenida.this, PrincipalRep.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
    private void goToSelectAuth() {
        Intent intent = new Intent(Bienvenida.this, Seleccion.class);
        startActivity(intent);
    }
}