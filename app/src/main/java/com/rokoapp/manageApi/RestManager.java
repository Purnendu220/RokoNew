package com.rokoapp.manageApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.rokoapp.utils.AppUrlUtils.BASE_URL;

@SuppressWarnings("deprecation")
public class RestManager {

    private static Retrofit retrofit;

/*    public static ApiClient getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiClient.class);
    }*/
    static ApiClient getInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();


            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);

            OkHttpClient okHttp = new OkHttpClient();
//            okHttp.setSslSocketFactory(getSSLConfig(contex).getSocketFactory());

            retrofit = builder.client(okHttp).build();
//            retrofit.create(serviceClass)
            OkHttpClient.Builder client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
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



//    public static ApiClient REST_CLIENT;
//     Context context;

   /* public RestManager(Context context){
        this.context = context;
    }*/

   /* public static ApiClient getInstance(){
        return createAdapter(); }

    private static ApiClient createAdapter() {
        // loading CAs from an InputStream
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
*//*
Add .crt to resource->raw
*//*
        InputStream cert = CareerwillApplication.getmContext().getResources().openRawResource(R.raw.certificate);
        Certificate ca=null;
        try {
            ca = cf.generateCertificate(cert);
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            try {
                cert.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        try {
            keyStore.setCertificateEntry("ca", ca);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // creating a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            tmf.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        // creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

       *//* OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory())
                .build();*//*

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(sslContext.getSocketFactory())
                .build();

     *//*   HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);*//*


        // creating a RestAdapter using the custom client
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(ApiClient.class);
    }*/
}
