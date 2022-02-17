package com.example.addproductret;

import com.google.gson.annotations.SerializedName;

public class loginRes {
    @SerializedName("api_token")
    public String apiToken;

    @SerializedName("error")
    public String error;
}
