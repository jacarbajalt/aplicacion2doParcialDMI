package com.tesatechnology.techgas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Arrays;
import java.util.Objects;

import clientes.Principal;
import clientes.Registro;
import repartidores.PrincipalRep;


public class Login extends AppCompatActivity {

    ImageView logo;
    TextView bienvenida, aviso, opciones;
    EditText correo, contrasena;
    Button botonIniciar, botonRegistrar, botonInicioFB, botonInicioGoogle;
    CallbackManager cM;
    GoogleSignInClient iGoogle;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ProgressDialog cargaLogin;
    SharedPreferences preferences;
    //Variables privadas
    private String correoUser, pass;
    int RC_SIGN_IN=1;
    String TAG="GoogleSignIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        cM=CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        logo=findViewById(R.id.lbiLogo);
        bienvenida=findViewById(R.id.lblBienvenida);
        aviso=findViewById(R.id.lblAviso);
        opciones=findViewById(R.id.lblOpciones);
        correo=findViewById(R.id.txtCorreo);
        contrasena=findViewById(R.id.txtContrasena);
        botonIniciar=findViewById(R.id.btnIniciarSesion);
        botonRegistrar=findViewById(R.id.btnRegistro);
        botonInicioGoogle=findViewById(R.id.btnGoogle);
        botonInicioFB=findViewById(R.id.btnFacebook);
        cargaLogin=new ProgressDialog(this);
        botonIniciar.setOnClickListener(this::InicioSesion);
        botonInicioGoogle.setOnClickListener(this::inicioSesionGoogle);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        iGoogle = GoogleSignIn.getClient(this, gso);
        botonInicioFB.setOnClickListener(this::inicioSesionFB);
        preferences=getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);


    }
    public void InicioSesion(View v){
        correoUser=correo.getText().toString().trim();
        pass=contrasena.getText().toString().trim();
        if(TextUtils.isEmpty(correoUser)){
            Toast.makeText(this, "Debes ingresar un correo!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Debes ingresar una contraseña!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(correoUser) && TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
        }
        mAuth.signInWithEmailAndPassword(correoUser, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                cargaLogin.setMessage("Accediendo...");
                cargaLogin.show();
                if(task.isSuccessful()){
                    String user=preferences.getString("user", "");
                    if(user.equals("Clientes")){
                        Toast.makeText(Login.this, "Accediste Exitosamente!",  Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Login.this, Principal.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(Login.this, "Accediste Exitosamente!",  Toast.LENGTH_SHORT).show();
                        Intent i1=new Intent(Login.this, PrincipalRep.class);
                        startActivity(i1);
                        finish();
                    }

                }else{
                    AlertDialog.Builder m=new AlertDialog.Builder(Login.this);
                    m.setMessage("Datos ingresados erroneos, verifique bien sus datos");
                    m.setPositiveButton("Aceptar", null);
                    AlertDialog d=m.create();
                    d.show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void Registro(View v){
        Intent i=new Intent(this, Registro.class);
        startActivity(i);
    }

    public void inicioSesionGoogle(View v){
        signIn();
    }

    public void inicioSesionFB(View v){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(cM, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = iGoogle.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }else{
            cM.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    private void handleFacebookAccessToken(AccessToken token){
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent i=new Intent(this, Principal.class);
            startActivity(i);
        }else{
            AlertDialog.Builder m=new AlertDialog.Builder(this);
            m.setTitle("Bíenvenido");
            m.setMessage("Debes de iniciar sesión para continuar!");
            m.setPositiveButton("Aceptar", null);
            AlertDialog d=m.create();
            d.show();
        }
    }
}