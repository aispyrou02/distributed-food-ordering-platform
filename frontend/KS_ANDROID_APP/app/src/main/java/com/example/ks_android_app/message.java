package com.example.ks_android_app;






import java.util.ArrayList;
import java.io.Serializable;








public class message implements Serializable{

    private static final long serialVersionUID = 1L;

    private String command,responce;
    private ArrayList<String> data= new ArrayList<String>();
    private boolean resp=false;
    private ArrayList<store> stores= new ArrayList<store>();
    private int code=0;


    public int getSize(){
        return data.size();
    }
    public int getCode(){
        return code;
    }

    public void setCode(int code){
        this.code=code;
    }

    public message(String command){
        this.command=command;
    }

    public void addData(String d){
        data.add(d);
    }


    public String getData(int i){
        return data.get(i);
    }


    public String getCommand(){
        return command;
    }

    public void set_resp(boolean x){
        resp=x;
    }

    public void set_responce(String a){
        responce=a;
    }

    public String toString(){

        return responce;

    }




    public void setStores(ArrayList<store> stores2) {
        stores.addAll(stores2);
    }

    public ArrayList<store> getStores() {
        return new ArrayList<>(stores);
    }













}
