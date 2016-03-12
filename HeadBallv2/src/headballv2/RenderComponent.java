
package headballv2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

/*
 @author Tiax
*/
public class RenderComponent extends JPanel implements Runnable{
    
    private final Dimension SCREEN_SIZE = new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    private final int DELAY = 8;
    private Thread RenderThread;
    private MotionComponent Motion;
    private long FPS;
    private boolean debugFPS;

    public RenderComponent(){
        initRenderComponent();
    }
    
    private void initRenderComponent(){
        //this.setBackground(Color.WHITE);
        System.out.println("init_rendercomponent");
        this.setDoubleBuffered(true);
        Motion = new MotionComponent();
        debugFPS = true;
        System.out.println("done");
        //CHARGED GRAPHICS
    }

    @Override
    public void addNotify() {
        super.addNotify();
        
        RenderThread = new Thread(this);
        RenderThread.start();
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        //FUNZIONI DISEGNO
        /*ANTIALIAS ON*/
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawBackground(g2);
        
        if(debugFPS)
            g2.drawString("FPS: " + FPS, 20, 20);
        
        drawBall(g2);

        g2.drawImage(Motion.getPG(0).getTile().getSprite(), null, (int)Motion.getPG(0).getX(), (int)Motion.getPG(0).getY());

        g2.drawImage(Motion.getGUI().getSprite(), 0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height, null); //Doors

        drawScore(g2);
        
        //g2.drawLine(683, 0, 683, 768);

        /*DEALLOCO RISORSE GRAFICHE*/
        g2.dispose();
    }
    
    public void drawBall(Graphics2D g2){
        /*SEZIONE PALLA (CON ROTAZIONE)*/
        AffineTransform Original = g2.getTransform();
        
        g2.rotate(Math.toRadians(Motion.getBall().getAngle()), (int)Motion.getBall().getX() + 25, (int)Motion.getBall().getY() + 25); //35 sostituire con BallWidth e BallHeight
        g2.drawImage(Motion.getBall().getTile().getSprite(), null, (int)Motion.getBall().getX(), (int)Motion.getBall().getY());
        g2.rotate(Math.toRadians(-Motion.getBall().getAngle()), (int)Motion.getBall().getX() + 25, (int)Motion.getBall().getY() + 25);
        
        g2.setTransform(Original); //Ripristino la transform originale
    }
    
    public void drawScore(Graphics2D g2){
        /*SEZIONE PUNTEGGIO*/
        Font currentFont = g2.getFont();
        Font thisFont = currentFont.deriveFont(Font.BOLD, currentFont.getSize() * 5F);
        Paint currentPaint = g2.getPaint();
        g2.setFont(thisFont);
        g2.setPaint(new GradientPaint(0 , 0, Color.ORANGE, 0, 120, Color.RED));
        //g2.drawString(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())%100 + " ", SCREEN_SIZE.width/2 - 44, 120);
        g2.drawString(Motion.getPOINTS(0) + " ", SCREEN_SIZE.width/2 - 150, 100);
        g2.drawString(Motion.getPOINTS(1) + " ", SCREEN_SIZE.width/2 + 100, 100);
        g2.setPaint(currentPaint);
        g2.setFont(currentFont);
    }
    
    public void drawBackground(Graphics2D g2){
        g2.drawImage(Motion.getStadium().getSprite(), 0, 0, SCREEN_SIZE.width, SCREEN_SIZE.height, null); //BackgroundImage
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        long frameTimer = 0;
        int frames = 0;

        beforeTime = System.currentTimeMillis();
        
        while (true) {
            
            //FUNZIONE MOVIMENTO QUI'
            Motion.BallMovement();
            Motion.PGMovement();
            repaint();
            
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;
            
            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            }catch (InterruptedException e) {
                System.out.println("Interrotto: " + e.getMessage());
            }
            
            beforeTime = System.currentTimeMillis();
            frames++;
            if((beforeTime - frameTimer) + sleep > 1000){
                frameTimer = beforeTime;
                FPS = frames;
                frames = 0;
            }
        }
    }
    
    public MotionComponent getMotionComponent(){
        return this.Motion;
    }
}

