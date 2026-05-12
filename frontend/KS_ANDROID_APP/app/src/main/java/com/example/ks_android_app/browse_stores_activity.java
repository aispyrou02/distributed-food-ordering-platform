package com.example.ks_android_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class browse_stores_activity extends AppCompatActivity {


    private String serverIp="10.0.2.2";
    private  int serverPort=4000;
    public user u=new user();
    private ArrayList<store> stores=new ArrayList<store>();
    private ArrayList<store_model> store_models=new ArrayList<store_model>();

    private RecyclerView rec_stores;
    private ImageButton filter_button;
    private Button userinfo_button;
    private TextView welcome;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);

        rec_stores=findViewById(R.id.store_recycle);
        filter_button=findViewById(R.id.filter_button);
        userinfo_button=findViewById(R.id.adress_button);
        welcome=findViewById(R.id.welcome_text);

        String temp_text="Welcome ";
        Intent intent=getIntent();
        if (intent != null && intent.hasExtra("user")){
            user temp = (user) intent.getSerializableExtra("user");
            u=new user(temp);



        }
        if (u.getAddress() != null && !u.getAddress().equals("") ) {
            userinfo_button.setText(u.getAddress());
        }
        else{
            userinfo_button.setText("Adress");
        }
        if (u.getName() != null) {
            temp_text=temp_text+u.getName()+" ";
        }
        if (u.getSurname() != null) {
            temp_text=temp_text+u.getSurname();
        }
        welcome.setText(temp_text);

        final Context ctx = browse_stores_activity.this;
        final Activity thisActivity = this;



        new Thread(new Runnable() {

            @Override
            public void run() {

                try {


                    Socket master = new Socket(serverIp, serverPort);
                    master.setSoTimeout(10_000);
                    ObjectOutputStream out = new ObjectOutputStream(master.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(master.getInputStream());

                    message req = new message("Search");

                    ArrayList<String> filters = new ArrayList<String>();
                    filters = u.getSearch_filters();
                    for (String s : filters) {
                        req.addData(s);
                    }

                    out.writeObject(req);
                    out.flush();

                    message response = (message) in.readObject();



                    stores.clear();
                    stores.addAll(response.getStores());



                    master.close();

                }
                catch (IOException e){
                    Log.e("BrowseStores", "TCP fetch failed", e);}
                catch (ClassNotFoundException e) {Log.e("BrowseStores", "Deserialization failed", e);}



                thisActivity.runOnUiThread(() -> {
                    store_models.clear();
                    setStore_models();
                    store_adapter s_adapter=new store_adapter(ctx,store_models,u);
                    rec_stores.setAdapter(s_adapter);
                    rec_stores.setLayoutManager(new LinearLayoutManager(ctx));
                });

            }
        }).start();


    }



    private void setStore_models(){
        for (store s:stores){
            store_model temp = new store_model(s.getName(),s.getImageData(),s.get_stars());
            temp.setProducts(s.getProducts());
            store_models.add(temp);

        }
    }

    public void click_adress(View v){
        Intent temp=new Intent(this,mainActivity.class);
        temp.putExtra("user",u);
        startActivity(temp);

    }

    public void click_filters(View v){
        Intent temp=new Intent(this,filters_activity.class);
        temp.putExtra("user",u);
        startActivity(temp);
    }
    public user getUser(){
        return u;
    }

}
