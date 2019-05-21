package com.rokoapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.rokoapp.R;

public class Splash extends AppCompatActivity {

    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final ProgressBar pb = findViewById(R.id.pb);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    // Update the progress status
                    progressStatus +=1;

                    // Try to sleep the thread for 20 milliseconds
                    try{
                        Thread.sleep(20);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }
                    });
                }
                /*RootBeer rootBeer = new RootBeer(Splash.this);
                if(rootBeer.isRooted()){
//                    we found indication of root
//                    Toast.makeText(Splash.this,"This App is not support to your device. Sorry for Inconvenience.", Toast.LENGTH_LONG).show();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }else{*/
//                    we didn’t find indication of root
                    Intent in = new Intent(Splash.this, PermissionActivity.class);
//                    Intent in = new Intent(Splash.this, Login.class);
                    startActivity(in);
                    Splash.this.finish();
//                }

            }
        }).start();
    }
}
