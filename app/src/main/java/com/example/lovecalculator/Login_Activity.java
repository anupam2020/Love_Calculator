package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;

public class Login_Activity extends AppCompatActivity {

    private TextView signUp,forgot,loginOR;
    
    private TextInputEditText email,pass;
    
    private Button signIn;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;
    
    private ProgressDialog dialog;

    private ImageView googleBtn,twitterBtn,facebookBtn;

    GoogleSignInClient mGoogleSignInClient;

    private static final String TAG="GOOGLEAUTH";

    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp=findViewById(R.id.loginNoAccount);
        forgot=findViewById(R.id.loginForgot);
        loginOR=findViewById(R.id.loginOR);

        email=findViewById(R.id.loginEditText1);
        pass=findViewById(R.id.loginEditText2);
        
        signIn=findViewById(R.id.loginButton);

        googleBtn=findViewById(R.id.loginGoogleButton);
        twitterBtn=findViewById(R.id.loginTwitterButton);
        facebookBtn=findViewById(R.id.loginFacebookButton);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Users");
        
        dialog=new ProgressDialog(Login_Activity.this);

        if(firebaseAuth.getCurrentUser()!=null)
        {
            if(firebaseAuth.getCurrentUser().isEmailVerified())
            {
                Log.d("getUser",firebaseAuth.getCurrentUser().getEmail());
                Log.d("email verify", String.valueOf(firebaseAuth.getCurrentUser().isEmailVerified()));

                startActivity(new Intent(Login_Activity.this, Love_Test_Activity.class));
                finish();
            }
        }


        int nightModeFlags = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags)
        {
            case Configuration.UI_MODE_NIGHT_YES:
                loginOR.setTextColor(Color.WHITE);
                break;
        }



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("148994927020-20c0aalao06lalrrmur0bkn5pr7bir6t.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(Login_Activity.this, gso);


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });



        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Login_Activity.this,TwitterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Login_Activity.this, FacebookAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });



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
                dialog.setCancelable(false);
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


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                if(email.getText().toString().isEmpty())
                {
                    dialog.dismiss();
                    DynamicToast.makeWarning(Login_Activity.this,"Please enter your email!",1500).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                dialog.dismiss();
                                DynamicToast.makeSuccess(Login_Activity.this,"Password Reset Link was sent to your account!",3000).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            DynamicToast.makeError(Login_Activity.this,e.getMessage(),1500).show();
                        }
                    });
                }

            }
        });


    }



    private void signIn() {
        //For fresh logins
        mGoogleSignInClient.signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            dialog.show();
            dialog.setContentView(R.layout.loading_bg);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                dialog.dismiss();
                DynamicToast.makeError(Login_Activity.this,"Google sign in failed!",3000).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            HashMap map=new HashMap();
                            map.put("Name",firebaseAuth.getCurrentUser().getDisplayName());
                            map.put("Email",firebaseAuth.getCurrentUser().getEmail());

                            reference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile")
                                    .setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        DynamicToast.makeSuccess(Login_Activity.this,"Login Successful!",3000).show();
                                        startActivity(new Intent(Login_Activity.this,Login_Activity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    dialog.dismiss();
                                    DynamicToast.makeError(Login_Activity.this,e.getMessage(),2000).show();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            dialog.dismiss();
                            DynamicToast.makeError(Login_Activity.this, (CharSequence) task.getException(),2000).show();
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(Login_Activity.this);
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