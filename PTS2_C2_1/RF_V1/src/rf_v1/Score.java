/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

/**
 *
 * @author mypc
 */
public class Score {
    private int points;
    
    public Score(){
        points = 0;
    }
    
    public void setScore(int aPoints){
        points=aPoints;
    }
    
    public int getScore(){
        return points;
    }
    
    public void addPoints(int points){
        this.points+=points;
    }
    
    public void resetPoints(){
        points = 0;
    }
}
