
package headballv2;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 @author Tiax
*/
public class Ball{
    
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public final Rectangle2D.Double Traversa1 = new Rectangle2D.Double(0, 390, 90, 15);
    public final Rectangle2D.Double Traversa2 = new Rectangle2D.Double(SCREEN_SIZE.width - 90, 390, 90, 15);
    
    private double FRICTION = 0.02;
    private final double ATTRICT = 500;
    private final Sprite Sprite = new Sprite("ball.png");
    
    private double x;
    private double y;
    private double w;
    private double h;
    
    private double Vx;
    private double Vy;
    private double Mass;
    private double Gravity;
    private double Angle;
    
    private boolean State;

    public Ball(double x, double y, double w, double h, double Vx, double Vy, double Mass, double Gravity, double Angle, boolean State) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.Vx = Vx;
        this.Vy = Vy;
        this.Mass = Mass;
        this.Gravity = Gravity;
        this.Angle = Angle;
        this.State = State;
    }
    
    public Ball(double x, double y, double Angle){
        this.x = x;
        this.y = y;
        this.Angle = Angle;
    }

    public Ball(){
        this.x = 600;
        this.y = 100;
        this.w = 50;
        this.h = 50;
        this.Vx = -4.5;
        this.Vy = 0;
        this.Mass = 0.5;
        this.Gravity = 0.2;
        this.Angle = 0;
        this.FRICTION = 0.05;
        this.State = true;
    }
    
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }
    
    public double getCenterX(){
        return this.x + this.w/2;
    }
    
    public double getCenterY(){
        return this.y + this.h/2;
    }
    
    public double getAngle(){
        return this.Angle;
    }

    public double getVx() {
        return Vx;
    }

    public void setVx(double Vx) {
        this.Vx = Vx;
    }

    public double getVy() {
        return Vy;
    }

    public void setVy(double Vy) {
        this.Vy = Vy;
    }

    public double getGravity() {
        return Gravity;
    }

    public void setGravity(double Gravity) {
        this.Gravity = Gravity;
    }

    public Sprite getTile() {
        return this.Sprite;
    }
    
    public void reset(){
        this.x = SCREEN_SIZE.width/2 - this.w/2;
        this.y = 100;
        this.Vx = 0;
        this.Vy = 0;
        this.Gravity = 0.2;
    }
    
    public void moveBall(ArrayList <Player> Players){
        if(this.State) {
            this.Vy += this.Gravity;
            this.y += this.Vy;
            this.x += this.Vx;
            this.Angle += this.Vx;
            
            if(this.y + this.h*3.2 > SCREEN_SIZE.getHeight()){ //Rimbalzo (Altezza aumentata perchè il campo è in assonometria)
                this.y = SCREEN_SIZE.getHeight() - this.h*3.2;
                this.Vy = -Math.abs(this.Vy);
                this.Gravity += FRICTION;
                
            }
            
            if(this.x < 0 || (this.x + this.w) > (SCREEN_SIZE.getWidth()))
                this.Vx = -this.Vx;
            
            this.Vx -= (this.Vx/ATTRICT);
            
            
            
            for (Player Player1 : Players) {
                gestioneCollisione(Player1);
            }
            
            if((this.x < Traversa1.getMaxX()) && (Traversa1.y < this.y + this.h && Traversa1.getMaxY() > this.y)){ //Traversa1
                this.Vy = -this.Vy;
                this.Vx = -this.Vx;
                if(Math.abs(this.Vx) < 1)
                    this.Vx *= 3;
            }
            else if((this.x + this.w > Traversa2.x) && (Traversa2.y < this.y + this.h && Traversa2.getMaxY() > this.y)){ //Traversa2
                this.Vy = -this.Vy;
                this.Vx = -this.Vx;
                if(Math.abs(this.Vx) < 1)
                    this.Vx *= 3;
            }
        }
    }
    
    public void gestioneCollisione(Player b){
        
        double xDist, yDist;
        xDist = this.getCenterX() - b.getCenterX();
        yDist = this.getCenterY() - (b.getCenterY() - 25);
        double distQuadro = (xDist*xDist + yDist*yDist);
        if(distQuadro <= (this.w/2 + b.getW()/2) * (this.w/2 + b.getW()/2)){
            double xVel = b.getVx() - this.Vx;
            double yVel = b.getVy() - this.Vy;
            double dotProd = xDist*xVel + yDist*yVel;
            if(dotProd > 0){
                double Scala = dotProd / distQuadro;
                double xCollision = xDist * Scala;
                double yCollision = yDist * Scala;
                double combinedMass = this.Mass + b.getMass();
                double collisionWeightA =  this.Mass / combinedMass;
                this.Vx += collisionWeightA * xCollision;
                this.Vy += collisionWeightA * yCollision;
            }
        }
    }
    
    public void writeObj(ObjectOutputStream Stream){
        try {
            Stream.writeDouble(this.x);
            Stream.writeDouble(this.y);
            Stream.writeDouble(this.h);
            Stream.writeDouble(this.w);
            Stream.writeDouble(this.Vx);
            Stream.writeDouble(this.Vy);
            Stream.writeDouble(this.Angle);
            Stream.writeDouble(this.Gravity);
            Stream.writeDouble(this.Mass);
            Stream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void readObj(ObjectInputStream Stream){
        try {
            this.x = Stream.readDouble();
            this.y = Stream.readDouble();
            this.h = Stream.readDouble();
            this.w = Stream.readDouble();
            this.Vx = Stream.readDouble();
            this.Vy = Stream.readDouble();
            this.Angle = Stream.readDouble();
            this.Gravity = Stream.readDouble();
            this.Mass = Stream.readDouble();
        } catch (IOException ex) {
            Logger.getLogger(Ball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
