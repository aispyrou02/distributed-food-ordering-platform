package network;

import model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.google.gson.*;
import java.lang.reflect.Array;

public class worker {
    

    private int port,reducer_port;
    private ServerSocket worker_server;
    
    private ArrayList<store> stores=new ArrayList<store>();


    public static void main(String args[]){
        int worker_port = Integer.parseInt(args[0]);
        int reducer_port = Integer.parseInt(args[1]);
        worker worker_exe=new worker(worker_port,reducer_port);
        worker_exe.run();
    }





    public worker(int port,int port2){
        this.port=port;
        this.reducer_port=port2;
        try{
            worker_server=new ServerSocket(port);

        }
        catch (IOException e) {
            System.out.println("could not initialize worker, try again");
            System.exit(0);
        }
    }


    public void run(){
        try{

            
            while(true){

                Socket master_req = worker_server.accept();
                processCommand prCommand=new processCommand(master_req);
                Thread workerThread = new Thread(prCommand);
                workerThread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }




    class processCommand implements Runnable{

        String command;
        Socket master_req;
        message req;
        ObjectOutputStream out;
        ObjectInputStream in;
        int code;

        public processCommand(Socket master_req){
            
            
            this.master_req=master_req;
            
            
        }
        public void send_resp(String output,int code) throws IOException{
            message resp=new message("resp");
            resp.set_resp(true);
            resp.set_responce(output);
            resp.setCode(code);
            out.writeObject(resp);
            out.flush();
            
        }



        public void run(){

            try{
                out = new ObjectOutputStream(master_req.getOutputStream());
                in = new ObjectInputStream(master_req.getInputStream());
                try{
                    req = (message) in.readObject();

                }
                catch (ClassNotFoundException e){
                    send_resp("error in communication",code);
                }

            }
            catch(IOException e){
                e.printStackTrace();
            }

            command=req.getCommand();
            code=req.getCode();


            if(command.equals("addStore")){  
     
                try{

                    String json =req.getData(0);
                

                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

                    String SName = jsonObject.get("StoreName").getAsString();
                    double lat = jsonObject.get("Latitude").getAsDouble();
                    double longitude = jsonObject.get("Longitude").getAsDouble();
                    String foodcategory = jsonObject.get("FoodCategory").getAsString();
                    float stars = jsonObject.get("Stars").getAsFloat();
                    int votes = jsonObject.get("NoOfVotes").getAsInt();
                    String logo = jsonObject.get("StoreLogo").getAsString();
                    JsonArray productList = jsonObject.getAsJsonArray("Products");

                    store temp=new store(SName, foodcategory, logo, lat, longitude, stars, votes, productList);
    
                    stores.add(temp);
                    
                    
                    
                    
                    try{
                        send_resp("Store added",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                }
                   
                
                                
                catch (ArrayIndexOutOfBoundsException e){
                    try{
                        send_resp("Wrong data input",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                }

            }
            
            else if(command.equals("updateProduct")) {

                try{
                    String store_name = req.getData(0);
                    String operation = req.getData(1);
                    

                    store target = null;

                    for (store s : stores){
                        if (s.getName().equals(store_name)){
                            target = s;
                            break;
                        }
                    }

                    if (target == null){
                        try{
                            send_resp("Store does not exist",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}            
                    }

                    if (operation.equals("add")){
                        String product_json = req.getData(2);
                        target.add_product(product_json);
                        try{
                            send_resp("Product added",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }

                    else if (operation.equals("remove")){
                        String pname = req.getData(2);
                        target.remove_product(pname);
                        try{
                            send_resp("Product removed",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }

                    else{
                        try{
                            send_resp("Unknown operation",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }

                }
                catch(Exception e){

                    e.printStackTrace();
                                
                    try{
                        send_resp("Worker error on update product",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                    

                }

        }
            else if(command.equals("updateAvailable")){

                try{
                    String s_name = req.getData(0);
                    String p_name = req.getData(1);
                    int amount = Integer.parseInt(req.getData(2));
        
                    store target = null;
                    for(store s:stores){
                        if(s.getName().equals(s_name)){
                            target = s;
                            break;
                        }
                    }
        
                    if(target == null){
                        try{
                            send_resp("Store not found",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }
                    else{
                    boolean flag=target.set_Available(p_name, amount);
                    if(flag){
                    try{
                        send_resp("Stock updated",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}}
                    else{
                        try{
                            send_resp("Impossible to update stock like that",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}}
                    }
        
                }
                catch(Exception e){
                    e.printStackTrace();
                    try{
                        send_resp("error updating stock",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                }
        
            }
            else if (command.equals("totalSales")){

                String store_name = req.getData(0);
                String pname=req.getData(1);
                
                store target = null;

                for (store s : stores){
                    if (s.getName().equals(store_name)){
                        target = s;
                        break;
                    }
                }

                if (target == null){
                    try{
                        send_resp("Store does not exist",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}            
                }
                else{
                    int sales=target.total_sales(pname);
                    try{
                        send_resp("product "+pname+" total sales are: "+sales,code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                }
            }
            else if (command.equals("rateStore")){

                String store_name = req.getData(0);
                
                store target = null;

                for (store s : stores){
                    if (s.getName().equals(store_name)){
                        target = s;
                        break;
                    }
                }
                if (target == null){
                    try{
                        send_resp("Store does not exist",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}            
                }
                else{

                    try{
                        float a=Float.parseFloat(req.getData(1));
                        target.rate_store(a);
                        try{
                            send_resp("rating added",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }
                    catch (NumberFormatException eee) {
                        try{
                            send_resp("not valid rating",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }
                }
            }
            else if (command.equals("Buy")){

                try{
                    String s_name = req.getData(0);
                    String p_name = req.getData(1);
                    int amount = Integer.parseInt(req.getData(2));
                    
        
                    store target = null;
                    for(store s:stores){
                        if(s.getName().equals(s_name)){
                            target = s;
                            break;
                        }
                    }
        
                    if(target == null){
                        try{
                            send_resp("Store not found",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}
                    }
                    else{
                    boolean flag=target.Buy(p_name, amount);
                    if(flag){
                    try{
                        send_resp("Purchase successful",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}}
                    else {
                        try{
                            send_resp("Not enough in stock",code);
                        }
                        catch(IOException ee){ee.printStackTrace();}}
                    }
                
                
                
                
                
                    }    
                catch(Exception e){
                    e.printStackTrace();
                    try{
                        send_resp("error buying product",code);
                    }
                    catch(IOException ee){ee.printStackTrace();}
                }
            }
            else if (command.equals("allStores")) {
                try {
                    req.setStores(stores);
                    Socket reducerSocket=new Socket("localhost",reducer_port);
                    ObjectOutputStream out2 = new ObjectOutputStream(reducerSocket.getOutputStream());
                    out2.writeObject(req);
                    out2.flush();                
                } 
                catch (IOException e) {
                    e.printStackTrace();

                }
            }
            
            else if(command.equals("Search")){

                try{
                    
                    String price = req.getData(0);
                    float stars_low = Float.parseFloat(req.getData(1));
                    float stars_high= Float.parseFloat(req.getData(2));

                    int i=3;
                    ArrayList<String> cats=new ArrayList<>();
                    while(i<req.getSize()){
                        cats.add(req.getData(i));
                        i++;
                    }

                    if(cats.isEmpty()){cats.add("any");}

                    ArrayList<store> filtered = new ArrayList<store>();

                    for(store s : stores){

                        if((price.equals(s.get_price())|| price.equals("any")) && s.get_stars()<=stars_high && s.get_stars()>=stars_low && (cats.contains(s.get_cat())|| cats.get(0).equals("any"))){
                            filtered.add(s);
                        }                    
                    }

                    try {
                        req.setStores(filtered);
                        Socket reducerSocket=new Socket("localhost",reducer_port);
                        ObjectOutputStream out2 = new ObjectOutputStream(reducerSocket.getOutputStream());
                        out2.writeObject(req);
                        out2.flush();                
                    } 
                    catch (IOException e) {
                        e.printStackTrace();
        
                    }
                }
                catch(Exception e){}

            }
    

            


    

        }


    }




}
