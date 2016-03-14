
package multiplayer;

import headballv2.Ball;
import headballv2.MotionComponent;
import headballv2.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 @author Tiax
 DA IMPLEMENTARE
*/
public class Client extends MotionComponent implements Runnable{
     
    private final int DEFAULTPORT = 8880;
    private final String DEBUGSERVER = "10.0.97.20";
    
    Socket Client;
    int Port;
    String State = " ";
    
    DataInputStream dataIn;
    DataOutputStream dataOut;
    ObjectInputStream objIn;
    ObjectOutputStream objOut;
    
    //LOCALI
    Ball B1 = null;
    Player PG = null;
    
    public Client(){
        this.Client = null;
        this.Port = DEFAULTPORT;
    }
    
    public Client(int Port){
        this.Client = null;
        this.Port = Port;
    }
    
    private Socket connectTo(String Server){
        boolean err;
        int conAttempt = 0;
        
        do{
            err = false;
            conAttempt++;
            System.out.println("[CLIENT] Tentativo di connessione a: " + Server);
            this.State = "Tentativo di connessione... (" + (conAttempt) + ")";
            try {
                Client = new Socket(InetAddress.getByName(Server), this.Port);
                System.out.println("[CLIENT] Connessione effettuata con " + this.Client.getInetAddress().toString() + ":" + this.Port);
                this.State = "Connessione stabilita!";
                dataIn = new DataInputStream(Client.getInputStream());
                dataOut = new DataOutputStream(Client.getOutputStream());
                objOut = new ObjectOutputStream(Client.getOutputStream());
                objIn = new ObjectInputStream(Client.getInputStream());
            } catch (UnknownHostException ex) {
                System.err.println("[CLIENT] Errore, host non riconosciuto");
                this.State = "Host non trovato, nuovo tentativo...";
                err = true;
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
                err = true;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(err && conAttempt < 10);
        
        if(conAttempt >= 10)
            this.State = "Timeout raggiunto.";
        
        return this.Client;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }
    
    private void disconnectClient(){
        try {
            this.Client.close();
            this.dataIn.close();
            this.dataOut.close();
            this.objIn.close();
            this.objOut.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public Ball getSocketBall() {
        return B1;
    }

    public Player getSocketPG() {
        return PG;
    }

    public ObjectOutputStream getObjOut() {
        return objOut;
    }
    
    @Override
    public void run(){
        connectTo(DEBUGSERVER);
        String sData = " ";
        //LOCALI
        B1 = new Ball();
        PG = new Player();
        
        this.State = "Aspetto Sinch...";
        
        try {
            while(!sData.equals("_SYN_")){
                sData = this.dataIn.readUTF();
            }
            
            this.dataOut.writeUTF("_SYN_");
            this.dataOut.flush();
            
            //READY
            this.State = "READY";
            while(Client.isConnected()){
                //Get delle risorse dal socket
                B1.readObj(objIn); 
                PG.readObj(objIn);
            }
            
            this.State = "Connessione persa";
            disconnectClient();
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
