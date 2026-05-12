package com.example.ks_android_app;

import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String surname;
    private String phone;
    private String address;
    private String tk;
    private String area;
    private String storey;
    private ArrayList<String> search_filters=new ArrayList<String>();



    // standard constructor
    public user(String name, String surname, String phone, String address, String tk, String area, String storey) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.address = address;
        this.tk = tk;
        this.area = area;
        this.storey = storey;

        search_filters.add("any");
        search_filters.add("0.0");
        search_filters.add("5.0");

    }

    public user() {
        search_filters.add("any");
        search_filters.add("0.0");
        search_filters.add("5.0");
    }

    public user(user temp) {
        this.name = temp.getName();
        this.surname = temp.getSurname();
        this.phone = temp.getPhone();
        this.address = temp.getAddress();
        this.tk = temp.getTk();
        this.area = temp.getArea();
        this.storey = temp.getStorey();

        search_filters.clear();
        search_filters.addAll(temp.getSearch_filters());


    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getTk() {
        return tk;
    }

    public String getArea() {
        return area;
    }

    public String getStorey() {
        return storey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setTk(String tk) {
        this.tk = tk;
    }

    public void setStorey(String storey) {
        this.storey = storey;
    }
    public void setPriceRange(String pr){
        search_filters.set(0,pr);
    }
    public void setStarsRange(String l,String h){
        search_filters.set(1,l);
        search_filters.set(2,h);
    }

    public void setCategories(ArrayList<String> cats){
        if (search_filters.size() > 3) {
            search_filters.subList(3, search_filters.size()).clear();
        }
        search_filters.addAll(cats);
    }

    public ArrayList<String> getSearch_filters(){
        return search_filters;
    }

    public String getStarsLow(){
        return search_filters.get(1);
    }
    public String getStarsHigh(){
        return search_filters.get(2);
    }
    public String getPrice(){
        return search_filters.get(0);
    }
    public ArrayList<String> get_cats(){

        return new ArrayList<>(search_filters.subList(3, search_filters.size()));
    }

}