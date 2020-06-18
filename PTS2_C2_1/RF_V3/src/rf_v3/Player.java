/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v3;

import javafx.scene.image.ImageView;

/**
 *
 * @author ewen
 */
public class Player {
    private final String name;
    private double posX;
    private double posY;
    private int speed=0;
    private int speedY=0;
    private int lifePoints;
    private ImageView skin;
    private int damage = 1;

    public Player(ImageView aSkin,int aLifePoints, double aPosX,double aPosY) {
        this.name = "jammy";
        posX = aPosX;
        posY=aPosY;
        skin = aSkin;
        speed=0;
        lifePoints=aLifePoints;
        draw();
    }
    
    public double getX(){
        return posX;
    }
    
    public double getY(){
        return posY;
    }
    
    public int getSpeed(){
        return speed;
    }
    
    public void setX(double aX){
        posX=aX;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public void setSpeed(int aSpeed){
        speed = aSpeed;
    }
    
    public void setSpeedY(int aSpeedY){
        speedY = aSpeedY;
    }
    
    public void setSkin(ImageView aSkin){
        skin=aSkin;
        skin.setX(posX);
    }
    
    public int getLife(){
        return lifePoints;
    }
    
    public void setLife(int aLife){
        lifePoints=aLife;
    }
    
    public ImageView getSkin(){
        return skin;
    }
    
    public double intersect(double aPosX, double aPosY){
        double posIntersect = aPosX;
        if(aPosX >= (getSkin().getX() - getSkin().getFitWidth()/2)
                && aPosX <= (getSkin().getX() + getSkin().getFitWidth()/2)
                && aPosY >=250){
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
    
    public void calculatePosition(){
        posX += speed;
        if(posX >= 730){
            posX=730;
        } else if(posX<=0){
            posX=0;
        }
        posY += speedY;
        if(posY <=50){
            speedY=1;
        } else if(posY >= 250){
            posY=250;
        }
    }
    
    public void draw(){
        calculatePosition();
        skin.setX(posX);
        skin.setY(posY);
    }
}
