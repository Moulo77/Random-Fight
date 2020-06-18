/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v3;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

/**
 *
 * @author ewen
 */
public class Target extends Parent{
    private int points;
    private int lifePoints;
    private TypeTarget typeTarget;
    private double posX;
    private double posY;
    private ImageView skin;
    private double speed;

    public Target(TypeTarget aTypeTarget, ImageView aSkin, int aPoints, int aLifePoints, double aPosX, double aPosY){
        
        this.points = aPoints;
        this.lifePoints = aLifePoints;
        this.posX = aPosX;
        this.posY = aPosY;
        this.skin = aSkin;
        this.typeTarget = aTypeTarget;
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
    
    public void setY(double aY){
        posY=aY;
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
    
    public void setTypeTarget(TypeTarget type){
        this.typeTarget = type;
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
    
    public double intersect(double aPosX, double aPosY){
        double posIntersect = aPosX;
        if(aPosX >= (getSkin().getX() - getSkin().getFitWidth()/2)
                && aPosX <= (getSkin().getX() + getSkin().getFitWidth()/2)
                && aPosY >=200){
            if(aPosX >= (getSkin().getX()-getSkin().getFitWidth()/2)
                    && aPosX <= getSkin().getX()){
                
                posIntersect = getSkin().getX()-getSkin().getFitWidth()/2;
                
            } else if(aPosX <= (getSkin().getX()+getSkin().getFitWidth()/2)
                    && aPosX >= getSkin().getX()){
                
                posIntersect = getSkin().getX()+getSkin().getFitWidth()/2;
                        
            }
        }
        return posIntersect;
    }
    
    public boolean hitLeft(double aPosX){
        return aPosX >= getSkin().getX()-(getSkin().getFitWidth())
            && aPosX <= getSkin().getX()+(getSkin().getFitWidth());
    }
    
    public boolean hitRight(double aPosX){
        return aPosX <= getSkin().getX()+(getSkin().getFitWidth())
            && aPosX >= getSkin().getX()-(getSkin().getFitWidth());
    }
    
    public void calculatePosition(){
        posX += speed/4;
        if(posX >= 730){
            speed=-1;
        } else if(posX<=0){
            speed=1;
        }
    }
    
    public void draw(){
        calculatePosition();
        skin.setX(posX);
        skin.setY(posY);
    }
    
}