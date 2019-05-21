package com.rokoapp.utils;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by vaio on 7/19/2016.
 */

public class MultiPartHelperClass {

    public static RequestBody getRequestBody(String value){
        if (!value.isEmpty() && value != null) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
            return body;
        }else {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "");
            return body;
        }
    }

    public static MultipartBody.Part getRequestFile(File jsonFile, String key){
        MultipartBody.Part body = null;
        if (jsonFile.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), jsonFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData(key, getTimeInMillis()+".jpg", requestFile);
        }


        return body;
    }
    public static MultipartBody.Part getMultipartData(String key, File imageFile){
        MultipartBody.Part body = null;
        if (imageFile.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            // MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData(key, getTimeInMillis()+".jpg", requestFile);
        }


        return body;
    }
    private static long getTimeInMillis(){
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));
        return cal.getTimeInMillis();
    }
}
