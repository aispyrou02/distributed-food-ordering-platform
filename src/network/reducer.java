package network;

import model.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import com.google.gson.*;







class reducer{

    private int port,number_of_workers,mport;
    private ServerSocket reducer_server;
    private Socket master_Socket;
    Map<Integer, ArrayList<ArrayList<store>>> reducer_requests = new HashMap<>();
    private ObjectOutputStream mout;

    public static void main(String args[]){
        try{
            int reducer_port = Integer.parseInt(args[0]);
            int master_port = Integer.parseInt(args[1]);
            int number= Integer.parseInt(args[2]);
            reducer reducer_exe=new reducer(reducer_port,master_port,number);
            reducer_exe.run();
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Could not initialize reducer, try again.");
            System.exit(0);
        }


    }

    public reducer(int port,int master_port,int number){
        this.mport=master_port;
        this.port=port;
        this.number_of_workers=number;
        try{
            reducer_server=new ServerSocket(port);
            master_Socket=new Socket("localhost",master_port);
            mout = new ObjectOutputStream(master_Socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("Could not initialize reducer, try again.");
            System.exit(0);
        }
    }




    public void run(){


        try{

            
            while(true){

                Socket worker_connection= reducer_server.accept();
                message_process process=new message_process(worker_connection);
                Thread reducerThread = new Thread(process);
                reducerThread.start();
                

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            
            try {
                reducer_server.close();
            } catch (IOException e) {e.printStackTrace();}
        }
    }

    class message_process implements Runnable{

        int code;
        Socket worker_Socket;
        message msg;
        ObjectOutputStream out;
        ObjectInputStream in;
        
        ArrayList<ArrayList<store>> big_temp;

        public message_process(Socket a){         
            this.worker_Socket=a;
                       
        }
        synchronized void send_master(message resp) throws IOException{
            mout.writeObject(resp);
            mout.flush();
            mout.close();
            master_Socket=new Socket("localhost",mport);
            mout = new ObjectOutputStream(master_Socket.getOutputStream());
        }
        
        synchronized boolean add_to_map(int code,ArrayList<store> temp){

            if (!reducer_requests.containsKey(code)) {
                reducer_requests.put(code, new ArrayList<>());
            }
            reducer_requests.get(code).add(temp);
            return (reducer_requests.get(code).size()>=number_of_workers);

        }

        synchronized ArrayList<ArrayList<store>> get_results(int code){
            ArrayList<ArrayList<store>> value =reducer_requests.get(code); 
            reducer_requests.remove(code);
            return value;
        }

        public void run(){
            try{
                out = new ObjectOutputStream(worker_Socket.getOutputStream());
                in = new ObjectInputStream(worker_Socket.getInputStream());
                try{
                    msg = (message) in.readObject();

                }
                catch (ClassNotFoundException e){e.printStackTrace();}

            
            
                code=msg.getCode();
                ArrayList<store> temp=new ArrayList<>(msg.getStores());
                boolean flag=add_to_map(code,temp);
                ArrayList<store> temp2=new ArrayList<store>();
                if(flag){
                    big_temp=get_results(code);
                    for (ArrayList<store> l:big_temp){
                        temp2.addAll(l);
                    }  
                    message resp=new message("red_resp");
                    resp.setStores(temp2);
                    
                    resp.setCode(code);
                    try{
                        send_master(resp);
                    }
                    catch(IOException e){e.printStackTrace();};
                                
                }
                else{}
            }
            catch(IOException e){e.printStackTrace();}
            finally {
                try {
                    if (worker_Socket != null) worker_Socket.close();
                } 
                catch (IOException e) {e.printStackTrace();}
            }        
        }
    }











}