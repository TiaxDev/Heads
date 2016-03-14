
package headballv2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import multiplayer.*;

/*
 @author Tiax
*/
public class MotionComponent implements KeyListener{
    
    private final int PKICK = 2;
    private final int PJUMP = 3;
    
    private final double MAX_SPEED = 4;
    private final int AFTER_GOAL_DELAY = 0;
    private final Set <Integer> Pulsanti = new HashSet <>(); /* ANDAVA BENE ANCHE ARRAYLIST MA CON HASHSET
                                                                NON HO BISOGNO DI CONTROLLARE TUTTO L'ARRAY 
                                                                PER VEDERE SE LA KEY è E' GIA' PRESENTE*/
    
    /*SPRITES BG*/
    Sprite Stadium;
    Sprite GUI;
    Sprite[] Splash;
    
    /*ISTANZE OGGETTI*/
    Thread th;
    private Ball b;
    private ArrayList <Player> p = new ArrayList <>();
    private int i, t = 0;
    private boolean Goal = false;
    private boolean readEnter = false;
    private boolean isServer = false;
    private String multiState = " ";
    private Client Client;
    private Server Server;
    
    public MotionComponent(){
        initComponents();
    }
    
    private void initComponents(){
        System.out.println("init_components...");
        this.b = new Ball();
        this.p.add(new Player());
        this.p.add(new Player());
        this.Stadium = new Sprite("stadium" + Integer.toString((int)Math.random() & 1) + ".png");
        this.GUI = new Sprite("doors.png");
        this.Splash = new Sprite[2];
        this.Splash[0] = new Sprite("splash.png");
        this.Splash[1] = new Sprite("splash2.png");
        System.out.println("done");
    }

    //TRUE: SERVER_MODE ON
    public void initMultiplayer(boolean Mode){
        if(Mode){
            /*SERVERMODE*/
            this.multiState = "Aspetto connessioni...";
            this.Server = new Server();
            isServer = true;
            Server.setState(multiState);
            th = new Thread(this.Server, "Server");
            th.start();
        }
        else{
            /*CLIENTMODE*/
            this.multiState = "Ricerca server...";
            this.Client = new Client();
            Client.setState(multiState);
            th = new Thread(this.Client, "Client");
            th.start();
        }
    }
    
    public Sprite getSplashItem(int Index){
        if(Index <= Splash.length)
            return Splash[Index];
        else
            return Splash[0];
    }

    public Sprite getStadium() {
        return Stadium;
    }
    
    public Sprite getGUI() {
        return GUI;
    }
    
    public Ball getBall(){
        if(isServer)
            return b;
        else
            return Client.getSocketBall();
    }

    public String getMultiState() {
        if(isServer){
            return Server.getState();
        }
        else{
            return Client.getState();
        }
    }

    public void setMultiState(String multiState) {
        this.multiState = multiState;
    }
    
    public void BallMovement(){
        b.moveBall(p);
        if(isServer)
            b.writeObj(Server.getObjOut());
        /*TEMP PER GOAL*/
        if(!Goal){
            if((b.getX() + b.getW() < b.Traversa1.x + b.Traversa1.width) && (b.getY() > b.Traversa1.getMaxY())){ //Goal Destra
                Goal = true;
                p.get(0).setPoints(p.get(0).getPoints() + 1);
            }
            else if((b.getX() > b.Traversa2.x) && (b.getY() > b.Traversa2.getMaxY())){ //Goal sinistra
                Goal = true;
                p.get(1).setPoints(p.get(1).getPoints() + 1);
            }
        }
        else{
            t++;
            if(t > AFTER_GOAL_DELAY){ //Time delay dopo Goal
                t = 0;
                b.reset();
                Goal = false;
            }
        }
    }
    
    public Player getPG(int Index){
        if(Index == 0)
            return p.get(Index);
        
        if(isServer){
            p.set(Index, Server.getSocketPG());
            return p.get(Index);
        }  
        else{
            p.set(Index, Client.getSocketPG());
            return p.get(Index);
        }

    }
    
    public void setPg(int Index, Player p1){
        if(Index == 0)
            this.p.set(Index, p1);
        if(Index == 1)
            this.p.set(Index, p1);
    }

    public void PGMovement(){
        p.get(0).movePlayer();
        if(isServer)
            p.get(0).writeObj(Server.getObjOut());
        else
            p.get(0).writeObj(Client.getObjOut());
    }
    
    public boolean isReadEnter() {
        return readEnter;
    }
    
    @Override
    public synchronized void keyPressed(KeyEvent e) {
        //System.out.println("PRESSED");

        Pulsanti.add(e.getKeyCode());
        keyJustMove();
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        //System.out.println("RELEASED");
        
        Pulsanti.remove(e.getKeyCode());
        p.get(0).getTile().setSprite((i & 1));
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("TYPED");
        /*INUTILIZZATO*/
    }
    
    public void keyJustMove(){
        if(Pulsanti.contains(KeyEvent.VK_ENTER))
            readEnter = true;
            
        if(Pulsanti.contains(KeyEvent.VK_UP) && !(p.get(0).isJump())){
            p.get(0).setJump(true);
            p.get(0).setVy(-8);
        }
        
        if(Pulsanti.contains(KeyEvent.VK_SHIFT)){
            p.get(0).Kick(b);
            p.get(0).getTile().setSprite(PKICK);
        }
        else{
            if(!(p.get(0).isJump())){
                p.get(0).getTile().setSprite((i & 1));
            }
            else
                p.get(0).getTile().setSprite(PJUMP);
        }
            
        if(p.get(0).getVx() < MAX_SPEED){

            if (Pulsanti.contains(KeyEvent.VK_LEFT)){
                p.get(0).decX(2.5);
                i++;
            }

            if (Pulsanti.contains(KeyEvent.VK_RIGHT)){
                p.get(0).incX(2.5);
                i++;
            }
        }
    }   
}
