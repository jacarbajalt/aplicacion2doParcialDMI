package clientes;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tesatechnology.techgas.Login;
import com.tesatechnology.techgas.R;
import Objetos.Usuarios;

import java.util.Calendar;
import java.util.Objects;

public class Registro extends AppCompatActivity {
    TextView bienvenidaUs;
    ImageView logoReg;
    EditText nom, ApPat, ApMat, corrTel, usuario, contrasena, fechaNac;
    Button registrar, cancelar, fecha;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuth;
    DatabaseReference mDatabase, currentUserDB;
    ProgressDialog carga;

    //Variables privadas para registrar datos
    private String nomb, apPat, apMat, correTel, user, password, fechNac;
    private int dia, mes, ano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth=FirebaseAuth.getInstance();
        bienvenidaUs=findViewById(R.id.lblBienvenida);
        logoReg=findViewById(R.id.lbiLogoReg);
        nom=findViewById(R.id.txtNombre);
        ApPat=findViewById(R.id.txtApPater);
        ApMat=findViewById(R.id.txtApellidoMater);
        corrTel=findViewById(R.id.txtCorrTel);
        usuario=findViewById(R.id.txtCorreo);
        contrasena=findViewById(R.id.txtContrasena);
        fechaNac=findViewById(R.id.txtFechaNac);
        registrar=findViewById(R.id.btnRegistrar);
        cancelar=findViewById(R.id.btnCancelar);
        fecha=findViewById(R.id.btnFecha);
        carga=new ProgressDialog(this);
        registrar.setOnClickListener(this::registrarUsuario);
    }
    public void registrarUsuario(View v) {
        nomb = nom.getText().toString().trim();
        apPat = ApPat.getText().toString().trim();
        apMat = ApMat.getText().toString().trim();
        correTel = corrTel.getText().toString().trim();
        user = usuario.getText().toString().trim();
        password = contrasena.getText().toString().trim();
        fechNac=fechaNac.getText().toString().trim();

        if(TextUtils.isEmpty(correTel)){
            Toast.makeText(this, "Ingresa un correo!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingresa una contraseña!", Toast.LENGTH_SHORT).show();
            return;
        }
        carga.setMessage("Espere un momento...");
        carga.show();
        mAuth.createUserWithEmailAndPassword(correTel, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AlertDialog.Builder m=new AlertDialog.Builder(Registro.this);
                    m.setMessage("Registro Exitoso!\nInicie Sesion para Continuar!");
                    m.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDatabase=FirebaseDatabase.getInstance().getReference();
                            guardarUsuario(nomb, apPat, apMat, correTel, user, password, fechNac);
                            Intent i=new Intent(Registro.this, Login.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    AlertDialog d=m.create();
                    d.show();
                }else{
                    AlertDialog.Builder m1=new AlertDialog.Builder(Registro.this);
                    m1.setMessage("No se pudo realizar el registro");
                    m1.setPositiveButton("Aceptar", null);
                }
            }
        });
    }
    public void registrarFecha(View v){
        Calendar c=Calendar.getInstance();
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes=c.get(Calendar.MONTH);
        ano=c.get(Calendar.YEAR);
        DatePickerDialog d=new DatePickerDialog(this, (view, year, month, dayOfMonth) -> fechaNac.setText(dayOfMonth+"/"+(month+1)+"/"+year),dia, mes, ano);
        d.show();
    }

    public void cancelarRegistro(View v){
        Toast.makeText(this, "Registro Cancelado!", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(this, Login.class);
        startActivity(i);
        finish();
    }
    public void guardarUsuario(String nomb, String apPat, String apMat, String correTel, String user,String password, String fechNac){
        Usuarios u=new Usuarios(nomb, apPat, apMat, correTel, user ,password, fechNac);
        String id=Objects.requireNonNull(mAuth.getCurrentUser().getUid());
        mDatabase.child("Usuarios").child("Clientes").child(id).setValue(u);
    }
}