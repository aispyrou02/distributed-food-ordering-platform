package network;

import model.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.ClassNotFoundException;
import com.google.gson.*;




public class master{

    private int port,code=0;
    private ServerSocket master_server;
    private List<Integer> workers=new ArrayList<>();
    private int numberOfNodes=0;
    private Map<Integer,ObjectOutputStream> requests = new HashMap<>();
    

    
    public static void main(String args[]){
        if(args.length<2){               
            System.out.println("Invalid system input, restart.");
            System.exit(0);
        }
        else{
            int masterPort = Integer.parseInt(args[0]);
            List<String> workerlist=Arrays.asList(args).subList(1, args.length);
            master m=new master(masterPort,workerlist);
            m.begin();
            try{m.close();}
            catch(IOException e){e.printStackTrace();}
        }
    }




    public master(int masterPort,List<String> workerlist){
        port=masterPort;
        try{
            master_server=new ServerSocket(port);

            for(String s:workerlist){              
                workers.add(Integer.parseInt(s));
            }
            numberOfNodes=workers.size();

        }
        catch(IOException e){
            System.out.println("Could not initialize master or worker connection, restart.");
            System.exit(0);
        }

    }




    public void begin(){

        while(true){
            try{
                Socket manager = master_server.accept();
                req_handler request=new req_handler(manager);
                Thread workerThread = new Thread(request);
                workerThread.start();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }



    }

    public int simpleHash(String storeName) {
        return Math.abs(storeName.hashCode());  
    }
    
    public void close() throws IOException {

        master_server.close();
    }


    class req_handler implements Runnable {

        Socket client;
        ObjectOutputStream out;
        ObjectInputStream in;


        public req_handler(Socket client){
            this.client=client;
        }
        
        synchronized int add_request(){
            requests.put(code, out);
            code++;
            return (code-1);
        }

        synchronized void send_response(message obj,int c) throws  IOException {
            if (requests.containsKey(c)) {
                ObjectOutputStream resp_out = requests.get(c);
                resp_out.writeObject(obj);
                resp_out.flush();
                resp_out.close();
                requests.remove(c);
            }
        }


    
        public void run(){

            message req;
            message resp=new message("empty"); 
            resp.set_resp(true);
            resp.set_response("invalid data");

            try{
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                try{


                    
                    req = (message) in.readObject();
                    String command = req.getCommand();


                 
                    if(command.equals("addStore")){

                        try{
                            
                            String json =req.getData(0);
                            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        
                            String SName = jsonObject.get("StoreName").getAsString();
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }                           

                    }
                    else if (command.equals("updateAvailable")){
                        try{
                            String SName =req.getData(0);
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }
                    }
                    else if (command.equals("updateProduct")){
                        try{
                            String SName =req.getData(0);
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }
                    }
                    else if (command.equals("totalSales")){
                        try{
                            String SName =req.getData(0);
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }
                    }
                    else if (command.equals("Search")){
                        communicate_all(req);
                    }
                    else if (command.equals("allStores")){
                        communicate_all(req);
                    }
                    else if (command.equals("Buy")){
                        try{
                            String SName =req.getData(0);
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }
                    }
                    else if (command.equals("rateStore")){
                        try{
                            String SName =req.getData(0);
    
                            int node=simpleHash(SName)%numberOfNodes;

                            communicate(node, req);

                        }
                        catch (ArrayIndexOutOfBoundsException e){
                            out.writeObject(resp);
                            out.flush();
                        }
                    }
                    else if (command.equals("red_resp")){
                        int resp_code=req.getCode();
                        send_response(req,resp_code);





                    }
                    else{
                        resp.set_response("no command");
                        out.writeObject(resp);
                        out.flush();
                    }


                    



                }
                catch (ClassNotFoundException e){
                    out.writeObject(resp);
                    out.flush();
                }


            }
            catch(IOException e){
                System.out.println("Connection error");
            }

        
        }

        void communicate_all(message obj){

            int cur_code;
            cur_code=add_request();
            obj.setCode(cur_code);

            for(int s:workers){
                try{
                    Socket curr_worker=new Socket("localhost",s);
                    ObjectOutputStream out2 = new ObjectOutputStream(curr_worker.getOutputStream());
                    out2.writeObject(obj);
                    out2.flush();
    
                }
                catch(IOException e){
                    System.out.println("Unable to communicate with server.");
                }
            }


        }

        void communicate(int node,message obj){

            int cur_code;
            


            try{
                Socket curr_worker=new Socket("localhost",workers.get(node));
                ObjectOutputStream out2 = new ObjectOutputStream(curr_worker.getOutputStream());
                out2.writeObject(obj);
                out2.flush();
                ObjectInputStream in2 = new ObjectInputStream(curr_worker.getInputStream());
                try{
                    message r = (message) in2.readObject();
                    out.writeObject(r);
                    out.flush();

                }
                catch(ClassNotFoundException e){e.printStackTrace();}
                curr_worker.close();
                out2.close();
                in2.close();
            }
            catch(IOException e){
                System.out.println("Unable to communicate with server.");
            }

        }
            





    }



}




