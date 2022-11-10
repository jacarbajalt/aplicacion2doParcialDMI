package Provedores;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PedidoLitrosProvider {
    DatabaseReference mDatabase;

    public PedidoLitrosProvider() {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Trabajadores").child("Repartidores").child("Pedidos por Litro");
    }
    public DatabaseReference getClient(String idClient) {
        return mDatabase.child(idClient);
    }
}
