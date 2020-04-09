package com.example.my_scheduler.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.example.my_scheduler.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Lottie Animation
        LottieAnimationView animationView = findViewById(R.id.animation_view);
        animationView.setAnimation("calendar_lottie.json");
        //animationView.loop(true);

        //Lottie Animation start
        animationView.playAnimation();
    }
}
