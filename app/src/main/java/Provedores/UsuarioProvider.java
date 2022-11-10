package Provedores;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import Objetos.Usuarios;

public class UsuarioProvider {

    DatabaseReference mDatabase;

    public UsuarioProvider() {
        mDatabase=FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Clientes");
    }

    public Task<Void> create(Usuarios client) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getNombre());
        map.put("email", client.getCorreo());
        return mDatabase.child(client.getNombre()).setValue(map);
    }

    public DatabaseReference getClient(String idClient) {
        return mDatabase.child(idClient);
    }
}
