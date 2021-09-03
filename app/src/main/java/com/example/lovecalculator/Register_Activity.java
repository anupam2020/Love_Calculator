package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {

    private TextView signIn;

    private TextInputEditText name,email,pass;

    private Button signUp;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signIn=findViewById(R.id.registerNoAccount);

        name=findViewById(R.id.registerEditText1);
        email=findViewById(R.id.registerEditText2);
        pass=findViewById(R.id.registerEditText3);

        signUp=findViewById(R.id.registerButton);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Users");

        dialog=new ProgressDialog(Register_Activity.this);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Register_Activity.this,Login_Activity.class));
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String sName=name.getText().toString();
                String sEmail=email.getText().toString();
                String sPass=pass.getText().toString();

                if(sName.isEmpty() || sEmail.isEmpty() || sPass.isEmpty())
                {
                    dialog.dismiss();
                    DynamicToast.makeWarning(Register_Activity.this,"Fields cannot be empty!",1500).show();
                }
                else
                {
                    registerUser(sName,sEmail,sPass);
                }

            }
        });

    }

    private void registerUser(String sName, String sEmail, String sPass)
    {

        firebaseAuth.createUserWithEmailAndPassword(sEmail,sPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                HashMap map=new HashMap();
                                map.put("Name",sName);
                                map.put("Email",sEmail);

                                reference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile")
                                        .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful())
                                        {
                                            dialog.dismiss();
                                            DynamicToast.makeSuccess(Register_Activity.this,"Registration Successful! Please verify your Email...",3000).show();
                                            startActivity(new Intent(Register_Activity.this,Login_Activity.class));
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        dialog.dismiss();
                                        DynamicToast.makeError(Register_Activity.this,e.getMessage(),2000).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            DynamicToast.makeError(Register_Activity.this,e.getMessage(),2000).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                dialog.dismiss();
                DynamicToast.makeError(Register_Activity.this,e.getMessage(),2000).show();
            }
        });

    }
}