package Provedores;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import Objetos.Usuarios;

public class PedidoEfectivoProvider {
    DatabaseReference mDatabase;

    public PedidoEfectivoProvider() {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Trabajadores").child("Repartidores").child("Pedidos por Efectivo");
    }

    public DatabaseReference getClient(String idClient) {
        return mDatabase.child(idClient);
    }
}
