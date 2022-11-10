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

import Objetos.PedidosLitros;
import clientes.PantallaCarga;

public class RegistroPedidoLitros extends BottomSheetDialogFragment {
    EditText litros;
    TextView metodoPago;
    RadioButton ef, tar, cil, est;
    Button solicitudLitros;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //Variables
    String cad="Seleccionado";
    String litrosIn, efecOTar, cilOEst;
    int litrosProcesados;
    double precioGas=13.25;
    double tarif;
    boolean eOta;
    boolean cOef;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.orden_litro, container, false);
        mAuth=FirebaseAuth.getInstance();
        cil=v.findViewById(R.id.rdnCilindro);
        est=v.findViewById(R.id.rdnEstacionario);
        litros=v.findViewById(R.id.txtLitros);
        metodoPago=v.findViewById(R.id.lblMetodoPago);
        ef=v.findViewById(R.id.rdnEfectivo);
        tar=v.findViewById(R.id.rdnTarjeta);
        solicitudLitros=v.findViewById(R.id.btnSolicitarServicios);
        solicitudLitros.setOnClickListener(this::hacerPedidoporLitros);
        return v;
    }
    public void hacerPedidoporLitros(View v){
        litrosIn=litros.getText().toString();
        litrosProcesados=Integer.parseInt(litrosIn);
        cilOEst=String.valueOf(cOef);
        efecOTar=String.valueOf(eOta);
        if(cOef=cil.isChecked()==true){
            tarif=precioGas*litrosProcesados;
            String res=String.valueOf(tarif);
            Toast.makeText(getContext(), "Tu tarifa es de: $"+res, Toast.LENGTH_SHORT).show();
        }else if(est.isChecked()==true){
            tarif=precioGas*litrosProcesados;
            String res=String.valueOf(tarif);
            Toast.makeText(getContext(), "Tu tarifa es de: $"+res, Toast.LENGTH_SHORT).show();
        }
        if(eOta=ef.isChecked()==true){
            Toast.makeText(getContext(), "Tu metodo de pago es: Efectivo", Toast.LENGTH_SHORT).show();
        }else if(tar.isChecked()==true){
            Toast.makeText(getContext(), "Tu metodo de pago es: Tarjeta", Toast.LENGTH_SHORT).show();
        }
        mDatabase=FirebaseDatabase.getInstance().getReference();
        hacerPedidoL(cOef, litrosProcesados, eOta);
        dismiss();
    }
    public void hacerPedidoL(boolean cOef, int litrosProcesados, boolean eOta){
        PedidosLitros pL=new PedidosLitros(cOef, litrosProcesados, eOta);
        AlertDialog.Builder m=new AlertDialog.Builder(getContext());
        m.setTitle("Aviso!");
        m.setMessage("¿Estas seguro de querer hacer este pedido?");
        m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id=Objects.requireNonNull(mAuth.getCurrentUser().getUid());
                mDatabase.child("Trabajadores").child("Repartidores").child("Pedidos por Litro").child(id).setValue(pL);
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
        m1.setMessage("Lo vamos a llevar a tu ubicacion "+pL.informacionActual());
        m1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        AlertDialog d1=m1.create();
        d1.show();
    }
}
