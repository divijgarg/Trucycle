package com.example.recycleapplicationv3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by AbhiAndroid
 */

public class SplashActivity extends AppCompatActivity {
    Animation fadeAnimation;
    Animation rotateAnimation;
    ImageView imageView;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().hide();
        imageView=(ImageView) findViewById(R.id.logo);
        fadeAnimation();
        System.out.println("232");

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,SignInActivity.class);
                startActivity(intent);
                finish();
            }
        },2600);


    }


    private void fadeAnimation() {
        fadeAnimation = AnimationUtils.loadAnimation(this,R.anim.fade);

        imageView.startAnimation(fadeAnimation);


    }
}
