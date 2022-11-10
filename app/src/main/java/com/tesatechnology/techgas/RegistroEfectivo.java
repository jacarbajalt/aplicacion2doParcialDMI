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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Objetos.PedidosEfectivo;
import clientes.PantallaCarga;
import clientes.Principal;

public class RegistroEfectivo extends AppCompatActivity {

    RadioButton cilindro, estacionario, efectivo, tarjeta;
    TextView metodo;
    EditText ingresoEfectivo;
    Button solicitarEfectivo;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    TextView direccion;

    //Variables
    String cad="Seleccionado";
    String ubAct, ubDest, litrosIn, efecOTar, cilOEst;
    String precioIngresado;
    int precioProcesado;
    boolean eOt;
    boolean cOe;
    private String mExtraOrigin;
    private double mExtraOriginLat;
    private double mExtraOriginLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_efectivo);
        mAuth=FirebaseAuth.getInstance();
        cilindro=findViewById(R.id.rdnCilindroE);
        estacionario=findViewById(R.id.rdnEstacionarioE);
        metodo=findViewById(R.id.lblMetodoPagoE);
        ingresoEfectivo=findViewById(R.id.txtEfectivo);
        efectivo=findViewById(R.id.rdnEfectivoE);
        tarjeta=findViewById(R.id.rdnTarjetaE);
        direccion=findViewById(R.id.lblDireccionPedido);
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraOriginLat=getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng=getIntent().getDoubleExtra("origin_lng", 0);
        direccion.setText(mExtraOrigin);
        solicitarEfectivo=findViewById(R.id.btnSolicitarServiciosE);
        solicitarEfectivo.setOnClickListener(this::hacerPedidoPorPrecio);
    }

    private void hacerPedidoPorPrecio(View v) {
        precioIngresado=ingresoEfectivo.getText().toString();
        precioProcesado=Integer.parseInt(precioIngresado);
        cilOEst=String.valueOf(cOe);
        efecOTar=String.valueOf(eOt);
        if(cOe=cilindro.isChecked()==true){
            Toast.makeText(this, "Tu seleccion fue Cilindro", Toast.LENGTH_SHORT).show();
        }else if(estacionario.isChecked()==true){
            Toast.makeText(this, "Tu seleccion fue Estacionario", Toast.LENGTH_SHORT).show();
        }
        if(eOt=efectivo.isChecked()==true){
            Toast.makeText(this, "Tu metodo de pago es Efectivo", Toast.LENGTH_SHORT).show();
        }else if(tarjeta.isChecked()==true){
            Toast.makeText(this, "Tu metodo de pago es Tarjeta", Toast.LENGTH_SHORT).show();
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();
        hacerPedidoE(cOe, precioProcesado, eOt);
    }

    public void hacerPedidoE(boolean cOe, int precioProcesado, boolean eOt) {
        PedidosEfectivo pE=new PedidosEfectivo(cOe, precioProcesado, eOt);
        /*String id=Objects.requireNonNull(mAuth.getCurrentUser().getUid());
        mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo").child(id).setValue(pE);
        dismiss();
        iniciarAnimacion();*/
        AlertDialog.Builder m=new AlertDialog.Builder(RegistroEfectivo.this);
        m.setTitle("¡Aviso!");
        m.setMessage("¿Estas seguro de hacer este pedido?");
        m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id= Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo").child(id).setValue(pE);
                Intent i=new Intent(RegistroEfectivo.this, PantallaCarga.class);
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
        AlertDialog.Builder m1=new AlertDialog.Builder(RegistroEfectivo.this);
        m1.setMessage("Lo vamos a llevar a: "+mExtraOrigin+" "+pE.informacionActual());
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