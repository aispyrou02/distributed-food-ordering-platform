package com.example.ks_android_app;

import java.io.Serializable;
import static java.lang.Math.abs;

public class product implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name,type;
    private int amount;
    private int sales=0;
    private double price;
    private boolean isActive=true;

    public product(product p){

    }

    public String get_name(){
        return name;
    }



    public int get_sales(){
        return abs(sales);
    }


    public boolean isActive(){
        return isActive;
    }

    public double get_price(){
        return price;
    }

}


