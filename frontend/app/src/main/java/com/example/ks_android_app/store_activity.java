package com.example.ks_android_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class store_activity extends AppCompatActivity {

    private String serverIp="10.0.2.2";
    private  int serverPort=4000;
    private user u;
    private message response;
    private ArrayList<String> buyMessage = new ArrayList<String>();
    RecyclerView rec_products;
    ImageView sImage;
    TextView sName,sRating;
    Button buyButton;
    ImageButton backButton;
    ArrayList<product> products=new ArrayList<product>();
    ArrayList<product_model> product_models=new ArrayList<product_model>();
    private String name;
    final Context ctx = store_activity.this;
    final Activity thisActivity = this;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_page);

        rec_products=findViewById(R.id.product_recycler);
        sImage=findViewById(R.id.simage);
        sName=findViewById(R.id.sname);
        sRating=findViewById(R.id.srating);
        buyButton=findViewById(R.id.buy_button);
        backButton=findViewById(R.id.back_button_store);

        sImage.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        sImage.setClipToOutline(true);

        Intent intent=getIntent();
        if (intent != null && intent.hasExtra("user")){
            user temp = (user) intent.getSerializableExtra("user");
            u=new user(temp);
            name=intent.getStringExtra("store_name");
            String rating=intent.getStringExtra("store_rating");
            String image_path=intent.getStringExtra("store_image");
            products=(ArrayList<product>) intent.getSerializableExtra("store_products");

            sName.setText(name);
            sRating.setText(rating+"★");

            byte[] bytes = getIntent().getByteArrayExtra("store_image");
            Bitmap bmp   = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            sImage.setImageBitmap(bmp);

        }


        setProduct_models();

        product_adapter p_adapter=new product_adapter(this,product_models);

        rec_products.setAdapter(p_adapter);
        rec_products.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setProduct_models(){
        for (product p:products){
            product_model temp = new product_model(p.get_price(),p.get_name());
            product_models.add(temp);

        }
    }

    public void click_completeBuy(View v) {

        ArrayList<product_model> selected = new ArrayList<>();
        for (product_model pm : product_models) {
            if (pm.getAmount() > 0) {
                selected.add(pm);
            }
        }
        if (selected.isEmpty()) {
            // nothing selected — tell the user
            Toast.makeText(this, "No items selected!", Toast.LENGTH_SHORT).show();

        } else {
            buyMessage.clear();
            buyMessage.add(name);
            for (product_model pr : selected) {
                buyMessage.add(pr.getName());
                buyMessage.add(String.valueOf(pr.getAmount()));
            }

            if (u.getName() == null || u.getName().isBlank() ||
                    u.getSurname() == null || u.getSurname().isBlank() ||
                    u.getPhone() == null || u.getPhone().isBlank() ||
                    u.getAddress() == null || u.getAddress().isBlank() ||
                    u.getTk() == null || u.getTk().isBlank() ||
                    u.getArea() == null || u.getArea().isBlank() ||
                    u.getStorey() == null || u.getStorey().isBlank()) {
                Toast.makeText(this, "You need to fill in your info.", Toast.LENGTH_SHORT).show();
                Intent temp = new Intent(this, mainActivity.class);
                temp.putExtra("user", u);
                startActivity(temp);
            } else {



                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {


                            Socket master = new Socket(serverIp, serverPort);
                            master.setSoTimeout(10_000);
                            ObjectOutputStream out = new ObjectOutputStream(master.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(master.getInputStream());

                            message req = new message("Buy");

                            for (String s : buyMessage) {
                                req.addData(s);
                            }

                            out.writeObject(req);
                            out.flush();

                            response = (message) in.readObject();



                            master.close();

                        } catch (IOException e) {
                            Log.e("BrowseStores", "TCP fetch failed", e);
                        } catch (ClassNotFoundException e) {
                            Log.e("BrowseStores", "Deserialization failed", e);
                        }

                        thisActivity.runOnUiThread(() -> {
                            Toast.makeText(thisActivity, response.toString(), Toast.LENGTH_SHORT).show();

                            sRating.setClickable(true);
                            sRating.setFocusable(true);

                            int color = ContextCompat.getColor(thisActivity, R.color.color_1);
                            sRating.setBackgroundColor(color);

                            sRating.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showRatingMenu(v);
                                }
                            });

                        });
                    }
                }).start();
            }
        }
    }
    private void showRatingMenu(View v){
        PopupMenu popupMenu=new PopupMenu(store_activity.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.ratingmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                String rate="0.0";

                if(item.getItemId()==R.id.one){
                    rate="1.0";
                }
                if(item.getItemId()==R.id.two){
                    rate="2.0";
                }
                if(item.getItemId()==R.id.three){
                    rate="3.0";
                }
                if(item.getItemId()==R.id.four){
                    rate="4.0";
                }
                if(item.getItemId()==R.id.five){
                    rate="5.0";
                }

                message req=new message("rateStore");
                req.addData(name);
                req.addData(rate);


                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {


                            Socket master = new Socket(serverIp, serverPort);
                            master.setSoTimeout(10_000);
                            ObjectOutputStream out = new ObjectOutputStream(master.getOutputStream());
                            ObjectInputStream in = new ObjectInputStream(master.getInputStream());


                            out.writeObject(req);
                            out.flush();

                            response = (message) in.readObject();


                            master.close();

                        } catch (IOException e) {
                            Log.e("BrowseStores", "TCP fetch failed", e);
                        } catch (ClassNotFoundException e) {
                            Log.e("BrowseStores", "Deserialization failed", e);
                        }
                        thisActivity.runOnUiThread(() -> {

                            Toast.makeText(thisActivity, response.toString(), Toast.LENGTH_SHORT).show();

                        });
                    }
                }).start();
                sRating.setClickable(false);
                sRating.setFocusable(false);

                int color = ContextCompat.getColor(thisActivity, R.color.background_primary);
                sRating.setBackgroundColor(color);


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



}
