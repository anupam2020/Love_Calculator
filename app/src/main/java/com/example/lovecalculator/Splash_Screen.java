package com.example.lovecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class Splash_Screen extends AppCompatActivity {

    Handler handler;
    ProgressDialog dialog;

    Animation top,bottom;

    ImageView imgLove;
    TextView txtLove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        handler=new Handler(Looper.getMainLooper());

        dialog=new ProgressDialog(this);

        top= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottom= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        imgLove=findViewById(R.id.loveImage);
        txtLove=findViewById(R.id.loveText);

        imgLove.startAnimation(bottom);

        txtLove.startAnimation(top);


        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags)
        {
            case Configuration.UI_MODE_NIGHT_YES:
                txtLove.setTextColor(Color.WHITE);
                break;
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        startActivity(new Intent(Splash_Screen.this,MainActivity.class));
                        finish();
                    }
                },4000);

            }
        },1500);
    }
}