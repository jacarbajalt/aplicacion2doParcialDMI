package com.tesatechnology.techgas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PedidoGas extends AppCompatActivity {
    RadioButton r1, r2, r3, r4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_gas);
        r1=findViewById(R.id.rBtnEstacionario);
        r2=findViewById(R.id.rBtnCilindro);
        r3=findViewById(R.id.rBtnEfectivo);
        r4=findViewById(R.id.rBtnTarjeta);
    }
    public void onClick(View v){
        validar();
    }
    private void validar(){
        String cad="Seleccionado \n";

        if(r1.isChecked()==true){
            cad+="Estacionario \n";
        }
        if(r2.isChecked()){
            cad+="Cilindro \n";
        }
        if(r3.isChecked()==true){
            cad+="Efectivo \n";
        }
        if (r4.isChecked()){
            cad+="Tarjeta \n";
        }
        Toast.makeText(getApplicationContext(), cad, Toast.LENGTH_SHORT).show();
    }
}