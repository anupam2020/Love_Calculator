package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class Login_Activity extends AppCompatActivity {

    private TextView signUp;
    
    private TextInputEditText email,pass;
    
    private Button signIn;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;
    
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp=findViewById(R.id.loginNoAccount);
        
        email=findViewById(R.id.loginEditText1);
        pass=findViewById(R.id.loginEditText2);
        
        signIn=findViewById(R.id.loginButton);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Users");
        
        dialog=new ProgressDialog(Login_Activity.this);

        if(firebaseAuth.getCurrentUser()!=null)
        {
            if(firebaseAuth.getCurrentUser().isEmailVerified())
            {
                startActivity(new Intent(Login_Activity.this, Love_Test_Activity.class));
                finish();
            }
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login_Activity.this,Register_Activity.class));
                finish();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String sEmail=email.getText().toString();
                String sPass=pass.getText().toString();

                if(sEmail.isEmpty() || sPass.isEmpty())
                {
                    dialog.dismiss();
                    DynamicToast.makeWarning(Login_Activity.this,"Fields cannot be empty!",1500).show();
                }
                else
                {
                    loginUser(sEmail,sPass);
                }

            }
        });

    }

    private void loginUser(String sEmail, String sPass)
    {
            firebaseAuth.signInWithEmailAndPassword(sEmail, sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified())
                        {
                            dialog.dismiss();
                            DynamicToast.makeSuccess(Login_Activity.this,"Login Successful!",2000).show();
                            startActivity(new Intent(Login_Activity.this,Love_Test_Activity.class));
                            finish();
                        }
                        else
                        {
                            dialog.dismiss();
                            DynamicToast.makeError(Login_Activity.this,"Please verify your email!",2000).show();
                        }

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    dialog.dismiss();
                    DynamicToast.makeError(Login_Activity.this,e.getMessage(),2000).show();
                }
            });


    }
}