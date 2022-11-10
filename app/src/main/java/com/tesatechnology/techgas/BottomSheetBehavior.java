package com.tesatechnology.techgas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class BottomSheetBehavior extends BottomSheetDialogFragment {
    Button config, info, cerrarSesion;
    TextView opciones;
    ImageView logoEmpresa;
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_sheet_configuraciones, container, false);
        config=v.findViewById(R.id.btnConfiguracion);
        info=v.findViewById(R.id.btnNosotros);
        cerrarSesion=v.findViewById(R.id.btnCerrar);
        cerrarSesion.setOnClickListener(this::cerrarSesion);
        opciones=v.findViewById(R.id.lblMenu);
        logoEmpresa=v.findViewById(R.id.logoEmpresa);
        return v;
    }
    public void cerrarSesion(View v){
        FirebaseAuth.getInstance().signOut();
        com.facebook.login.LoginManager.getInstance().logOut();
        Intent i = new Intent(getContext(), Bienvenida.class);
        startActivity(i);
        dismiss();
    }
}
