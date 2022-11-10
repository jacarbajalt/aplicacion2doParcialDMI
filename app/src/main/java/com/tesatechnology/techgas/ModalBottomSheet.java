package com.tesatechnology.techgas;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

public class ModalBottomSheet extends BottomSheetDialogFragment {
    String cad="Seleccionado \n";
    String tarifa="Tarifa: $";
    static ModalBottomSheet newInstance() {
        return new ModalBottomSheet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_principal, container, false);
        v.setOnClickListener(this::onRadioButtonClicked);
        return v;
    }

    public void onRadioButtonClicked(View v) {
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();
        double precioLitro=13.25;
        int litros =((EditText)v).getText().charAt(3);



        // Check which radio button was clicked
        switch(v.getId()) {

            case R.id.rdnCilindro:
                if (checked)
                    cad+="Cilindro \n";
                    tarifa+=""+precioLitro*litros;
                    break;
            case R.id.rdnEstacionario:
                if(checked)
                    cad+="Estacionario \n";
                    tarifa+=""+precioLitro*litros;
                break;
            case R.id.rdnEfectivo:
                if (checked)
                    cad+="Efectivo \n";
                break;
            case R.id.rdnTarjeta:
                if(checked)
                    cad+="Tarjeta \n";
                break;
        }
    }



}
