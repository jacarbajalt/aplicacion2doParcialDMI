package repartidores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tesatechnology.techgas.Bienvenida;
import com.tesatechnology.techgas.Login;
import com.tesatechnology.techgas.R;
import Objetos.Repartidores;

import java.util.Calendar;
import java.util.Objects;

public class RegistroRep extends AppCompatActivity {
    EditText nom, aP, aM, correo, pass, fechNac;
    Button reg, cancelar, fecha;
    TextView aviso;
    ImageView logoReg;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog carga;

    //Variables

    String n, aPat, aMat, co, p, fNac;
    int dia, mes, ano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_rep);
        mAuth=FirebaseAuth.getInstance();
        logoReg=findViewById(R.id.logoRegRep);
        aviso=findViewById(R.id.lblAvisoReg);
        nom=findViewById(R.id.txtNombreRep);
        aP=findViewById(R.id.txtApellidoPatRep);
        aM=findViewById(R.id.txtApellidoMatRep);
        correo=findViewById(R.id.txtCorreoRep);
        pass=findViewById(R.id.txtContrasenaRep);
        fechNac=findViewById(R.id.txtFechaRep);
        reg=findViewById(R.id.btnRegistrarRep);
        cancelar=findViewById(R.id.btnCancelarRep);
        fecha=findViewById(R.id.btnFechaRep);
        fecha.setOnClickListener(this::calendarioFechas);
        carga=new ProgressDialog(this);
    }

    public void registrarRepartidor(View v){
        n=nom.getText().toString().trim();
        aPat=aP.getText().toString().trim();
        aMat=aM.getText().toString().trim();
        co=correo.getText().toString().trim();
        p=pass.getText().toString().trim();
        fNac=fechNac.getText().toString().trim();
        if(TextUtils.isEmpty(co)){
            Toast.makeText(this,"Debes ingresar un correo!", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(p)){
            Toast.makeText(this,"Debes ingresar una contraseña", Toast.LENGTH_SHORT).show();
        }

        if(TextUtils.isEmpty(co) && TextUtils.isEmpty(p)){
            Toast.makeText(this,"Debes de llenar todos los campos!", Toast.LENGTH_SHORT).show();
        }
        carga.setMessage("Registrando...");
        carga.show();
        mAuth.createUserWithEmailAndPassword(co, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder m=new AlertDialog.Builder(RegistroRep.this);
                    m.setTitle("Aviso!");
                    m.setMessage("Registro Exitoso\nInicie Sesion para Continuar!");
                    m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabase=FirebaseDatabase.getInstance().getReference();
                            guardarRepartidor(n, aPat, aMat, co, p, fNac);
                            Intent i=new Intent(RegistroRep.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    AlertDialog d=m.create();
                    d.show();
                }else{
                    Toast.makeText(RegistroRep.this,"Algo fallo, intentelo nuevamente!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void guardarRepartidor(String n, String aPat, String aMat, String co, String p, String fNac) {
        Repartidores r=new Repartidores(n, aPat, aMat, co, p, fNac);
        String id= Objects.requireNonNull(mAuth.getCurrentUser().getUid());
        mDatabase.child("Trabajadores").child("Repartidores").child(id).setValue(r);
    }

    public void calendarioFechas(View v){
        Calendar c=Calendar.getInstance();
        ano=c.get(Calendar.YEAR);
        mes=c.get(Calendar.MONTH);
        dia=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dPD=new DatePickerDialog(this,
                (view, year, month, dayOfMonth) ->
                fechNac.setText(dayOfMonth+"/"+(month+1)+"/"+year),dia,mes,ano);
        dPD.show();
    }
    public void cancelarRegistro(View v){
        Intent i=new Intent(this, Bienvenida.class);
        startActivity(i);
        finish();
    }
}