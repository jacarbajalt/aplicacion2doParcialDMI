package com.tesatechnology.techgas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    Button efectivo, litros;
    TextView titulo;
    private String mExtraOrigin;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        titulo=v.findViewById(R.id.lblTitulo);
        efectivo=v.findViewById(R.id.btnEfectivo);
        litros=v.findViewById(R.id.btnLitros);
        efectivo.setOnClickListener(this::efectivo);
        litros.setOnClickListener(this::litros);
        return v;
    }
    public void efectivo(View v){
        Intent i=new Intent(getContext(), RegistroEfectivo.class);
        startActivity(i);
    }

    public void litros(View v){
        Intent i=new Intent(getContext(), RegistroLitro.class);
        startActivity(i);
    }
}
