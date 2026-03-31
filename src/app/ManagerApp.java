package app;

import model.*;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.lang.ClassNotFoundException;
import com.google.gson.*;


class ManagerApp{

    Scanner sc;
    Socket master;
    ObjectOutputStream out ;
	ObjectInputStream in ;
    int master_port;

    public static void main(String args[]){
        if(args.length<1){               
            System.out.println("No system input, restart.");
            System.exit(0);
        }
        int master_port=Integer.parseInt(args[0]);
        ManagerApp manager=new ManagerApp(master_port);
        manager.begin();
        

    }


    public ManagerApp(int master_port){
        sc = new Scanner(System.in);
        this.master_port=master_port;
        try{
            master=new Socket("localhost",master_port);
            out = new ObjectOutputStream(master.getOutputStream());
            in = new ObjectInputStream(master.getInputStream());
        }
        catch(IOException e){
            System.out.println("Could not initialize app, Restart.");
            System.exit(0);
        }
    }


    public void begin(){

        int choice=0;
        

        while(choice!=6){

            System.out.println("1. Add Store.");
            System.out.println("2. Add/remove available product.");
            System.out.println("3. Add new product.");
            System.out.println("4. Remove existing product.");
            System.out.println("5. Total sales for product.");
            System.out.println("6. Exit.");
            System.out.print("Your choice...: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }
        
            switch (choice) {
                case 1:
                    addStore();
                    break;
                case 2:
                    updateAvailable();
                    break;
                case 3:
                    addProduct();
                    break;
                case 4:
                    removeProduct();
                    break;
                case 5:
                    totalSales();
                    break;

                case 6:
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
            try{
            master=new Socket("localhost",master_port);
            out = new ObjectOutputStream(master.getOutputStream());
            in = new ObjectInputStream(master.getInputStream());
            }
            catch(IOException e){}

        }
        sc.close();
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (master != null) master.close();
            
            
        } 
        catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void addStore(){
        message req= new message("addStore");
        System.out.println("Enter JSON path for store: ");
        String json = sc.nextLine();
        try{
            String storeData = new String(Files.readAllBytes(Paths.get(json)),StandardCharsets.UTF_8);
            req.addData(storeData);

            communicate(req);
        }
        catch(IOException e){
            System.out.println("Invalid file path.");
        }

        
    }

    public void updateAvailable(){
        message req= new message("updateAvailable");
        System.out.println("Give store name.");
        String sname = sc.nextLine();
        req.addData(sname);
        System.out.print("Give product name.");
        String pname = sc.nextLine();
        req.addData(pname);
        System.out.print("Give the amount you want to change.");
        String amount = sc.nextLine();
        req.addData(amount);

        communicate(req);

    }

    public void addProduct(){
        message req= new message("updateProduct");
        System.out.println("Give store name.");
        String sname = sc.nextLine();
        req.addData(sname);
        req.addData("add");
        System.out.println("Give path to product file.");
        String pjson = sc.nextLine();
        try{
            String productData = new String(Files.readAllBytes(Paths.get(pjson)),StandardCharsets.UTF_8);
            req.addData(productData);

            communicate(req);
        }
        catch(IOException e){
            System.out.println("Invalid file path.");
        }

        

    }

    public void removeProduct(){
        message req= new message("updateProduct");
        System.out.println("Give store name.");
        String sname = sc.nextLine();
        req.addData(sname);       
        req.addData("remove");
        System.out.println("Give product name.");
        String pname = sc.nextLine();
        req.addData(pname);

        communicate(req);

    }

    public void totalSales(){
        message req= new message("totalSales");
        System.out.println("Give store name.");
        String sname = sc.nextLine();
        req.addData(sname);  
        System.out.println("Give product name.");
        String pname = sc.nextLine();
        req.addData(pname);

        communicate(req);

    }


    public void communicate(message obj){
        try{
            out.writeObject(obj);
            out.flush();
            try{
                message res = (message) in.readObject();
                System.out.println(res.toString());
            }
            catch(ClassNotFoundException e){
                System.out.println("Request sent but responce not found.");
            }
            out.close();
            in.close();
        }
        catch(IOException e){
            System.out.println("Unable to communicate with server.");
        }
    }


}