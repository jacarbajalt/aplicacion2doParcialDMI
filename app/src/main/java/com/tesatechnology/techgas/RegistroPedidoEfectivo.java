package com.tesatechnology.techgas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Objetos.PedidosEfectivo;
import clientes.PantallaCarga;
import clientes.Principal;

public class RegistroPedidoEfectivo extends BottomSheetDialogFragment {
    RadioButton cilindro, estacionario, efectivo, tarjeta;
    TextView metodo;
    EditText ingresoEfectivo;
    Button solicitarEfectivo;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //Variables
    String cad="Seleccionado";
    String ubAct, ubDest, litrosIn, efecOTar, cilOEst;
    String precioIngresado;
    int precioProcesado;
    boolean eOt;
    boolean cOe;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.orden_efectivo, container, false);
        mAuth=FirebaseAuth.getInstance();
        cilindro=v.findViewById(R.id.rdnCilindroE);
        estacionario=v.findViewById(R.id.rdnEstacionarioE);
        metodo=v.findViewById(R.id.lblMetodoPagoE);
        ingresoEfectivo=v.findViewById(R.id.txtEfectivo);
        efectivo=v.findViewById(R.id.rdnEfectivoE);
        tarjeta=v.findViewById(R.id.rdnTarjetaE);
        solicitarEfectivo=v.findViewById(R.id.btnSolicitarServiciosE);
        solicitarEfectivo.setOnClickListener(this::hacerPedidoPorPrecio);
        return v;
    }
    public void hacerPedidoPorPrecio(View v){
        precioIngresado=ingresoEfectivo.getText().toString();
        precioProcesado=Integer.parseInt(precioIngresado);
        cilOEst=String.valueOf(cOe);
        efecOTar=String.valueOf(eOt);
        if(cOe=cilindro.isChecked()==true){
            Toast.makeText(getContext(), "Tu seleccion fue Cilindro", Toast.LENGTH_SHORT).show();
        }else if(estacionario.isChecked()==true){
            Toast.makeText(getContext(), "Tu seleccion fue Estacionario", Toast.LENGTH_SHORT).show();
        }
        if(eOt=efectivo.isChecked()==true){
            Toast.makeText(getContext(), "Tu metodo de pago es Efectivo", Toast.LENGTH_SHORT).show();
        }else if(tarjeta.isChecked()==true){
            Toast.makeText(getContext(), "Tu metodo de pago es Tarjeta", Toast.LENGTH_SHORT).show();
        }
        mDatabase=FirebaseDatabase.getInstance().getReference();
        //iniciarAnimacion();
        hacerPedidoE(cOe, precioProcesado, eOt);
        dismiss();
    }
    public void hacerPedidoE(boolean cOe, int precioProcesado, boolean eOt) {
        PedidosEfectivo pE=new PedidosEfectivo(cOe, precioProcesado, eOt);
        String id=Objects.requireNonNull(mAuth.getCurrentUser().getUid());
        mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo").child(id).setValue(pE);
        dismiss();
        iniciarAnimacion();
        /*AlertDialog.Builder m=new AlertDialog.Builder(getContext());
        m.setTitle("Aviso!");
        m.setMessage("Estas seguro de hacer este pedido?");
        m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id=Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo").child(id).setValue(pE);
                dismiss();
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
        AlertDialog.Builder m1=new AlertDialog.Builder(getContext());
        m1.setMessage("Lo vamos a llevar a tu ubicacion "+pE.informacionActual());
        m1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        AlertDialog d1=m1.create();
        d1.show();*/
    }
    public void iniciarAnimacion(){
        Intent i=new Intent(getActivity(), PantallaCarga.class);
        getContext().startActivity(i);
    }
}
