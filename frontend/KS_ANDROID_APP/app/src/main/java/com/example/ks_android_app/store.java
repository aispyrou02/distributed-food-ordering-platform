package com.example.ks_android_app;

import java.util.ArrayList;
import java.io.Serializable;

public class store  implements Serializable{

    private static final long serialVersionUID = 1L;


    private String name,category,logo,price_range;
    private double lat,longitude;
    private float stars;
    private int  NOofvotes;

    public byte[] getImageData() {
        return imageData;
    }

    private byte[] imageData;

    public ArrayList<product> getProducts() {
        return products;
    }

    private ArrayList<product> products= new ArrayList<product>();



    public store(store s) {

    }

    public String get_price(){
        return price_range;
    }



    public int total_sales(String p ){
        int i=-1;
        for (product pr:products){
            i++;
            if (pr.get_name().equals(p)){

                break;
            }
        }

        return products.get(i).get_sales();
    }


    public String toString(){
        String temp;
        temp= name+"  "+String.format("%.2f", stars)+"\n"+"----------------------- "+"\n";
        for (product p:products){
            if(p.isActive()){
                temp=temp+p.get_name()+"  "+ p.get_price()+"\n";
            }
        }
        return temp;
    }

    public String getName(){
        return name;
    }


    public float get_stars(){
        return Math.round(stars * 10f) / 10f;
    }

    public String get_cat(){
        return category;
    }


    public String getLogo() {
        return logo;
    }
}
