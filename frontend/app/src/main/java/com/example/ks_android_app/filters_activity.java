package com.example.ks_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class filters_activity extends AppCompatActivity {



    private user u;
    private ArrayList<String> store_cats=new ArrayList<String>();
    Button applyButton;
    ImageButton backButton,catB,rateLp,rateLm,rateHp,rateHm,priceB;
    TextView cat,rate,priceR,priceRvalue,ratelow,ratehigh;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filters);

        applyButton=findViewById(R.id.apply_button);
        backButton=findViewById(R.id.back_button);
        catB=findViewById(R.id.menu_category_button);
        rateLp=findViewById(R.id.plus_button1);
        rateLm=findViewById(R.id.minus_button1);
        rateHp=findViewById(R.id.plus_button2);
        rateHm=findViewById(R.id.minus_button2);
        priceB=findViewById(R.id.menu_price_button);
        cat=findViewById(R.id.category);
        rate=findViewById(R.id.rating);
        priceR=findViewById(R.id.price);
        priceRvalue=findViewById(R.id.price_value);
        ratelow=findViewById(R.id.low_stars);
        ratehigh=findViewById(R.id.high_stars);


        Intent intent=getIntent();
        if (intent != null && intent.hasExtra("user")){
            user temp = (user) intent.getSerializableExtra("user");
            u=new user(temp);
        }
        else{
            u=new user();
        }
        ratelow.setText(u.getStarsLow());
        ratehigh.setText(u.getStarsHigh());
        priceRvalue.setText(u.getPrice());
        store_cats=u.get_cats();

        catB.setOnClickListener(v -> {
            showCatMenu(v);
        });
        priceB.setOnClickListener(v -> {
            showPriceMenu(v);
        });
        rateLp.setOnClickListener(v -> {
            double x;
            x=Double.parseDouble(ratelow.getText().toString());
            if (x<5){
                x=x+0.5;
            }
            float y= (float) x;
            ratelow.setText(String.valueOf(y));
        });

        rateLm.setOnClickListener(v -> {
            double x;
            x=Double.parseDouble(ratelow.getText().toString());
            if (x>0){
                x=x-0.5;
            }
            float y= (float) x;
            ratelow.setText(String.valueOf(y));

        });

        rateHp.setOnClickListener(v -> {
            double x;
            x=Double.parseDouble(ratehigh.getText().toString());
            if(x<5){
                x=x+0.5;
            }
            float y= (float) x;
            ratehigh.setText(String.valueOf(y));
        });

        rateHm.setOnClickListener(v -> {
            double x;
            x=Double.parseDouble(ratehigh.getText().toString());
            if(x>0){
                x=x-0.5;
            }
            float y= (float) x;
            ratehigh.setText(String.valueOf(y));
        });

    }
    private void showCatMenu(View v){
        PopupMenu popupMenu=new PopupMenu(filters_activity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.categorymenu,popupMenu.getMenu());
        int green = ContextCompat.getColor(this, R.color.green);

        Menu menu = popupMenu.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);

            // titles in the XML are capitalised; store_cats holds lower-case keys
            String key = mi.getTitle().toString().toLowerCase(Locale.ROOT);

            if (store_cats.contains(key)) {
                SpannableString s = new SpannableString(mi.getTitle());
                s.setSpan(new ForegroundColorSpan(green), 0, s.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mi.setTitle(s);
            }
        }





        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.asian){
                    if (!store_cats.remove("asian")) {
                        store_cats.add("asian");
                    }
                }
                if(item.getItemId()==R.id.italian){
                    if (!store_cats.remove("italian")) {
                        store_cats.add("italian");
                    }
                }
                if(item.getItemId()==R.id.coffee){
                    if (!store_cats.remove("coffee")) {
                        store_cats.add("coffee");
                    }
                }
                if(item.getItemId()==R.id.burger){
                    if (!store_cats.remove("burger")) {
                        store_cats.add("burger");
                    }
                }
                if(item.getItemId()==R.id.souvlaki){
                    if (!store_cats.remove("souvlaki")) {
                        store_cats.add("souvlaki");
                    }
                }
                if(item.getItemId()==R.id.pizza){
                    if (!store_cats.remove("pizza")) {
                        store_cats.add("pizza");
                    }
                }
                if(item.getItemId()==R.id.bakery){
                    if (!store_cats.remove("bakery")) {
                        store_cats.add("bakery");
                    }
                }
                if(item.getItemId()==R.id.sawndwich){
                    if (!store_cats.remove("sawndwich")) {
                        store_cats.add("sawndwich");
                    }
                }

                return false;
            }
        });
        popupMenu.show();
    }
    private void showPriceMenu(View v){
        PopupMenu popupMenu=new PopupMenu(filters_activity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.pricemenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String value=String.valueOf(priceRvalue.getText());
                if(item.getItemId()==R.id.one){
                    if(value.equals("$")){
                        value="any";
                    }
                    else{
                        value="$";
                    }
                }
                if(item.getItemId()==R.id.two){
                    if(value.equals("$$")) {
                        value = "any";
                    }
                    else{
                        value="$$";
                    }
                }
                if(item.getItemId()==R.id.three){
                    if(value.equals("$$$")){
                        value="any";
                    }
                    else{
                        value="$$$";
                    }
                }
                priceRvalue.setText(value);
                return false;
            }
        });
        popupMenu.show();
    }
    public void click_back(View v){
        Intent temp=new Intent(this,browse_stores_activity.class);
        temp.putExtra("user",u);
        temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(temp);

    }
    public void click_apply(View v){


        u.setStarsRange(String.valueOf(ratelow.getText()),String.valueOf(ratehigh.getText()));

        u.setPriceRange(priceRvalue.getText().toString());

        u.setCategories(store_cats);

        store_cats.clear();

        Intent temp=new Intent(this,browse_stores_activity.class);
        temp.putExtra("user",u);
        temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(temp);


    }




}
