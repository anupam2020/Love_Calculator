package com.example.lovecalculator;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;

public class Feedback extends Fragment {

    private TextInputEditText email;
    private Button send;

    private ImageView greenEmoji,yellowEmoji,redEmoji;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email=view.findViewById(R.id.feedbackEditText1);

        send=view.findViewById(R.id.feedbackButton);

        greenEmoji=view.findViewById(R.id.verySatisfied);
        yellowEmoji=view.findViewById(R.id.satisfied);
        redEmoji=view.findViewById(R.id.veryDissatisfied);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("User_Feedback");

        greenEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                greenEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                yellowEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));
                redEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));

                String feedback="Great to hear it from you!";

                DynamicToast.make(getActivity(),feedback,1500).show();

                HashMap map=new HashMap();
                //map.put("Name",firebaseAuth.getCurrentUser().getDisplayName());
                map.put("Feedback",feedback);

                reference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Log.d("Task","Success");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("Task",e.getMessage());
                    }
                });

            }
        });


        yellowEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                greenEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));
                yellowEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.yellow));
                redEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));

                String feedback="Thank You!";

                DynamicToast.make(getActivity(),feedback,1500).show();

                HashMap map=new HashMap();
                //map.put("Name",firebaseAuth.getCurrentUser().getDisplayName());
                map.put("Feedback",feedback);


                reference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Log.d("Task","Success");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("Task",e.getMessage());
                    }
                });

            }
        });


        redEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                greenEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));
                yellowEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.grey));
                redEmoji.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));

                String feedback="We'll try to improve next time!";

                DynamicToast.make(getActivity(),feedback,1500).show();

                HashMap map=new HashMap();
                //map.put("Name",firebaseAuth.getCurrentUser().getEmail());
                map.put("Feedback",feedback);

                reference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Log.d("Task","Success");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("Task",e.getMessage());
                    }
                });

            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=email.getText().toString();

                if(message.isEmpty())
                {
                    DynamicToast.makeWarning(getActivity(),"Please enter the message!",1500).show();
                }
                else
                {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    //email.setData(Uri.parse("mailto:"));
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "anupam00basak@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    email.putExtra(Intent.EXTRA_TEXT, message);

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    try {
                        startActivity(Intent.createChooser(email, "Choose an Email client"));
                    } catch (android.content.ActivityNotFoundException ex) {
                        DynamicToast.makeError(getActivity(), "There is no email client installed.", 1500).show();
                    }
                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.FeedbackTheme);

        return inflater.inflate(R.layout.fragment_feedback, container, false);

    }
}