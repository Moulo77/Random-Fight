/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

import java.util.ArrayList;
import javafx.scene.image.ImageView;

/**
 *
 * @author ewen
 */
public class Player {
    private final String name;
    private double posX;
    private double posY;
    private int speed;
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
    
    public void setSpeed(int aSpeed){
        speed = aSpeed;
    }
    
    public void setSkin(ImageView aSkin){
        skin=aSkin;
        skin.setX(posX);
    }
    
    public void calculatePosition(){
        posX += speed;
        if(posX >= 880){
            posX=880;
        } else if(posX<=0){
            posX=0;
        }
    }
    
    public void draw(){
        calculatePosition();
        skin.setX(posX);
        skin.setY(posY);
    }
}

