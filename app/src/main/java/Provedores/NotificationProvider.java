package Provedores;

import com.tesatechnology.techgas.IFCMApi;
import com.tesatechnology.techgas.RetrofitClient;

import Objetos.FCMBody;
import Objetos.FCMResponse;
import retrofit2.Call;

public class NotificationProvider {
    private String url="https://fcm.googleapis.com";

    public NotificationProvider(){

    }

    public Call<FCMResponse> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}
