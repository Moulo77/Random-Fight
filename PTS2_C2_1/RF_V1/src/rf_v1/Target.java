/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

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

    public Target(ImageView skin, int points, int lifePoints, double posX, double posY) {
        this.points = points;
        this.lifePoints = lifePoints;
        this.posX = posX;
        this.posY = posY;
        this.skin = skin;
    }

    public int getPoints() {
        return points;
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
            points =0;
        }
        
        return alive;
    }

    public double getPosX() {
        return posX;
    }
    
    public void draw(){
        skin.setX(posX);
        skin.setY(posY);
    }
    
}
