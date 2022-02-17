package com.example.addproductret;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface loginService {
    @Multipart
    @POST("index.php?route=api44/login2")
    Call<loginRes> login(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("key") RequestBody apiKey
            );
}
