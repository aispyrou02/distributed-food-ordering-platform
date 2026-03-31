
package app;

import model.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import com.google.gson.*;


class ClientApp{
    
    Scanner sc;
    Socket master;
    ObjectOutputStream out ;
	ObjectInputStream in ;
    ArrayList<store> currentStores=new ArrayList<store>();
    int master_port;

    public static void main(String args[]){
        if(args.length<1){               
            System.out.println("No system input, restart.");
            System.exit(0);
        }
        int master_port=Integer.parseInt(args[0]);
        ClientApp client=new ClientApp(master_port);
        client.begin();
        

    }


    public ClientApp(int master_port){
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

        printStores();

        int choice=0;
        

        while(choice!=5){

            System.out.println("1. Refresh.");
            System.out.println("2. Search.");
            System.out.println("3. Buy.");
            System.out.println("4. Rate Store.");
            System.out.println("5. Exit.");
            System.out.print("Your choice...: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }
        
            switch (choice) {
                case 1:
                    printStores();
                    break;
                case 2:
                    Search();
                    break;
                case 3:
                    Buy();
                    break;
                case 4:
                    Rate();
                    break;
                case 5:
                    System.out.println("Goodbye.");
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }

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




    public void printStores(){
        
        System.out.println("\n");

        ArrayList<store> AllStores=new ArrayList<store>();

        message req=new message("allStores");
        message resp=communicate(req);
        AllStores=new ArrayList<>(resp.getStores());
        for(store s:AllStores){
            System.out.println(s.toString());
        }


    
    }
    
    public void Search(){

        
        int choice;
        String msg="Y";
        String input;
        message req = new message("Search"); 
        
        currentStores.clear();

        System.out.println("Wanna filter on price? Y or N");
        msg = sc.nextLine();
        if(msg.equals("Y") || msg.equals("y")){
            System.out.println("give price range, $ , $$ , $$$");
            input = sc.nextLine();
            req.addData(input);
        }
        else{
            req.addData("any");
        }
        System.out.println("Wanna filter on Stars? Y or N");
        msg = sc.nextLine();
        if(msg.equals("Y") || msg.equals("y")){
            System.out.println("give low stars range.");
            input = sc.nextLine();
            req.addData(input);
            System.out.println("give high stars range.");
            input = sc.nextLine();
            req.addData(input);
        }
        else{
            req.addData("0");
            req.addData("5");        
        }

        System.out.println("Wanna add food category? Y or N");
        msg = sc.nextLine();


        while (msg.equals("Y") || msg.equals("y")) {

            
            System.out.println("give category");
            input=sc.nextLine();

            req.addData(input);

            System.out.println("want to add more categories? Y or N ");
            msg = sc.nextLine();

        }

              
        message resp=communicate(req);
        currentStores = new ArrayList<>(resp.getStores());
        for(store s:currentStores){
            System.out.println(s.toString());
        }


    }

    public void Buy(){
        message req = new message("Buy");

        System.out.println("give store name");
        String sname = sc.nextLine();
        System.out.println("give product name");
        String pname = sc.nextLine();
        System.out.println("give amount");
        String amount = sc.nextLine();

        req.addData(sname);
        req.addData(pname);
        req.addData(amount);

        message resp=communicate(req);
        System.out.println(resp.toString());

        printStores();

    }


    public void Rate(){
        message req= new message("rateStore");
        System.out.println("Give store name.");
        String sname = sc.nextLine();
        req.addData(sname);  
        System.out.println("Give rating.");
        String rating = sc.nextLine();
        req.addData(rating);
        
        message resp=communicate(req);
        System.err.println(resp);

        printStores();
    }




    public message communicate(message obj){

        message res=new message("empty");
        try{
            out.writeObject(obj);
            out.flush();
            try{

                res = (message) in.readObject();
                try{
                    master=new Socket("localhost",master_port);
                    out = new ObjectOutputStream(master.getOutputStream());
                    in = new ObjectInputStream(master.getInputStream());
                    }
                    catch(IOException e){}
                return res;
            }
            catch(ClassNotFoundException e){
                System.out.println("Request sent but response not found.");
                try{
                    master=new Socket("localhost",master_port);
                    out = new ObjectOutputStream(master.getOutputStream());
                    in = new ObjectInputStream(master.getInputStream());
                    }
                    catch(IOException ee){}
            }

        }
        catch(IOException e){
            System.out.println("Unable to communicate with server.");
        }
        


        return res;

        
    }

    
}


