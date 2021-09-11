package com.example.lovecalculator;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class Verification extends Fragment {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private EditText email;

    private Button send;

    private String sEmail;

    private TextView statusText,marquee;

    private ImageView statusIcon;

    private ProgressDialog dialog;

    SwipeRefreshLayout swipe;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        marquee=view.findViewById(R.id.marqueeText);
        marquee.setSelected(true);

        email=view.findViewById(R.id.verificationEmail);

        send=view.findViewById(R.id.verificationButton);

        statusText=view.findViewById(R.id.verificationStatusText);
        statusIcon=view.findViewById(R.id.verificationStatusIcon);

        swipe=view.findViewById(R.id.swipeToRefresh);

        dialog=new ProgressDialog(getActivity());

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Users");

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        reference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals("Email"))
                    {
                        sEmail=dataSnapshot.getValue().toString();
                        email.setText(sEmail);
                    }
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        if(firebaseAuth.getCurrentUser().isEmailVerified())
        {
            statusText.setText("Verified!");
            //statusText.setTextColor(Color.GREEN);
            statusIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            statusIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
        }
        else
        {
            statusText.setText("Verification Pending!");
            //statusText.setTextColor(Color.RED);
            statusIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
        }


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                if(email.getText().toString().isEmpty())
                {
                    DynamicToast.makeWarning(getActivity(),"Email cannot be empty!",1500).show();
                    dialog.dismiss();
                }
                else
                {
                    FirebaseUser user=firebaseAuth.getCurrentUser();

                    user.updateEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        DynamicToast.makeWarning(getActivity(),"Email Verification Link sent!",1500).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    DynamicToast.makeWarning(getActivity(),e.getMessage(),1500).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                            DynamicToast.makeWarning(getActivity(),e.getMessage(),1500).show();
                        }
                    });

                }
            }
        });



        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                if(user.isEmailVerified())
                {
                    statusText.setText("Verified!");
                    //statusText.setTextColor(Color.GREEN);
                    statusIcon.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    statusIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                }
                else
                {
                    statusText.setText("Verification Pending!");
                    //statusText.setTextColor(Color.RED);
                    statusIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
                }

                swipe.setRefreshing(false);

            }
        });


        Log.d("Status", String.valueOf(firebaseAuth.getCurrentUser().isEmailVerified()));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.FeedbackTheme);

        return inflater.inflate(R.layout.fragment_verification, container, false);
    }
}