package com.example.ks_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class mainActivity extends  AppCompatActivity{
    private String name,surname,phone,adress,tk,area,storey;

    private user u=new user();

    EditText tName,tSurname,tAdress,tPhone,tTK,tArea,tStorey;
    Button enterButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        tName=findViewById(R.id.text_name);
        tSurname=findViewById(R.id.text_surname);
        tAdress=findViewById(R.id.text_adress);
        tPhone=findViewById(R.id.text_phone);
        tTK=findViewById(R.id.text_tk);
        tStorey=findViewById(R.id.text_storey);
        tArea=findViewById(R.id.text_area);
        enterButton=findViewById(R.id.enter_button);

        Intent intent=getIntent();
        if (intent != null && intent.hasExtra("user")){
            user temp = (user) intent.getSerializableExtra("user");
            u=new user(temp);
        }
        if (u.getName() != null) {
            tName.setText(u.getName());
        }
        if (u.getSurname() != null) {
            tSurname.setText(u.getSurname());
        }
        if (u.getPhone() != null) {
            tPhone.setText(u.getPhone());
        }
        if (u.getAddress() != null) {
            tAdress.setText(u.getAddress());
        }
        if (u.getTk() != null) {
            tTK.setText(u.getTk());
        }
        if (u.getArea() != null) {
            tArea.setText(u.getArea());
        }
        if (u.getStorey() != null) {
            tStorey.setText(u.getStorey());
        }

    }

    public void click_enter(View v){
        name = String.valueOf(tName.getText());
        surname = String.valueOf(tSurname.getText());
        phone = String.valueOf(tPhone.getText());
        adress = String.valueOf(tAdress.getText());
        tk = String.valueOf(tTK.getText());
        area = String.valueOf(tArea.getText());
        storey = String.valueOf(tStorey.getText());

        u.setAddress(adress);
        u.setName(name);
        u.setSurname(surname);
        u.setArea(area);
        u.setPhone(phone);
        u.setTk(tk);
        u.setStorey(storey);

        Intent temp=new Intent(this,browse_stores_activity.class);
        temp.putExtra("user",u);
        temp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(temp);

    }
}
