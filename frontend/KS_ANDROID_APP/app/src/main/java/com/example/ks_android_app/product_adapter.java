package com.example.ks_android_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class product_adapter extends RecyclerView.Adapter<product_adapter.myViewHolder>  {


    Context context;
    ArrayList<product_model> products;

    public product_adapter(Context context, ArrayList<product_model> products){
        this.context=context;
        this.products=products;
    }






    @NonNull
    @Override
    public product_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.product_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull product_adapter.myViewHolder holder, int position) {

        product_model model = products.get(position);

        holder.pName.setText(products.get(position).getName());
        holder.pPrice.setText(products.get(position).getPrice());


        holder.pAmount.setText(String.valueOf(model.getAmount()));


        holder.plusButton.setOnClickListener(v -> {
            model.addAmount();
            holder.pAmount.setText(String.valueOf(model.getAmount()));
        });


        holder.minusButton.setOnClickListener(v -> {
            model.remAmount();
            holder.pAmount.setText(String.valueOf(model.getAmount()));
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{


        TextView pName,pPrice,pAmount;
        ImageButton plusButton,minusButton;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            pName=itemView.findViewById(R.id.p_name);
            pPrice=itemView.findViewById(R.id.p_price);
            pAmount=itemView.findViewById(R.id.p_amount);
            plusButton=itemView.findViewById(R.id.p_plus);
            minusButton=itemView.findViewById(R.id.p_minus);

        }
    }
}
