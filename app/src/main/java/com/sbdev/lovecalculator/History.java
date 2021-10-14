package com.sbdev.lovecalculator;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lovecalculator.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

public class History extends Fragment {

    private RecyclerView recyclerView;
    private Item_Adapter adapter;
    private ArrayList<Item_Model> arrayList;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference reference;

    private String myKey;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.historyRecyclerView);

        arrayList=new ArrayList<>();

        adapter=new Item_Adapter(arrayList,getActivity());
        recyclerView.setAdapter(adapter);

        dialog=new ProgressDialog(getActivity());

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Love_Tests");

        reference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    //Log.d("Keys",dataSnapshot.getKey());

                    myKey=dataSnapshot.getKey();

                    arrayList.add(new Item_Model(myKey,String.valueOf(dataSnapshot.child("Name").getValue()),String.valueOf(dataSnapshot.child("Partner").getValue()),String.valueOf(dataSnapshot.child("Percentage").getValue()),String.valueOf(dataSnapshot.child("Result").getValue()),String.valueOf(dataSnapshot.child("Date").getValue())));
                }

                dialog.dismiss();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                dialog.dismiss();
                DynamicToast.makeError(getActivity(),error.getMessage(),2000).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.getContext().setTheme(R.style.AppTheme);

        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}