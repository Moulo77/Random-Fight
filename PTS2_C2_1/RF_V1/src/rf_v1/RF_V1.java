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
import javafx.stage.Stage;

/**
 *
 * @author Palès
 */
public class RF_V1 extends Application {
    Timer itsTimer;
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        AnchorPane root = new AnchorPane();
        //Image background = new Image("images/decor.png");
        /*BackgroundImage background_img = new BackgroundImage(background,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG = new Background(background_img);
        
        
        
        root.setBackground(BG);*/
        
        //Scene scene = new Scene(root, background.getWidth(), background.getHeight());
        Scene scene = new Scene(root, 1000 , 600);
        
        FileInputStream file = new FileInputStream("src/RF_V1/images/jamyCour.png");
        Image image = new Image(file,150,180,false,false);
        ImageView iVPlayer = new ImageView(image);
        
        file = new FileInputStream("src/RF_V1/images/jamyCoupPoing.png");
        image = new Image(file,150,180,false,false);
        ImageView iVPlayerPunch = new ImageView(image);
        
        
        Player p1 = new Player(iVPlayer,450, 460);
        
        TimerTask gameLoop = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                p1.draw();
            });
            }
        };
        itsTimer = new Timer();
        itsTimer.schedule(gameLoop,1000, 1);
        
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                KeyCode keyCode = ke.getCode();
                switch(keyCode){
                    case Q: p1.setSpeed(-1);
                        break;
                    case D: p1.setSpeed(1);
                        break;
                    case A: p1.setSkin(iVPlayerPunch);
                            iVPlayer.setVisible(false);
                            iVPlayerPunch.setVisible(true);
                        break;
                    default: break;
                }
            }
        });
        
        scene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if(keyCode.equals(KeyCode.Q) || keyCode.equals(KeyCode.D)){
                p1.setSpeed(0);
            } else if(keyCode.equals(KeyCode.A)){
                p1.setSkin(iVPlayer);
                iVPlayer.setVisible(true);
                iVPlayerPunch.setVisible(false);
            }
        });
        
        root.getChildren().add(iVPlayer);
        root.getChildren().add(iVPlayerPunch);
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
