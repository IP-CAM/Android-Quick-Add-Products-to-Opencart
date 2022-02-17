package com.example.addproductret;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Logout {
    @Multipart
    @POST("index.php?route=api44/logout")
    Call<Void> deleteSession(
            @Part("api_token") RequestBody apiToken
            );
}
