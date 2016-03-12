
package headballv2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    
    /*ISTANZE OGGETTI*/
    Ball b;
    ArrayList <Player> p = new ArrayList <>();
    int[] POINTS = new int[2];
    int i, t = 0;
    boolean Goal = false;
    
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
        POINTS[0] = 0;
        POINTS[1] = 0;
        System.out.println("done");
    }

    public Sprite getStadium() {
        return Stadium;
    }
    
    public Sprite getGUI() {
        return GUI;
    }
    
    public Ball getBall(){
        return b;
    }
    
    public void BallMovement(){
        b.moveBall(p);

        /*TEMP PER GOAL*/
        if(!Goal){
            if((b.getX() + b.getW() < b.Traversa1.x + b.Traversa1.width) && (b.getY() > b.Traversa1.getMaxY())){ //Goal Destra
                Goal = true;
                POINTS[0] ++;
            }
            else if((b.getX() > b.Traversa2.x) && (b.getY() > b.Traversa2.getMaxY())){ //Goal sinistra
                Goal = true;
                POINTS[1] ++;
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
    
    //Implementare PG2
    public Player getPG(int i){
        if(i < 1)
            return p.get(i);
        return null;

    }

    public void PGMovement(){
        p.get(0).movePlayer();
    }

    public int getPOINTS(int i) {
        return POINTS[i];
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
