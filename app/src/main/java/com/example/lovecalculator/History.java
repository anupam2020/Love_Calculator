package com.example.lovecalculator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

public class History extends Fragment {

    RecyclerView recyclerView;
    Item_Adapter adapter;
    ArrayList<Item_Model> arrayList;

    FirebaseAuth firebaseAuth;

    DatabaseReference reference;

    String myKey;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.historyRecyclerView);

        arrayList=new ArrayList<>();

        adapter=new Item_Adapter(arrayList,getActivity());
        recyclerView.setAdapter(adapter);

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

                    Log.d("Key",myKey);
                    Log.d("Name",String.valueOf(dataSnapshot.child("Name").getValue()));
                    Log.d("Partner",String.valueOf(dataSnapshot.child("Partner").getValue()));
                    Log.d("Percentage",String.valueOf(dataSnapshot.child("Percentage").getValue()));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                DynamicToast.makeError(getActivity(),error.getMessage(),1500).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
}