package com.example.lovecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoveResultActivity extends AppCompatActivity {

    private TextView name,partner,result,percentage;

    private RelativeLayout relativeLayout;

    private String sName,sPartner,sResult,sPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_result);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        name=findViewById(R.id.loveResultName);
        partner=findViewById(R.id.loveResultPartner);
        result=findViewById(R.id.loveResult);
        percentage=findViewById(R.id.loveResultPercentage);

        relativeLayout=findViewById(R.id.loveResultRelative2);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        sName=getIntent().getStringExtra("Name");
        sPartner=getIntent().getStringExtra("Partner");
        sResult=getIntent().getStringExtra("Result");
        sPercentage=getIntent().getStringExtra("Percentage");

        name.setText(sName);
        partner.setText(sPartner);
        result.setText(sResult);
        percentage.setText(sPercentage+"%");
    }

}