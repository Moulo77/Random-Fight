/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v2;

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
    private ImageView skin;
    private int damage = 1;

    public Player(ImageView aSkin, double aPosX,double aPosY) {
        this.name = "jammy";
        posX = aPosX;
        posY=aPosY;
        skin = aSkin;
        speed=0;
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
    
    public ImageView getSkin(){
        return skin;
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

