/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Palès
 */
public class RF_V1 extends Application {
    Timer itsTimer;
    int vitesse;
    Timer gameTimer;
    int time =120;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        AnchorPane root = new AnchorPane();
        
        /*
        * Création du background
        */
        FileInputStream backgroundFile = new FileInputStream("src/RF_V1/images/background.png");
        Image background = new Image(backgroundFile);
        BackgroundImage background_img = new BackgroundImage(background,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG = new Background(background_img);
        
        
        
        root.setBackground(BG);
        
        // Création de la Scene
        Scene scene = new Scene(root, background.getWidth(), background.getHeight());
        
        /*
        * Création du joueur 
        */
        FileInputStream file = new FileInputStream("src/RF_V1/images/jamyCour.png");
        Image image = new Image(file,150,180,false,false);
        ImageView iVPlayer = new ImageView(image);
        iVPlayer.setPreserveRatio(true);
        iVPlayer.setFitWidth(150);
        
        file = new FileInputStream("src/RF_V1/images/jamyCoupPoing.png");
        image = new Image(file,150,180,false,false);
        ImageView iVPlayerPunch = new ImageView(image);
        iVPlayerPunch.setPreserveRatio(true);
        iVPlayerPunch.setFitWidth(150);
        
        Player p1 = new Player(iVPlayer,50, background.getHeight()-200);
        
        /*
        * Création d'une cible
        */
        FileInputStream targetFile = new FileInputStream("src/RF_V1/images/ennemi.PNG");
        Image targetImg = new Image(targetFile, 150, 180, false, false);
        ImageView iVTarget = new ImageView(targetImg);
        iVTarget.setPreserveRatio(true);
        iVTarget.setFitWidth(90);
        
        Target target = new Target(iVTarget, 50, 2, background.getWidth()-250, background.getHeight()-180);
        
        /*
        * Mise en place du score
        */
        Score score = new Score();
        Text textScore = new Text();
        textScore.setFont(Font.font(STYLESHEET_MODENA, FontWeight.THIN, FontPosture.REGULAR, 20));
        textScore.setFill(Color.RED);
        textScore.setX(25);
        textScore.setY(25);
        
        /*
        * Boucle du jeu
        */
        TimerTask gameLoop = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    p1.draw();
                    target.draw();
                    textScore.setText(String.valueOf(score.getScore()));
                    if(target.isAlive() == false){
                        score.addPoints(target.getPoints());
                        root.getChildren().remove(iVTarget);
                    }
                });
            }
        };
        itsTimer = new Timer();
        itsTimer.schedule(gameLoop,1000, 2);
        
        /*
        * Timer du jeu
        */
        Text timeText = new Text();
        timeText.setX(background.getWidth()-50);
        timeText.setY(25);
        timeText.setFont(Font.font(STYLESHEET_MODENA, FontWeight.THIN, FontPosture.REGULAR, 20));
        timeText.setFill(Color.GREEN);
        
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeText.setText(String.valueOf(time));
                    if(time <=0){
                        stop();
                        timeText.setText("0");
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        
        /*
        * Gestion des évenements clavier
        */
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                KeyCode keyCode = ke.getCode();
                switch(keyCode){
                    case A: p1.setSkin(iVPlayerPunch);
                            iVPlayer.setVisible(false);
                            iVPlayerPunch.setVisible(true);
                            p1.setSpeed(0);
                            if(iVPlayer.getX() >= iVTarget.getX()-(iVTarget.getFitWidth()/2)){
                                target.hit(p1.getDamage());
                            }
                            if(target.getLifePoints() <= 0){
                                score.addPoints(target.getPoints());
                                root.getChildren().remove(iVTarget);
                            }
                        break;
                    case LEFT: if(p1.getSkin()!=iVPlayerPunch){
                            p1.setSpeed(-1);
                            vitesse=-1;
                        }
                        break;
                    case RIGHT: if(p1.getSkin()!=iVPlayerPunch){
                            p1.setSpeed(1);
                            vitesse=1;
                        }
                        break;
                    default: break;
                }
            }
        });
        
        scene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if(keyCode.equals(KeyCode.A)){
                p1.setSkin(iVPlayer);
                iVPlayer.setVisible(true);
                iVPlayerPunch.setVisible(false);
                p1.setSpeed(vitesse);
            } else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p1.setSpeed(0);
                vitesse=0;
            }
        });
        
        
        root.getChildren().add(iVPlayer);
        root.getChildren().add(iVPlayerPunch);
        root.getChildren().add(iVTarget);
        root.getChildren().add(textScore);
        root.getChildren().add(timeText);
        iVPlayerPunch.setVisible(false);
        scene.setFill(Color.GREEN);
        primaryStage.setTitle("Random Fight V1");
        //primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    @Override
    public void stop(){
        itsTimer.cancel();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
