/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    Random rand = new Random();
    double randomX;
    Random randTargetLife = new Random();
    int targetLife;
    
    boolean played = false;
    
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
        Image image = new Image(file,140,170,false,false);
        ImageView iVPlayer = new ImageView(image);
        iVPlayer.setPreserveRatio(true);
        iVPlayer.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V1/images/jamyCoupPoing.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerPunch = new ImageView(image);
        iVPlayerPunch.setPreserveRatio(true);
        iVPlayerPunch.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V1/images/jamyCoupPoingG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerPunchL = new ImageView(image);
        iVPlayerPunchL.setPreserveRatio(true);
        iVPlayerPunchL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V1/images/jamyCourG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerL = new ImageView(image);
        iVPlayerL.setPreserveRatio(true);
        iVPlayerL.setFitWidth(110);
        
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
        FileInputStream scoreBar = new FileInputStream("src/RF_V1/images/LaBarre.png");
        Image scoreBarImg = new Image(scoreBar);
        ImageView iVScoreBar = new ImageView(scoreBarImg);
        iVScoreBar.setX(0);
        iVScoreBar.setY(-30);
        
        Score score = new Score();
        Text textScore = new Text();
        textScore.setFont(wallpoet);
        textScore.setFill(Color.WHITE);
        textScore.setEffect(dropShadow);
        textScore.setX(25);
        textScore.setY(27);
        
        /*
        * Boucle du jeu
        */
        TimerTask gameLoop = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    p1.draw();
                    target.draw();
                    if(!target.isAlive()){
                        do{
                        randomX = rand.nextInt(740 - 0 + 1) + 0;
                        target.setX(randomX);
                        targetLife = randTargetLife.nextInt((4-1)+1);
                        for(int i=1;i<4;i++){
                            if(i==targetLife){
                                target.setPoints(i*50);
                            }
                        }   
                        target.setLife(targetLife);
                        root.getChildren().add(iVTarget);
                        }while( randomX >= p1.getX()-30 && randomX <=p1.getX()+140);
                    }
                    textScore.setText("Score : " + String.valueOf(score.getScore()));
                    if(p1.getX() >= (iVTarget.getX()-iVTarget.getFitWidth()/2)
                            && p1.getX() <= (iVTarget.getX()+iVTarget.getFitWidth()/2)
                            && target.isAlive()){
                        if(p1.getX() >= (iVTarget.getX()-iVTarget.getFitWidth()/2)
                                && p1.getX() <= iVTarget.getX()){
                            p1.setX(iVTarget.getX()- iVTarget.getFitWidth()/2);
                        } else if(p1.getX() <= (iVTarget.getX()+iVTarget.getFitWidth()/2)
                                    && p1.getX() >= iVTarget.getX()){
                            p1.setX(iVTarget.getX()+ iVTarget.getFitWidth()/2);
                        } 
                    }
                });
            }
        };
        itsTimer = new Timer();
        itsTimer.schedule(gameLoop,1000, 1);
        
        /*
        * Timer du jeu
        */
        
        Text timeText = new Text();
        timeText.setX(background.getWidth()-160);
        timeText.setY(27);
        timeText.setFont(wallpoet);
        timeText.setFill(Color.WHITE);
        timeText.setEffect(dropShadow);
        
        
        /*
        * Gestion des évenements clavier
        */
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                KeyCode keyCode = ke.getCode();
                switch(keyCode){
                    case A: if(p1.getSkin()== iVPlayer){
                                p1.setSkin(iVPlayerPunch);
                                iVPlayerPunch.setVisible(true);
                                iVPlayer.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayer.getX() >= iVTarget.getX()-(iVTarget.getFitWidth())
                                        && iVPlayer.getX() <= iVTarget.getX()+iVTarget.getFitWidth()){
                                target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerL){
                                p1.setSkin(iVPlayerPunchL);
                                iVPlayerPunchL.setVisible(true);
                                iVPlayerL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerL.getX() <= iVTarget.getX()+(iVTarget.getFitWidth())
                                        && iVPlayerL.getX() >= iVTarget.getX()-iVTarget.getFitWidth()){
                                    target.hit(p1.getDamage());
                                }
                            }
                            
                            if(target.getLifePoints() <= 0){
                                score.addPoints(target.getPoints());
                                root.getChildren().remove(iVTarget);
                            }
                        break;
                    case LEFT: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL){
                            p1.setSkin(iVPlayerL);
                            iVPlayerL.setVisible(true);
                            iVPlayer.setVisible(false);
                            p1.setSpeed(-1);
                            vitesse=-1;
                        }
                        break;
                    case RIGHT: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL){
                        p1.setSkin(iVPlayer);
                        iVPlayer.setVisible(true);
                        iVPlayerL.setVisible(false);
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
                if(p1.getSkin()==iVPlayerPunch){
                    p1.setSkin(iVPlayer);
                    iVPlayer.setVisible(true);
                    iVPlayerPunch.setVisible(false);
                } else if(p1.getSkin()==iVPlayerPunchL){
                    p1.setSkin(iVPlayerL);
                    iVPlayerL.setVisible(true);
                    iVPlayerPunchL.setVisible(false);
                }
                p1.setSpeed(vitesse);
            } else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p1.setSpeed(0);
                vitesse=0;
            }
        });
        Text finalScore = new Text();
        
        root.getChildren().add(iVPlayer);
        root.getChildren().add(iVPlayerL);
        root.getChildren().add(iVPlayerPunchL);
        root.getChildren().add(iVPlayerPunch);
        root.getChildren().add(textScore);
        root.getChildren().add(iVTarget);
        root.getChildren().add(timeText);
        iVPlayerL.setVisible(false);
        iVPlayerPunch.setVisible(false);
        iVPlayerPunchL.setVisible(false);
        scene.setFill(Color.GREEN);
        
        
        /*
        * Menu principal
        */
        AnchorPane menuPane = new AnchorPane();
        
        Font wallpoet = Font.loadFont(getClass().getClassLoader().getResource("font/Wallpoet-Regular.ttf").toExternalForm(), 30);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.RED);
        
        FileInputStream menuBackgroundFile = new FileInputStream("src/RF_V1/images/menuBackground.png");
        Image menuBackground = new Image(menuBackgroundFile);
        BackgroundImage menuBackgroundImg = new BackgroundImage(menuBackground,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background menuBG = new Background(menuBackgroundImg);
       
        menuPane.setBackground(menuBG);
        
        Scene menuScene = new Scene(menuPane, 800, 450);
        
        Stop[] stops = new Stop[] { new Stop(0, Color.rgb(109, 7, 26)), new Stop(0.5, Color.RED), new Stop(1, Color.rgb(109, 7, 26))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill buttonBackground = new BackgroundFill(lg1, CornerRadii.EMPTY, Insets.EMPTY);
        
        Text playText = new Text("Play");
        playText.setEffect(dropShadow);
        playText.setFill(Color.WHITE);
        playText.setFont(wallpoet);
        Button play = new Button();
        play.setBackground(new Background(buttonBackground));
        play.setGraphic(playText);
        play.setMinHeight(50);
        play.setMinWidth(100);
        play.setTranslateX(500);
        play.setTranslateY(150);
        
        Text exitText = new Text("Exit");
        exitText.setEffect(dropShadow);
        exitText.setFill(Color.WHITE);
        exitText.setFont(wallpoet);
        Button exit = new Button();
        exit.setBackground(new Background(buttonBackground));
        exit.setGraphic(exitText);
        exit.setMinHeight(50);
        exit.setMinWidth(100);
        exit.setTranslateX(500);
        exit.setTranslateY(250);
        
        FileInputStream titleMenu = new FileInputStream("src/RF_V1/images/Title.png");
        Image imgTitle = new Image(titleMenu, 600, 100 , false, false);
        ImageView iVTitle = new ImageView(imgTitle);
        iVTitle.setX(400 - (imgTitle.getWidth()/2));
        iVTitle.setY(10);
        
        FileInputStream fileMenu = new FileInputStream("src/RF_V1/images/jamyface.png");
        Image imageMenu = new Image(fileMenu, 300, 300, true, false);
        ImageView iVMenu = new ImageView(imageMenu);
        iVMenu.setX(30);
        iVMenu.setY(200 - (imageMenu.getHeight()/2));
        
        Text name = new Text("Jamy");
        name.setFont(wallpoet);
        name.setEffect(dropShadow);
        name.setFill(Color.WHITE);
        name.setX(130);
        name.setY(375);
        
        Text arrows = new Text("<- -> ");
        arrows.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 20));
        arrows.setFill(Color.WHITE);
        arrows.setEffect(dropShadow);
        arrows.setX(500);
        arrows.setY(350);
        
        Text A = new Text("A");
        A.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 20));
        A.setFill(Color.WHITE);
        A.setEffect(dropShadow);
        A.setX(520);
        A.setY(400);
        
        Text move = new Text("move");
        move.setFill(Color.WHITE);
        move.setEffect(dropShadow);
        move.setX(575);
        move.setY(350);
        
        Text punch = new Text ("punch");
        punch.setFill(Color.WHITE);
        punch.setEffect(dropShadow);
        punch.setX(575);
        punch.setY(400);

        menuPane.getChildren().add(iVTitle);
        menuPane.getChildren().add(name);
        menuPane.getChildren().add(A);
        menuPane.getChildren().add(move);
        menuPane.getChildren().add(punch);
        menuPane.getChildren().add(arrows);
        menuPane.getChildren().add(iVMenu);
        menuPane.getChildren().add(play);
        menuPane.getChildren().add(exit);
        
        
        
        /*
        * Gestion des fenetres du jeu
        */
        play.setOnAction((ActionEvent event) ->{
            primaryStage.setScene(scene);
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeText.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        stop();
                        timeText.setText("Time : 0");
                        finalScore.setText("Votre score : " + score.getScore() + " points!");
                        finalScore.setFont(wallpoetBigger);
                        finalScore.setFill(Color.WHITE);
                        finalScore.setEffect(dropShadow);
                        finalScore.setX(100);
                        finalScore.setY(200);
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        });
        exit.setOnAction((ActionEvent event) ->{
            primaryStage.close();
        });
        
        primaryStage.setTitle("Random Fight V1");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
    
    @Override
    public void stop(){
        itsTimer.cancel();
        gameTimer.cancel();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            launch(args);
    }
    
}
