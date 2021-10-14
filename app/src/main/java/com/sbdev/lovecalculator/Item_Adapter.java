package com.sbdev.lovecalculator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovecalculator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;

public class Item_Adapter extends RecyclerView.Adapter<Item_Adapter.ItemViewHolder>
{

    private ArrayList<Item_Model> arrayList;
    private Context context;

    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Love_Tests");

    public Item_Adapter(ArrayList<Item_Model> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.history_design_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        holder.name.setText("Name: "+arrayList.get(position).name);
        holder.partner.setText("Partner: "+arrayList.get(position).partner);
        holder.percentage.setText("Percentage: "+arrayList.get(position).percentage);
        holder.result.setText("Result: "+arrayList.get(position).result);
        holder.date.setText(arrayList.get(position).date);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Warning");
                builder.setMessage("Do you really want to delete this item?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        ProgressDialog pDialog=new ProgressDialog(context);

                        pDialog.show();
                        pDialog.setContentView(R.layout.loading_bg);
                        pDialog.setCancelable(false);
                        pDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        reference.child(firebaseAuth.getCurrentUser().getUid()).child(arrayList.get(position).key)
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    pDialog.dismiss();
                                    DynamicToast.make(context, "Item successfully deleted!", v.getResources().getDrawable(R.drawable.ic_baseline_delete_outline_24),
                                            v.getResources().getColor(R.color.white), v.getResources().getColor(R.color.black), 2000).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                pDialog.dismiss();
                                DynamicToast.makeError(context,e.getMessage(),2000).show();
                            }
                        });

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
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView partner;
        TextView percentage;
        TextView result;
        TextView date;

        ImageView delete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.historyName);
            partner=itemView.findViewById(R.id.historyPartner);
            percentage=itemView.findViewById(R.id.historyPercentage);
            result=itemView.findViewById(R.id.historyResult);
            date=itemView.findViewById(R.id.historyDateTime);
            delete=itemView.findViewById(R.id.historyDelete);
        }
    }

}
