package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;

public class Love_Test_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private NavigationView nav;

    private ImageView logOut,menu;

    private DrawerLayout drawer;

    private View view;

    private TextView hName,hEmail;

    private TextView topText;

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(Love_Test_Activity.this);
        builder.setTitle("Warning");
        builder.setMessage("Please select any one!");

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
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

        reference= FirebaseDatabase.getInstance().getReference("Users");

        nav=findViewById(R.id.navView);

        view=nav.getHeaderView(0);

        hName=view.findViewById(R.id.headerNameText);
        hEmail=view.findViewById(R.id.headerEmailText);

        nav.setNavigationItemSelectedListener(this);

        hName.setText(firebaseAuth.getCurrentUser().getDisplayName());
        hEmail.setText(firebaseAuth.getCurrentUser().getEmail());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Love_Test()).commit();
        nav.getMenu().getItem(0).setChecked(true);
        topText.setText("Love Test");

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(Love_Test_Activity.this,Login_Activity.class));
                finish();
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

            case R.id.loveTest:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Love_Test()).commit();
                topText.setText("Love Test");
                break;

            case R.id.history:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new History()).commit();
                topText.setText("History");
                break;

            case R.id.feedback:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Feedback()).commit();
                topText.setText("Feedback");
                break;

            case R.id.shareApp:
                DynamicToast.make(Love_Test_Activity.this,"Share App",2000).show();
                break;

            case R.id.about:
                DynamicToast.make(Love_Test_Activity.this,"About",2000).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}