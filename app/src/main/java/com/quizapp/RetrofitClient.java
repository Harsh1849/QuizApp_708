package com.quizapp;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    String ApiUrl = "https://llamamobileappdevelopment.pythonanywhere.com/";
    private ApiInterface myApi;

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        myApi = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitClient getInstance(){
        if (instance == null){
            instance = new RetrofitClient();
        }

        return instance;
    }

    public ApiInterface getMyApi(){
        return myApi;
    }
}
