package com.example.ks_android_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class store_adapter extends RecyclerView.Adapter<store_adapter.myViewHolder> {

    Context context;
    user u;
    ArrayList<store_model> stores;

    public store_adapter(Context context, ArrayList<store_model> stores,user u){
        this.context=context;
        this.stores=stores;
        this.u=u;
    }








    @NonNull
    @Override
    public store_adapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.store_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull store_adapter.myViewHolder holder, int position) {

        holder.storeRating.setText(stores.get(position).getRating()+"★");
        holder.storeName.setText(stores.get(position).getName());

        holder.storeImage.setImageBitmap(stores.get(position).getBitmap());

        holder.storeImage.setOnClickListener(v -> {

            Intent temp = new Intent(context, store_activity.class);
            temp.putExtra("store_name", stores.get(position).getName());
            temp.putExtra("store_rating", stores.get(position).getRating());
            temp.putExtra("store_products", stores.get(position).getProducts());
            temp.putExtra("store_image", stores.get(position).getImageData());
            temp.putExtra("user", u);
            context.startActivity(temp);
        });
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{


        ImageButton storeImage;
        TextView storeName,storeRating;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage=itemView.findViewById(R.id.store_image) ;
            storeName=itemView.findViewById(R.id.store_name) ;
            storeRating=itemView.findViewById(R.id.store_rating) ;
        }
    }
















}
