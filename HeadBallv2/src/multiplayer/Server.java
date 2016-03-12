
package multiplayer;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 @author Tiax
 DA IMPLEMENTARE
*/
public class Server implements Runnable{
    
    private final int DEFAULTPORT = 8880;
    
    ServerSocket Server;
    Socket Client;
    int Port;
    
    DataInputStream dataIn;
    DataOutputStream dataOut;
    
    
    public Server(){
        this.Server = null;
        this.Client = null;
        this.Port = DEFAULTPORT;
    }
    
    public Server(int Port){
        this.Server = null;
        this.Client = null;
        this.Port = Port;
    }
    
    public Socket initServer(int Port){
        System.out.println("[SERVER] Creazione server, in ascolto su porta " + Port);
        try {
            Server = new ServerSocket(Port);
            Client = Server.accept();
            
            System.out.println("[SERVER] Connessione stabilita con client: " + Client.getInetAddress());
            dataIn = new DataInputStream(Client.getInputStream());
            dataOut = new DataOutputStream(Client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Client;
    }
    
    //METODI GENERICI PER TEST CONNESSIONE//
    public String getData(){
        return dataIn.toString();
    }
    
    public void putData(String Generic){
        try {
            dataOut.writeBytes(Generic);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopServer(){
        try {
            this.Server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("[SERVER]>");
            this.initServer(Port);
            for(int i = 0; i < 10; i++){
                this.dataOut.writeUTF(String.valueOf(i));
                this.dataOut.flush();
                //System.err.println("[SERVER]> Pause...");
                //this.dataOut.writeUTF("PAUSE_2000");
                Thread.sleep(1000);
                
            }
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        try {
//            this.dataOut.writeUTF("END");
//        } catch (IOException ex) {
//            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        boolean end = false;
        
        while(!end){
            try {
                System.out.println("[SERVER]>");
                String s = r.readLine();
                if(s.equals("end")){
                    end = true;
                    this.dataOut.writeUTF("END");
                }
                else
                    this.dataOut.writeUTF(s);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        System.err.println("[SERVER]> Closed Connection");
        this.stopServer();
    }
}
