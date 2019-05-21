package com.rokoapp.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rokoapp.utils.SaveCache;

import static com.rokoapp.utils.ParamUtils.ANDROID_TOKEN;

@SuppressWarnings("deprecation")
public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

        SaveCache.save(getApplicationContext(), ANDROID_TOKEN, token);
    }
}
