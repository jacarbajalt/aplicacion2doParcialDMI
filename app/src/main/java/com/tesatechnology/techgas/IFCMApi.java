package com.tesatechnology.techgas;

import Objetos.FCMBody;
import Objetos.FCMResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAX4SAAZ4:APA91bFk9e2Vy6hCsyKFZJxu0eGECHb9Ud3-anbkOsBBcxGyFpGnqYu1LBJ3GY6v5cAz3xBytkFTM0OoznWWE77GXtoXRrl5v9cY5ObSsyKN2JRgeVWKhEXJnTJec79GmTUMqgb8jFs6"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
