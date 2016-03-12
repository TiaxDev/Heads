
package multiplayer;

import headballv2.Ball;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.*;

/*
 @author Tiax
 DA IMPLEMENTARE
*/
public class Client implements Runnable{
     
    private final int DEFAULTPORT = 8880;
    
    Socket Client;
    int Port;
    
    DataInputStream dataIn;
    DataOutputStream dataOut;
    
    public Client(){
        this.Client = null;
        this.Port = DEFAULTPORT;
    }
    
    public Client(int Port){
        this.Client = null;
        this.Port = Port;
    }
    
    private Socket connectTo(String Server){
        System.out.println("[CLIENT] Tentativo di connessione a: " + Server);
        try {
            Client = new Socket(InetAddress.getByName(Server), this.Port);
            System.out.println("[CLIENT] Connessione effettuata con " + this.Client.getInetAddress().toString() + ":" + this.Port);
            dataIn = new DataInputStream(Client.getInputStream());
            dataOut = new DataOutputStream(Client.getOutputStream());
        } catch (UnknownHostException ex) {
            System.err.println("[CLIENT] Errore, host non riconosciuto");
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        
        return this.Client;
    }
    
    //METODI GENERICI PER TEST CONNESSIONE//
    public DataInputStream getData(){
        return dataIn;
    }
    
    public void putData(Double Generic){
        try {
            dataOut.writeDouble(Generic);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }
    
    private void disconnectClient(){
        try {
            this.Client.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    @Override
    public void run(){
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        //BufferedReader s = new BufferedReader(new InputStreamReader(this.dataIn));
        System.out.println("[CLIENT]>");
        System.out.println("[CLIENT]> Indirizzo server a cui connettersi: ");
        try {
            this.connectTo(r.readLine());
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }

        while(!Client.isClosed()){
            
        }
        
        
        System.err.println("[CLIENT]> Disconnected from server");
    }
}
