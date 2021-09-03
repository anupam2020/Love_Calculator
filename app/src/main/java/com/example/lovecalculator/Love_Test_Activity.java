package com.example.lovecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.shreyaspatil.material.navigationview.MaterialNavigationView;

public class Love_Test_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button signOut;

    private FirebaseAuth firebaseAuth;

    NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_test);

        signOut=findViewById(R.id.loveTestSignOut);

        firebaseAuth=FirebaseAuth.getInstance();

        nav=findViewById(R.id.navView);
        nav.setNavigationItemSelectedListener(this);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(Love_Test_Activity.this,Login_Activity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.loveTest:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new Love_Test()).commit();
                break;

            case R.id.history:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new History()).commit();
                break;

            case R.id.feedback:
                DynamicToast.make(Love_Test_Activity.this,"Feedback",2000).show();
                break;

            case R.id.shareApp:
                DynamicToast.make(Love_Test_Activity.this,"Share App",2000).show();
                break;

            case R.id.about:
                DynamicToast.make(Love_Test_Activity.this,"About",2000).show();
                break;
        }
        return true;
    }
}