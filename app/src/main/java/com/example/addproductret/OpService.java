package com.example.addproductret;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface OpService {
    @Multipart
    @POST("index.php?route=api44/addProduct")
    Call<response> addProduct(
            @Part("api_token") RequestBody api_token,
            @Part("product_name") RequestBody product_name,
            @Part("product_price") RequestBody product_price,
            @Part("product_quantity") RequestBody description_name,
            @Part("model") RequestBody product_model,
            @Part MultipartBody.Part image
            );
}
