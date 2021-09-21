package com.example.lovecalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Love_Test extends Fragment {

    private TextInputEditText name,partner;

    private ImageView imgGo;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name=view.findViewById(R.id.loveTestEditText1);
        partner=view.findViewById(R.id.loveTestEditText2);

        imgGo=view.findViewById(R.id.loveTestImageView);

        dialog=new ProgressDialog(getActivity());

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Love_Tests");


        imgGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String sName=name.getText().toString();
                String sPartner=partner.getText().toString();

                if(sName.isEmpty() || sPartner.isEmpty())
                {
                    dialog.dismiss();
                    DynamicToast.makeWarning(getActivity(),"Fields cannot be empty!",2000).show();
                }
                else
                {
                    testLove(sName,sPartner);
                }

            }
        });

    }

    private void testLove(String sName, String sPartner)
    {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://love-calculator.p.rapidapi.com/getPercentage?fname="+sName+"&sname="+sPartner)
                .get()
                .addHeader("x-rapidapi-host", "love-calculator.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        dialog.dismiss();

                        dialog.show();
                        dialog.setContentView(R.layout.no_internet);
                        dialog.setCancelable(true);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response!=null)
                {

                    String res=response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd MMM yyyy hh:mm a");
                                Date date=new Date();
                                String strTime=simpleDateFormat.format(date);

                                JSONObject jsonObject=new JSONObject(res);

                                String percentage=jsonObject.getString("percentage");
                                String result=jsonObject.getString("result");


                                HashMap<String,String> map=new HashMap();
                                map.put("Name",sName);
                                map.put("Partner",sPartner);
                                map.put("Percentage",percentage);
                                map.put("Result",result);
                                map.put("Date",strTime);

                                reference.child(firebaseAuth.getCurrentUser().getUid()).push().setValue(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        dialog.dismiss();

                                        Intent intent=new Intent(getActivity(),LoveResultActivity.class);
                                        intent.putExtra("Name",sName);
                                        intent.putExtra("Partner",sPartner);
                                        intent.putExtra("Result",result);
                                        intent.putExtra("Percentage",percentage);
                                        startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        dialog.dismiss();
                                        DynamicToast.makeError(getActivity(),e.getMessage(),2000).show();
                                    }
                                });

                            } catch (JSONException e) {

                                dialog.dismiss();
                                DynamicToast.makeError(getActivity(),e.getMessage(),2000).show();
                            }

                        }
                    });

                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.AppTheme);

        return inflater.inflate(R.layout.fragment_love_test, container, false);
    }
}