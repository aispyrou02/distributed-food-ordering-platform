package model;

import java.io.Serializable;

public class product implements Serializable{

    private static final long serialVersionUID = 1L;
    private String name,type;
    private int amount;
    private int sales=0;
    private double price;
    private boolean isActive=true;

    public product(String name, String type, int amount, double price){
        this.name=name;
        this.type=type;
        this.amount=amount;
        this.price=price;
    }

    public String get_name(){
        return name;
    }

    public synchronized boolean set_amount(int a){
        if((amount+a)>=0){
            amount=amount+a;
        }
        return (amount+a)>=0;
    }

    public int get_sales(){
        return sales;
    }

    public void add_sales(int x){
        sales=sales+x;
    }

    public void setActive(boolean a){
        isActive=a;
    }

    public boolean isActive(){
        return isActive;
    }

    public double get_price(){
        return price;
    }

}

