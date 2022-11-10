package receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import Provedores.ClientBookingProvider;
import Provedores.GeofireProvider;
import repartidores.DetallesPedidoRep;

public class AcceptReceiver extends BroadcastReceiver {
    private GeofireProvider mGeofireProvider;
    private ClientBookingProvider mClientBookingProvider;
    private FirebaseAuth mAuth;
    @Override
    public void onReceive(Context context, Intent intent) {
        mAuth=FirebaseAuth.getInstance();
        mGeofireProvider=new GeofireProvider("active_drivers");
        mGeofireProvider.removeLocation(mAuth.getCurrentUser().getUid());
        String idClient =intent.getExtras().getString("idClient");
        mClientBookingProvider=new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "accept");

        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, DetallesPedidoRep.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        intent1.putExtra("idClient", idClient);
        context.startActivity(intent1);
    }
}
