package receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import Provedores.ClientBookingProvider;
import Provedores.GeofireProvider;

public class CancelReceiver extends BroadcastReceiver {
    private GeofireProvider mGeofireProvider;
    private ClientBookingProvider mClientBookingProvider;
    private FirebaseAuth mAuth;
    @Override
    public void onReceive(Context context, Intent intent) {
        String idClient =intent.getExtras().getString("idClient");
        mClientBookingProvider=new ClientBookingProvider();
        mClientBookingProvider.updateStatus(idClient, "cancel");

        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }
}
