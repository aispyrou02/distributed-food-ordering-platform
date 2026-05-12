package com.example.ks_android_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class product_model {
    String name;
    double price;

    int amount = 0;

    public product_model(double price, String name) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount() {
        this.amount++;
    }

    public void remAmount() {
        if (this.amount > 0) {
            this.amount--;
        }
    }
}
