package com.example.ks_android_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Objects;

public class store_model {
    String name, image_path;
    float rating;

    public byte[] getImageData() {
        return imageData;
    }

    private byte[] imageData;

    ArrayList<product> products=new ArrayList<product>();

    public store_model(String name, byte[] imageData, float rating) {
        this.name = name;
        this.imageData = Objects.requireNonNull(imageData, "imageData").clone();
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getRating() {
        return String.valueOf(rating);
    }

    public ArrayList<product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<product> prs){
        products.clear();
        for (product pr:prs){
            if(pr.isActive()){
                products.add(pr);
            }
        }

    }
    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }
}
