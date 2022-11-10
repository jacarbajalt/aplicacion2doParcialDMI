package clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tesatechnology.techgas.R;

import java.util.Date;

import Objetos.ClientBooking;
import Objetos.HistoryBooking;
import Provedores.ClientBookingProvider;
import Provedores.HistoryBookingProvider;
import repartidores.CalificacionCliente;
import repartidores.PrincipalRep;

public class CalificacionRepartidor extends AppCompatActivity {

    private TextView origen;
    private RatingBar puntuacion;
    private Button calificar;

    private ClientBookingProvider mClientBookingProvider;

    private FirebaseAuth mAuth;

    private HistoryBooking mHistoryBooking;
    private HistoryBookingProvider mHistoryBookingProvider;
    private float mCalificacion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion_repartidor);
        mAuth=FirebaseAuth.getInstance();
        origen=findViewById(R.id.lblorigenCalifCli);
        puntuacion=findViewById(R.id.ratingBarCliente);
        calificar=findViewById(R.id.btnCalificacionCliente);
        calificar.setOnClickListener(this::calificarConductor);
        mClientBookingProvider=new ClientBookingProvider();
        mHistoryBooking=new HistoryBooking();
        mHistoryBookingProvider=new HistoryBookingProvider();
        puntuacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float calificacion, boolean fromUser) {
                mCalificacion=calificacion;
            }
        });
        getClientBooking();
    }

    private void getClientBooking() {
        mClientBookingProvider.getClientBooking(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ClientBooking cB=snapshot.getValue(ClientBooking.class);
                    Toast.makeText(CalificacionRepartidor.this, cB.getIdClient(), Toast.LENGTH_SHORT).show();
                    origen.setText(cB.getOrigin());
                    mHistoryBooking=new HistoryBooking(
                            cB.getIdHistoryBooking(),
                            cB.getIdClient(),
                            cB.getIdDriver(),
                            cB.getOrigin(),
                            cB.getStatus(),
                            cB.getOriginLat(),
                            cB.getOriginLng()
                    );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void calificarConductor(View v) {
        calificate();
    }

    private void calificate() {
        if(mCalificacion>0){
            mHistoryBooking.setCalificationClient(mCalificacion);
            mHistoryBooking.setTimestamp(new Date().getTime());
            mHistoryBookingProvider.getHistoryBooking(mHistoryBooking.getIdHistoryBooking()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        mHistoryBookingProvider.updateCalificactionDriver(mHistoryBooking.getIdHistoryBooking(), mCalificacion).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CalificacionRepartidor.this, "Se guardo correctamente la calificacion", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(CalificacionRepartidor.this, Principal.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }else{
                        mHistoryBookingProvider.create(mHistoryBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CalificacionRepartidor.this, "Se guardo correctamente la calificacion", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(CalificacionRepartidor.this, Principal.class);
                                startActivity(i);
                                finish();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Toast.makeText(CalificacionRepartidor.this, "Debes ingresar una calificacion", Toast.LENGTH_SHORT).show();
        }
    }
}