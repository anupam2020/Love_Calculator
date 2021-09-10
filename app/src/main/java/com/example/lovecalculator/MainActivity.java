package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button signIn,signUp;

    ImageView img;

    TextView mainText,mainSubText;

    Animation topTextAnim,subTextAnim,imageAnim,button1Anim,button2Anim;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn=findViewById(R.id.mainSignInButton);
        signUp=findViewById(R.id.mainSignUpButton);
        img=findViewById(R.id.mainImage);

        mainText=findViewById(R.id.mainLoveText);
        mainSubText=findViewById(R.id.mainSubLoveText);

        topTextAnim= AnimationUtils.loadAnimation(this,R.anim.main_window_top);
        subTextAnim= AnimationUtils.loadAnimation(this,R.anim.main_window_top_sub);
        imageAnim= AnimationUtils.loadAnimation(this,R.anim.main_window_image);
        button1Anim= AnimationUtils.loadAnimation(this,R.anim.main_window_button1);
        button2Anim= AnimationUtils.loadAnimation(this,R.anim.main_window_button2);

        mainText.startAnimation(topTextAnim);
        mainSubText.startAnimation(subTextAnim);
        img.startAnimation(imageAnim);
        signIn.startAnimation(button1Anim);
        signUp.startAnimation(button2Anim);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {

                startActivity(new Intent(MainActivity.this,Love_Test_Activity.class));
                finish();


        }


        //DynamicToast.make(this,"Email: "+firebaseAuth.getCurrentUser().isEmailVerified(),3000).show();


        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags)
        {
            case Configuration.UI_MODE_NIGHT_YES:
                mainText.setTextColor(Color.WHITE);

                signIn.setBackgroundResource(R.drawable.button_bg_white);
                signIn.setTextColor(Color.BLACK);

                signUp.setBackgroundResource(R.drawable.white_border);
                signUp.setTextColor(Color.WHITE);

                break;
        }


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Register_Activity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you really want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();
    }

}