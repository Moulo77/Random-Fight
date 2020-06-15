/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v2;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

/**
 *
 * @author Palès
 */
public class Target extends Parent{
    private int points;
    private int lifePoints;
    private double posX;
    private double posY;
    private ImageView skin;
    private double speed;

    public Target(ImageView skin, int points, int lifePoints, double posX, double posY) {
        this.points = points;
        this.lifePoints = lifePoints;
        this.posX = posX;
        this.posY = posY;
        this.skin = skin;
    }
    
    public double getX(){
        return posX;
    }
    
    public double getY(){
        return posY;
    }
    
    public void setX(double aX){
        posX=aX;
    }

    public int getPoints() {
        return points;
    }
    
    public void setLife(int aLife){
        lifePoints=aLife;
    }
    
    public void setPoints(int points){
        this.points=points;
    }
    
    public void setSkin(ImageView aSkin){
        skin=aSkin;
        skin.setX(posX);
    }
    
    public ImageView getSkin(){
        return skin;
    }
    
    public int getLifePoints() {
        return lifePoints;
    }
    
    public void hit(int damage){
        lifePoints-=damage;
    }
    
    public boolean isAlive(){
        boolean alive = true;
        if(lifePoints<=0){
            alive=false;
        }
        
        return alive;
    }
    
    public void setSpeed(int aSpeed){
        speed = aSpeed;
    }

    public double getPosX() {
        return posX;
    }
    
    public void calculatePosition(){
        posX += speed/4;
        if(posX >= 730){
            speed=-1;
        } else if(posX<=0){
            posX=1;
        }
    }
    
    public void draw(){
        calculatePosition();
        skin.setX(posX);
        skin.setY(posY);
    }
    
}
