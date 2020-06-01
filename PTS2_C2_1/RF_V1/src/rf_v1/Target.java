/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

import javafx.scene.Parent;

/**
 *
 * @author Palès
 */
public class Target extends Parent{
    private int points;
    private int lifePoints;
    private double posX;
    private double posY;

    public Target(int points, int lifePoints, double posX, double posY) {
        this.points = points;
        this.lifePoints = lifePoints;
        this.posX = posX;
        this.posY = posY;
    }

    public int getPoints() {
        return points;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public double getPosX() {
        return posX;
    }
    
}
