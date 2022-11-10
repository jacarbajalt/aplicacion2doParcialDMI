package com.tesatechnology.techgas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Objetos.PedidosLitros;
import clientes.PantallaCarga;

public class RegistroLitro extends AppCompatActivity {
    EditText litros;
    TextView metodoPago;
    RadioButton ef, tar, cil, est;
    Button solicitudLitros;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView direccion;

    //Variables
    String cad="Seleccionado";
    String litrosIn, efecOTar, cilOEst;
    int litrosProcesados;
    double precioGas=23.50;
    double tarif;
    boolean eOta;
    boolean cOef;

    private String mExtraOrigin;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_litro);
        mAuth=FirebaseAuth.getInstance();
        cil=findViewById(R.id.rdnCilindro);
        est=findViewById(R.id.rdnEstacionario);
        litros=findViewById(R.id.txtLitros);
        metodoPago=findViewById(R.id.lblMetodoPago);
        ef=findViewById(R.id.rdnEfectivo);
        tar=findViewById(R.id.rdnTarjeta);
        direccion=findViewById(R.id.lblDireccionPedidoLi);
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraOriginLat=getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng=getIntent().getDoubleExtra("origin_lng", 0);
        direccion.setText(mExtraOrigin);
        solicitudLitros=findViewById(R.id.btnSolicitarServicios);
        solicitudLitros.setOnClickListener(this::hacerPedidoporLitros);
    }

    private void hacerPedidoporLitros(View v) {
        litrosIn=litros.getText().toString();
        litrosProcesados=Integer.parseInt(litrosIn);
        cilOEst=String.valueOf(cOef);
        efecOTar=String.valueOf(eOta);
        if(cOef=cil.isChecked()==true){
            tarif=precioGas*litrosProcesados;
            String res=String.valueOf(tarif);
            Toast.makeText(this, "Tu tarifa es de: $"+res, Toast.LENGTH_SHORT).show();
        }else if(est.isChecked()==true){
            tarif=precioGas*litrosProcesados;
            String res=String.valueOf(tarif);
            Toast.makeText(this, "Tu tarifa es de: $"+res, Toast.LENGTH_SHORT).show();
        }
        if(eOta=ef.isChecked()==true){
            Toast.makeText(this, "Tu metodo de pago es: Efectivo", Toast.LENGTH_SHORT).show();
        }else if(tar.isChecked()==true){
            Toast.makeText(this, "Tu metodo de pago es: Tarjeta", Toast.LENGTH_SHORT).show();
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();
        hacerPedidoL(cOef, litrosProcesados, tarif, eOta);

    }

    private void hacerPedidoL(boolean cOef, int litrosProcesados, double precioProcesado, boolean eOta) {
        PedidosLitros pL=new PedidosLitros(cOef, litrosProcesados, precioProcesado, eOta);
        AlertDialog.Builder m=new AlertDialog.Builder(RegistroLitro.this);
        m.setTitle("¡Aviso!");
        m.setMessage("¿Estas seguro de querer hacer este pedido?");
        m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id= Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Litro").child(id).setValue(pL);
                Intent i=new Intent(RegistroLitro.this, PantallaCarga.class);
                i.putExtra("origin_lat", mExtraOriginLat);
                i.putExtra("origin_lng", mExtraOriginLng);
                i.putExtra("origin", mExtraOrigin);
                startActivity(i);
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
        AlertDialog.Builder m1=new AlertDialog.Builder(RegistroLitro.this);
        m1.setMessage("Lo vamos a llevar a: "+mExtraOrigin+" "+pL.informacionActual());
        m1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog d1=m1.create();
        d1.show();
    }
}