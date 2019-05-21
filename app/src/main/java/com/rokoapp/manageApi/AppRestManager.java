package com.rokoapp.manageApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.rokoapp.utils.AppUrlUtils.BASE_URL;
import static com.rokoapp.utils.ParamUtils.ID_AUTH;

public class AppRestManager {

    private static Retrofit retrofit;

    static ApiClient getInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);

            OkHttpClient okHttp = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", ID_AUTH)
                        .build();
                return chain.proceed(newRequest);
            }).build();

            retrofit = builder.client(okHttp).build();
//            retrofit.create(serviceClass)
            OkHttpClient.Builder client = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(ApiClient.class);
    }



}
