package Provedores;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Objetos.Repartidores;

public class TrabajadorProvider {

    DatabaseReference mDatabase;

    public TrabajadorProvider() {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Trabajadores").child("Repartidores");
    }

    public Task<Void> create(Repartidores repartidores) {
        return mDatabase.child(repartidores.getNombre()).setValue(repartidores);
    }

    public DatabaseReference getDriver(String idDriver) {
        return mDatabase.child(idDriver);
    }
}
