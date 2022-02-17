package com.example.addproductret;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {

    ImageView pic;
    Button btnPic;
    Button btnAddP;
    File imageFile;
    EditText name;
    EditText price;
    EditText quantity;
    EditText model;

    FloatingActionButton loginBut;
    String api_token;

    Retrofit retrofitInc;
    OpService opService;
    Logout logout;

    MultipartBody.Part Ppic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofitInc = new Retrofit.Builder()
                .baseUrl("http://lightg.ir/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        pic = (ImageView) findViewById(R.id.imageView);
        btnPic = (Button) findViewById(R.id.picBtn);
        btnAddP = (Button) findViewById(R.id.btnAddP);
        btnAddP.setEnabled(false);
        name = (EditText) findViewById(R.id.editTextPname);
        price = (EditText) findViewById(R.id.editTextPprice);
        quantity = (EditText) findViewById(R.id.editTextPqun);
        model = (EditText) findViewById(R.id.editTextPmod);



        loginBut = (FloatingActionButton) findViewById(R.id.loginBut);
        loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getBaseContext() , Login.class);
                resultLauncherLogin.launch(login);
            }
        });

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                resultLauncherPic.launch(takePicIntent);
            }
        });

        btnAddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opService = retrofitInc.create(OpService.class);
                RequestBody token = RequestBody.create(MediaType.parse("text/plain") , api_token);
                RequestBody pName = RequestBody.create(MediaType.parse("text/plain") , name.getText().toString());
                RequestBody pPrice = RequestBody.create(MediaType.parse("text/plain") , price.getText().toString());
                RequestBody pQuantity = RequestBody.create(MediaType.parse("text/plain") , quantity.getText().toString());
                RequestBody pModel = RequestBody.create(MediaType.parse("text/plain") , model.getText().toString());
                RequestBody rFile = RequestBody.create(MediaType.parse("image/*") , imageFile);
                Ppic = MultipartBody.Part.createFormData("file" , imageFile.getName() , rFile);
                Call<response> call = opService.addProduct(token , pName , pPrice , pQuantity , pModel , Ppic);
                call.enqueue(new Callback<response>() {
                    @Override
                    public void onResponse(Call<response> call, Response<response> response) {
                        Toast.makeText(getApplicationContext() , response.body().success , Toast.LENGTH_LONG).show();
                        name.setText("");
                        price.setText("");
                        quantity.setText("");
                        model.setText("");
                    }

                    @Override
                    public void onFailure(Call<response> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext() ,"سرور در دسترس نیست!" , Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    ActivityResultLauncher<Intent> resultLauncherPic = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();
                Bitmap image = (Bitmap) intent.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext() , image);
                imageFile = new File(getRealPathFromURI(tempUri));
                if (api_token != null){
                    btnAddP.setEnabled(true);
                }
                //set pic to image view
                Bitmap imageRounded = Bitmap.createBitmap(image.getWidth(),image.getHeight(),image.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(image, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, image.getWidth(), image.getHeight())), 10, 10, mpaint); // Round Image Corner 100 100 100 100
                pic.setImageBitmap(imageRounded);
            }
        }
    });

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG,100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getApplicationContext().getContentResolver() != null) {
            Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    ActivityResultLauncher<Intent> resultLauncherLogin = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent intent = result.getData();
                if (intent.getStringExtra("api_token") == null){
                    Toast.makeText(getApplicationContext() , "لطفا مرحله ورود را انجام دهید." , Toast.LENGTH_LONG).show();
                } else {
                    // api_token = data.getStringExtra("api_token");
                    api_token = intent.getExtras().getString("api_token");
                    if (imageFile != null){
                        btnAddP.setEnabled(true);
                    }
                }
            }
        }
    });

    //onDestroy Clear the api_token from server
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (api_token != null){
            logout = retrofitInc.create(Logout.class);
            RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data") , api_token);
            Call<Void> call = logout.deleteSession(token);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    return;
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    return;
                }
            });
        }
    }

    //permissions check
    public void cameraPermission(){
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.CAMERA} , (int) 102);
    }

    public void storagePermission(){
        ActivityCompat.requestPermissions(MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE} , (int) 103);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            this.cameraPermission();
        } else if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            this.storagePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case (int)102:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext() , "مجوز استفاده از دوربین تایید شد!" , Toast.LENGTH_SHORT).show();
                    if (ContextCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        this.storagePermission();
                    }
                } else {
                    Toast.makeText(getApplicationContext() , "اجازه استفاده از دوربین داده نشد!" , Toast.LENGTH_LONG).show();
                    //Intent exit = new Intent(MainActivity.this ,Exit.class);
                    //startActivity(exit);
                    MainActivity.this.finish();
                }
                break;
            case (int)103:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext() , "مجوز استفاده از فضای ذخیره سازی خارجی داده شد!" , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext() , "مجوز استفاده از فضای ذخیره سازی خارجی داده نشد!" , Toast.LENGTH_LONG).show();
                    //Intent exit = new Intent(MainActivity.this ,Exit.class);
                    //startActivity(exit);
                    MainActivity.this.finish();
                }
                break;
        }
    }
}