
package multiplayer;

import headballv2.MotionComponent;
import headballv2.Player;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 @author Tiax
 
*/
public class Server extends MotionComponent implements Runnable{
    
    private final int DEFAULTPORT = 8880;
    
    ServerSocket Server;
    Socket Client;
    int Port;
    String State = " ";
    
    DataInputStream dataIn;
    DataOutputStream dataOut;
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    
    //LOCALI
    Player PG = null;
    
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
            this.State = "Connessione stabilita!";
            dataIn = new DataInputStream(Client.getInputStream());
            dataOut = new DataOutputStream(Client.getOutputStream());
            objOut = new ObjectOutputStream(Client.getOutputStream());
            objIn = new ObjectInputStream(Client.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Client;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }
    
    //METODI GENERICI PER TEST CONNESSIONE//
    
    private void stopServer(){
        try {
            this.Server.close();
            this.dataIn.close();
            this.dataOut.close();
            this.objIn.close();
            this.objOut.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Player getSocketPG() {
        return this.PG;
    }
    
    public ObjectOutputStream getObjOut() {
        return objOut;
    }

    @Override
    public void run() {
        initServer(this.DEFAULTPORT);
        String sData = " ";
        //LOCALI
        PG = new Player();
        
        this.State = "Invio Sinch...";
        try {
            this.dataOut.writeUTF("_SYN_");
            this.dataOut.flush();
            
            while(!sData.equals("_SYN_")){
                sData = this.dataIn.readUTF();
            }
            
            //READY
            this.State = "READY";
            while(Client.isConnected()){
                //Get risorse dal socket
                PG.readObj(objIn);
            }
            
            this.State = "Client disconnesso";
            stopServer();
                
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
