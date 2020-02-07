package com.webtomob.myrecipe.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.webtomob.myrecipe.R;
import com.webtomob.myrecipe.view.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        creatingSplashScreen();
    }

    /********* Creating Splash Screen ***********/

    private void creatingSplashScreen() {

        new CountDownTimer(2500, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(in);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {

    }
}
