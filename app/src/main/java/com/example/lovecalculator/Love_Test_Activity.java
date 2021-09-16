package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class Love_Test_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference nameRef;

    private NavigationView nav;

    private ImageView logOut,menu;

    private DrawerLayout drawer;

    private View view;

    private TextView hName,hEmail;

    private TextView topText;

    private String userName,userEmail;

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(Love_Test_Activity.this);
        builder.setTitle("Warning");
        builder.setMessage("Please select any one!");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
                //System.exit(0);
            }
        });

        builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                firebaseAuth.signOut();
                startActivity(new Intent(Love_Test_Activity.this,MainActivity.class));
                finish();
            }
        });

        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_test);

        logOut=findViewById(R.id.testLogout);
        menu=findViewById(R.id.testMenu);
        topText=findViewById(R.id.loveTestTopText);

        drawer=findViewById(R.id.drawer_layout);

        firebaseAuth=FirebaseAuth.getInstance();

        nameRef= FirebaseDatabase.getInstance().getReference("Users");

        nav=findViewById(R.id.navView);

        view=nav.getHeaderView(0);

        hName=view.findViewById(R.id.headerNameText);
        hEmail=view.findViewById(R.id.headerEmailText);

        nav.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Love_Test()).commit();
        nav.getMenu().getItem(1).setChecked(true);
        topText.setText("Love Test");

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(Love_Test_Activity.this);
                builder.setTitle("Logout");
                builder.setMessage("Do you really want to logout?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        firebaseAuth.signOut();
                        startActivity(new Intent(Love_Test_Activity.this,MainActivity.class));
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
        });


        nameRef.child(firebaseAuth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals("Name"))
                    {
                        userName=dataSnapshot.getValue().toString();
                        hName.setText(userName);

                    }
                    if(dataSnapshot.getKey().equals("Email"))
                    {
                        userEmail=dataSnapshot.getValue().toString();
                        hEmail.setText(userEmail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Profile()).commit();
                topText.setText("Profile");
                break;

            case R.id.loveTest:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Love_Test()).commit();
                topText.setText("Love Test");
                break;

            case R.id.history:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new History()).commit();
                topText.setText("History");
                break;

            case R.id.emailVerification:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Verification()).commit();
                topText.setText("Verification");
                break;

            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(Love_Test_Activity.this,MainActivity.class));
                finish();
                break;

            case R.id.feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Feedback()).commit();
                topText.setText("Feedback");
                break;

            case R.id.shareApp:
                shareApp();
                break;

            case R.id.about:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new AboutUs()).commit();
                topText.setText("About Us");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareApp()
    {
        try {
            final String appPackageName = Love_Test_Activity.this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch(Exception e) {
            DynamicToast.makeError(Love_Test_Activity.this,e.getMessage(),2000).show();
        }

    }
}