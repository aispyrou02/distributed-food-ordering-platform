package model;
import java.util.ArrayList;
import com.google.gson.*;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class store  implements Serializable{

    private static final long serialVersionUID = 1L;

     
    private String name,category,logo,price_range;
    private double lat,longitude;
    private float stars;
    private int  NOofvotes;
    private ArrayList<product> products= new ArrayList<product>();



    public store(String name, String category, String logo, double lat, double longitude, float stars, int NOofvotes, JsonArray productList) {
        this.name = name;
        this.category = category;
        this.logo = logo;
        this.lat = lat;
        this.longitude = longitude;
        this.stars = stars;
        this.NOofvotes = NOofvotes;
    
        
        for (JsonElement element : productList) {
            JsonObject jsonObject = element.getAsJsonObject();
    
            String pName = jsonObject.get("ProductName").getAsString();
            String pType = jsonObject.get("ProductType").getAsString();
            int amount = jsonObject.get("Available Amount").getAsInt();
            double price = jsonObject.get("Price").getAsDouble();
    
            product temp = new product(pName, pType, amount, price);
            products.add(temp);
        }
    
        calc_price_range();
    }

    public String get_price(){
        return price_range;
    }

    public void calc_price_range(){
        int i=0;
        double sum=0;
        for (product pr:products){
                sum=sum+pr.get_price();
                i++;      
        }
        sum=sum/i;
        if (sum<=5){price_range="$";}
        else if (sum<=15){price_range="$$";}
        else{price_range="$$$";}
    }


    public void add_product(String p){


        JsonObject jsonObject = JsonParser.parseString(p).getAsJsonObject();

        String pName = jsonObject.get("ProductName").getAsString();
        String pType = jsonObject.get("ProductType").getAsString();
        int amount = jsonObject.get("Available Amount").getAsInt();
        double price = jsonObject.get("Price").getAsDouble();

        product temp=new product(pName, pType, amount, price);

        products.add(temp);

        calc_price_range();


    }


    public void remove_product(String p){
        int i=-1;
        for (product pr:products){
            if (pr.get_name().equals(p)){
                i++;
                break;
            }
        }

        products.get(i).setActive(false);

    }



    public boolean set_Available(String p ,int a){
        int i=-1;
        boolean flag=false;
        for (product pr:products){
            if (pr.get_name().equals(p)){
                i++;
                flag=true;
                break;
                
            }
        }
        if(!flag){return flag;}
        flag=products.get(i).set_amount(a);
        return flag;
    }


    public int total_sales(String p ){
        int i=-1;
        for (product pr:products){
            if (pr.get_name().equals(p)){
                i++;
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


    public void rate_store(float x){
        stars=((stars*NOofvotes)+x)/(NOofvotes+1);
        NOofvotes++;
    }

    public boolean Buy(String pname,int amount){
        boolean flag=set_Available(pname, amount);
        if(flag){
            int i=-1;           
            for (product pr:products){
                if (pr.get_name().equals(pname)){
                    i++;
                    break;                    
                }
            }
            
            products.get(i).add_sales(amounMath.abs(amount));
        }
        return flag;
    }

    public float get_stars(){
        return stars;
    }

    public String get_cat(){
        return category;
    }







}
