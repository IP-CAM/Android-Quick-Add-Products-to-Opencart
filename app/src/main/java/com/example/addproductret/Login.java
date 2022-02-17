package com.example.addproductret;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Retrofit retrofitInc;
    EditText Eusername;
    EditText Epass;
    Button btnLogin;
    private String api_key = "pWptDUQ5SHdSL1t0U4ZhnIvzQgpHd5eqkn6HF6doOOqwCN8Q8UUFn4y2SYWqCgL3HQKSePPfgD8TJAvjGe8rI7eF2lyPthMlDwc37zVlcqFwZOkL0OVBuCx3S85kfz1Bv5mgeGfikOPFkN4jiJVlqEtYoPQ4lH41n2OUfBC26YZbaj4AMaEJLEHbkU31v1S0QN5zjbXBStc4ZhpNKmVzZqiObwEb4lQmubP0MaoMI83VrIN7ssIefx32sL3KsZIF";
    private loginService loginservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Eusername = (EditText) findViewById(R.id.editTextUsername);
        Epass = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.loginButton);

        retrofitInc = new Retrofit.Builder()
                .baseUrl("http://lightg.ir/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginservice = retrofitInc.create(loginService.class);
                RequestBody username = RequestBody.create(MediaType.parse("multipart/form-data") , Eusername.getText().toString());
                RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data") , Epass.getText().toString());
                RequestBody key = RequestBody.create(MediaType.parse("multipart/form-data") , api_key);
                Call<loginRes> call = loginservice.login(username , password , key);
                call.enqueue(new Callback<loginRes>() {
                    @Override
                    public void onResponse(Call<loginRes> call, Response<loginRes> response) {
                        if (response.body().error == null){
                            Toast.makeText(getApplicationContext(),"ورود با موفقییت انجام شد!", Toast.LENGTH_LONG).show();
                            //put api_token string into new Intent to pass back to main activity and close this one
                            Intent intent = new Intent();
                            intent.putExtra("api_token" , response.body().apiToken);
                            setResult(RESULT_OK , intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),response.body().error,Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<loginRes> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext() , "سرور در دسترس نیست!" ,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}