/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 * @author Palès
 */
public class RF_V2 extends Application {
    Timer itsTimer;
    int vitesse;
    Timer gameTimer;
    int time =120;
    int timeTemp;
    double musicTime;
    Random rand = new Random();
    double randomX;
    Random randTargetLife = new Random();   
    int targetLife;
    DropShadow dropShadow;
    double targetPosX;
    
    boolean played = false;
    
    Connection con = null;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        primaryStage.getIcons().add(new Image(RF_V2.class.getResourceAsStream("images/icon.png")));
        
        AnchorPane root = new AnchorPane();
        
        /*
        * DATABASE
        */
        try 
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch(Exception e )
        {
            System.out.println("Exception found ! " + e);  
        }
        
        try
        {
            con = DriverManager.getConnection("jdbc:sqlite:Scores.db");
        }
        catch (SQLException s)
        {
            System.out.println("Exception 2 : "+ s);
        }
        
        /*
        * Chargement des sons et musiques
        */
        URL menuMusicFile = getClass().getResource("sounds/menuMusic.mp3");
        Media mediaMenu = new Media(menuMusicFile.toExternalForm());
        MediaPlayer menuMusic = new MediaPlayer(mediaMenu);
        menuMusic.setVolume(0.5);
        menuMusic.play();
        
        URL gameMusicFile = getClass().getResource("sounds/gameMusic.mp3");
        Media mediaGame = new Media(gameMusicFile.toExternalForm());
        MediaPlayer gameMusic = new MediaPlayer(mediaGame);
        gameMusic.setVolume(0.5);
        
        URL hitPunchFile = getClass().getResource("sounds/punchSound.mp3");
        Media mediaPunch = new Media(hitPunchFile.toExternalForm());
        MediaPlayer punchSound = new MediaPlayer(mediaPunch);
        punchSound.setVolume(0.5);
        
        URL hitKickFile = getClass().getResource("sounds/kickSound.mp3");
        Media mediaKick = new Media(hitKickFile.toExternalForm());
        MediaPlayer kickSound = new MediaPlayer(mediaKick);
        kickSound.setVolume(0.5);
        
        /*
        * Création du background du jeu
        */
        FileInputStream backgroundFile = new FileInputStream("src/RF_V2/images/background.png");
        Image background = new Image(backgroundFile);
        BackgroundImage background_img = new BackgroundImage(background,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG = new Background(background_img);
        
        
        FileInputStream menuBackgroundFile = new FileInputStream("src/RF_V2/images/menuBackground.png");
        Image menuBackground = new Image(menuBackgroundFile);
        BackgroundImage menuBackgroundImg = new BackgroundImage(menuBackground,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background menuBG = new Background(menuBackgroundImg);
        
        root.setBackground(BG);
        
        Scene scene = new Scene(root, background.getWidth(), background.getHeight());
        
        /*
        * Création des graphismes
        */
        Font wallpoet = Font.loadFont(getClass().getClassLoader().getResource("font/Wallpoet-Regular.ttf").toExternalForm(), 25);
        Font wallpoetBigger = Font.loadFont(getClass().getClassLoader().getResource("font/Wallpoet-Regular.ttf").toExternalForm(), 35);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.RED);
        
        Stop[] stops = new Stop[] { new Stop(0, Color.rgb(109, 7, 26)),
                                    new Stop(0.2, Color.DARKRED), 
                                    new Stop(0.5, Color.RED), 
                                    new Stop(0.8, Color.DARKRED), 
                                    new Stop(1, Color.rgb(109, 7, 26))};
        LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
        BackgroundFill buttonBackground = new BackgroundFill(lg1, new CornerRadii(15), Insets.EMPTY);
        
        /*
        * Création du joueur 
        */
        FileInputStream file = new FileInputStream("src/RF_V2/images/jamyCour.png");
        Image image = new Image(file,140,170,false,false);
        ImageView iVPlayer = new ImageView(image);
        iVPlayer.setPreserveRatio(true);
        iVPlayer.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyCoupPoing.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerPunch = new ImageView(image);
        iVPlayerPunch.setPreserveRatio(true);
        iVPlayerPunch.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyCoupPoingG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerPunchL = new ImageView(image);
        iVPlayerPunchL.setPreserveRatio(true);
        iVPlayerPunchL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyCourG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerL = new ImageView(image);
        iVPlayerL.setPreserveRatio(true);
        iVPlayerL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyCoupPied.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerKick = new ImageView(image);
        iVPlayerKick.setPreserveRatio(true);
        iVPlayerKick.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyCoupPiedG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerKickL = new ImageView(image);
        iVPlayerKickL.setPreserveRatio(true);
        iVPlayerKickL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupie.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouch = new ImageView(image);
        iVPlayerCrouch.setPreserveRatio(true);
        iVPlayerCrouch.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupieG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouchL = new ImageView(image);
        iVPlayerCrouchL.setPreserveRatio(true);
        iVPlayerCrouchL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamySaut.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerJump = new ImageView(image);
        iVPlayerJump.setPreserveRatio(true);
        iVPlayerJump.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamySautG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerJumpL = new ImageView(image);
        iVPlayerJumpL.setPreserveRatio(true);
        iVPlayerJumpL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamySaut2.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerJump2 = new ImageView(image);
        iVPlayerJump2.setPreserveRatio(true);
        iVPlayerJump2.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamySaut2G.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerJump2L = new ImageView(image);
        iVPlayerJump2L.setPreserveRatio(true);
        iVPlayerJump2L.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupieCoupPied.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouchKick = new ImageView(image);
        iVPlayerCrouchKick.setPreserveRatio(true);
        iVPlayerCrouchKick.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupieCoupPiedG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouchKickL = new ImageView(image);
        iVPlayerCrouchKickL.setPreserveRatio(true);
        iVPlayerCrouchKickL.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupieCoupPoing.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouchPunch = new ImageView(image);
        iVPlayerCrouchPunch.setPreserveRatio(true);
        iVPlayerCrouchPunch.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V2/images/jamyAcroupieCoupPoingG.png");
        image = new Image(file,140,170,false,false);
        ImageView iVPlayerCrouchPunchL = new ImageView(image);
        iVPlayerCrouchPunchL.setPreserveRatio(true);
        iVPlayerCrouchPunchL.setFitWidth(110);
        
        Player p1 = new Player(iVPlayer,50, 250);
        
        /*
        * Création d'une cible
        */
        FileInputStream targetFile = new FileInputStream("src/RF_V2/images/ennemi.PNG");
        Image targetImg = new Image(targetFile, 150, 180, false, false);
        ImageView iVTarget = new ImageView(targetImg);
        iVTarget.setPreserveRatio(true);
        iVTarget.setFitWidth(90);
        
        FileInputStream targetFile2 = new FileInputStream("src/RF_V2/images/ennemiLVL2.PNG");
        Image targetImg2 = new Image(targetFile2, 150, 180, false, false);
        ImageView iVTarget2 = new ImageView(targetImg2);
        iVTarget2.setPreserveRatio(true);
        iVTarget2.setFitWidth(90);
        
        FileInputStream targetFile3 = new FileInputStream("src/RF_V2/images/ennemiLVL3.PNG");
        Image targetImg3 = new Image(targetFile3, 150, 180, false, false);
        ImageView iVTarget3 = new ImageView(targetImg3);
        iVTarget3.setPreserveRatio(true);
        iVTarget3.setFitWidth(90);
        
        Target target = new Target(iVTarget, 50, 2, background.getWidth()-250, background.getHeight()-180);
        target.setSpeed(1);
        
        /*
        * Mise en place du score
        */
        FileInputStream scoreBar = new FileInputStream("src/RF_V2/images/LaBarre.png");
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
                        targetPosX = randomX;
                        targetLife = randTargetLife.nextInt((4-1)+1);
                        switch(targetLife){
                            case 1: 
                                    target.setSkin(iVTarget);
                                    iVTarget.setVisible(true);
                                    iVTarget2.setVisible(false);
                                    iVTarget3.setVisible(false);
                                    break;
                            case 2: 
                                    target.setSkin(iVTarget2);
                                    iVTarget.setVisible(false);
                                    iVTarget2.setVisible(true);
                                    iVTarget3.setVisible(false);
                                    break;
                            case 3: 
                                    target.setSkin(iVTarget3);
                                    iVTarget.setVisible(false);
                                    iVTarget2.setVisible(false);
                                    iVTarget3.setVisible(true);
                                    break;
                            default:break;
                       }
                        for(int i=1;i<4;i++){
                            if(i==targetLife){
                                target.setPoints(i*50);
                            }
                        }   
                        target.setLife(targetLife);
                        try{
                            root.getChildren().add(iVTarget);
                        }catch(IllegalArgumentException e){
                            System.out.println("IllegalArgumentException capturée ligne 218");
                        }
                        
                        }while( randomX >= p1.getX()-30 && randomX <=p1.getX()+140);
                    }
                    
                    if(target.getX()>=targetPosX+50){
                        target.setSpeed(-1);
                    } else if (target.getX()<=targetPosX-50){
                        target.setSpeed(1);
                    }
                    
                    textScore.setText("Score : " + String.valueOf(score.getScore()));
                    if(p1.getX() >= (target.getSkin().getX()-target.getSkin().getFitWidth()/2)
                            && p1.getX() <= (target.getSkin().getX()+target.getSkin().getFitWidth()/2)
                            && p1.getY() >= 200
                            && target.isAlive()){
                        if(p1.getX() >= (target.getSkin().getX()-target.getSkin().getFitWidth()/2)
                                && p1.getX() <= target.getSkin().getX()){
                            p1.setX(target.getSkin().getX()- target.getSkin().getFitWidth()/2);
                        } else if(p1.getX() <= (target.getSkin().getX()+target.getSkin().getFitWidth()/2)
                                    && p1.getX() >= target.getSkin().getX()){
                            p1.setX(target.getSkin().getX()+ target.getSkin().getFitWidth()/2);
                        } 
                    }
                    if(p1.getY()==250){
                        if(p1.getSkin()==iVPlayerJump){
                            p1.setSkin(iVPlayer);
                            iVPlayerJump.setVisible(false);
                            iVPlayer.setVisible(true);
                        } else if(p1.getSkin()==iVPlayerJumpL){
                            p1.setSkin(iVPlayerL);
                            iVPlayerJumpL.setVisible(false);
                            iVPlayerL.setVisible(true);
                        }
                    }
                });
            }
        };
        itsTimer = new Timer();
        itsTimer.schedule(gameLoop,1000, 1);
      
        /*
        * Menu principal
        */
        AnchorPane menuPane = new AnchorPane();
        
       
        menuPane.setBackground(menuBG);
        
        Scene menuScene = new Scene(menuPane, 800, 450);
                
        FileInputStream titleMenu = new FileInputStream("src/RF_V2/images/Title.png");
        Image imgTitle = new Image(titleMenu, 600, 100 , false, false);
        ImageView iVTitle = new ImageView(imgTitle);
        iVTitle.setX(400 - (imgTitle.getWidth()/2));
        iVTitle.setY(10);
        
        Text playText = new Text("Play");
        playText.setFill(Color.WHITE);
        playText.setEffect(dropShadow);
        playText.setFont(wallpoetBigger);
        Button play = new Button();
        play.setBackground(new Background(buttonBackground));
        play.setGraphic(playText);
        play.setMinHeight(70);
        play.setMinWidth(500);
        play.setTranslateX(background.getWidth()/2 - 250);
        play.setTranslateY(120);
        
        Text scoreText = new Text("Scores");
        scoreText.setFill(Color.WHITE);
        scoreText.setEffect(dropShadow);
        scoreText.setFont(wallpoetBigger);
        Button scoreButton = new Button();
        scoreButton.setBackground(new Background(buttonBackground));
        scoreButton.setGraphic(scoreText);
        scoreButton.setMinHeight(70);
        scoreButton.setMinWidth(500);
        scoreButton.setTranslateX(background.getWidth()/2 - 250);
        scoreButton.setTranslateY(200);
        
        Text settingsText = new Text("Settings");
        settingsText.setFill(Color.WHITE);
        settingsText.setEffect(dropShadow);
        settingsText.setFont(wallpoetBigger);
        Button settingsButton = new Button();
        settingsButton.setBackground(new Background(buttonBackground));
        settingsButton.setGraphic(settingsText);
        settingsButton.setMinHeight(70);
        settingsButton.setMinWidth(500);
        settingsButton.setTranslateX(background.getWidth()/2 - 250);
        settingsButton.setTranslateY(280);
        
        Text exitText = new Text("Exit");
        exitText.setFill(Color.WHITE);
        exitText.setEffect(dropShadow);
        exitText.setFont(wallpoetBigger);
        Button exit = new Button();
        exit.setBackground(new Background(buttonBackground));
        exit.setGraphic(exitText);
        exit.setMinHeight(70);
        exit.setMinWidth(500);
        exit.setTranslateX(background.getWidth()/2 - 250);
        exit.setTranslateY(360);

        
        menuPane.getChildren().add(scoreButton);
        menuPane.getChildren().add(settingsButton);
        menuPane.getChildren().add(iVTitle);
        menuPane.getChildren().add(play);
        menuPane.getChildren().add(exit);
        
        /*
        *Fenetre des scores
        */
        GridPane scorePane = new GridPane();
        scorePane.setAlignment(Pos.TOP_CENTER);
        scorePane.setVgap(25);
        scorePane.setHgap(25);
        scorePane.setPadding(new Insets(15,15,15,15));
        scorePane.setBackground(menuBG);
       
        Text pseudoPlayer1 = new Text();
        pseudoPlayer1.setFont(wallpoet);
        pseudoPlayer1.setFill(Color.GOLD);
        scorePane.add(pseudoPlayer1, 0, 1);
        
        Text scorePlayer1 = new Text();
        scorePlayer1.setFont(wallpoet);
        scorePlayer1.setFill(Color.GOLD);
        scorePane.add(scorePlayer1, 1, 1);
        
        Text pseudoPlayer2 = new Text();
        pseudoPlayer2.setFont(wallpoet);
        pseudoPlayer2.setFill(Color.GREY);
        scorePane.add(pseudoPlayer2, 0, 2);
        
        Text scorePlayer2 = new Text();
        scorePlayer2.setFont(wallpoet);
        scorePlayer2.setFill(Color.GREY);
        scorePane.add(scorePlayer2, 1, 2);
        
        Text pseudoPlayer3 = new Text();
        pseudoPlayer3.setFont(wallpoet);
        pseudoPlayer3.setFill(Color.BROWN);
        scorePane.add(pseudoPlayer3, 0, 3);
        
        Text scorePlayer3 = new Text();
        scorePlayer3.setFont(wallpoet);
        scorePlayer3.setFill(Color.BROWN);
        scorePane.add(scorePlayer3, 1, 3);
        
        Text pseudoPlayer4 = new Text();
        pseudoPlayer4.setFont(wallpoet);
        pseudoPlayer4.setFill(Color.WHITE);
        scorePane.add(pseudoPlayer4, 0, 4);
        
        Text scorePlayer4 = new Text();
        scorePlayer4.setFont(wallpoet);
        scorePlayer4.setFill(Color.WHITE);
        scorePane.add(scorePlayer4, 1, 4);
        
        Text pseudoPlayer5 = new Text();
        pseudoPlayer5.setFont(wallpoet);
        pseudoPlayer5.setFill(Color.WHITE);
        scorePane.add(pseudoPlayer5, 0, 5);
        
        Text scorePlayer5 = new Text();
        scorePlayer5.setFont(wallpoet);
        scorePlayer5.setFill(Color.WHITE);
        scorePane.add(scorePlayer5, 1, 5);
        
        String sql = "SELECT * FROM Scores ORDER by points DESC;";
        
        String[] bestsScores = new String[5];
        String[] bestsScorePseudo = new String[5];
        
        PreparedStatement p = null;
        ResultSet r = null;
        
        int i = 0;
                
        try
        {
            p = con.prepareStatement( sql );
            p.clearParameters();
            
            r = p.executeQuery();
            
            int points;
            String pseudo;
            
            while (r.next() && i<5)
            {
                points = r.getInt(1);
                pseudo = r.getString("pseudo");
                
                bestsScores[i] = String.valueOf(points);
                bestsScorePseudo[i] = pseudo;
                
                i++;
            }
            p.close();
            r.close();
        }
        catch ( SQLException s)
        {
            System.out.println("Exception 3 : "+ s);
        }
        
        pseudoPlayer1.setText(bestsScorePseudo[0]);
        pseudoPlayer2.setText(bestsScorePseudo[1]);
        pseudoPlayer3.setText(bestsScorePseudo[2]);
        pseudoPlayer4.setText(bestsScorePseudo[3]);
        pseudoPlayer5.setText(bestsScorePseudo[4]);
        
        scorePlayer1.setText(bestsScores[0]);
        scorePlayer2.setText(bestsScores[1]);
        scorePlayer3.setText(bestsScores[2]);
        scorePlayer4.setText(bestsScores[3]);
        scorePlayer5.setText(bestsScores[4]);
        
        Text pseudoText = new Text("Pseudo");
        pseudoText.setFont(wallpoet);
        pseudoText.setFill(Color.WHITE);
        pseudoText.setEffect(dropShadow);
        scorePane.add(pseudoText, 0, 0);
        
        Text scoreTextTable = new Text("Score");
        scoreTextTable.setFont(wallpoet);
        scoreTextTable.setFill(Color.WHITE);
        scoreTextTable.setEffect(dropShadow);
        scorePane.add(scoreTextTable, 1, 0);
        
        Scene scoreTable = new Scene(scorePane, 800, 450);
        
        scoreTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    primaryStage.setScene(menuScene);
                }
            }
            
        });
        
        scoreButton.setOnAction((ActionEvent event) ->{
            primaryStage.setScene(scoreTable);
        });
        
        /*
        *Fenêtre des paramètres
        */
        Text leftArrow = new Text("←");
        leftArrow.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        leftArrow.setFill(Color.WHITE);
        leftArrow.setEffect(dropShadow);
        leftArrow.setX(150);
        leftArrow.setY(90);
        
        Text moveLeft = new Text("move left");
        moveLeft.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        moveLeft.setFill(Color.WHITE);
        moveLeft.setEffect(dropShadow);
        moveLeft.setX(200);
        moveLeft.setY(90);
        
        Text rightArrow = new Text("→");
        rightArrow.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        rightArrow.setFill(Color.WHITE);
        rightArrow.setEffect(dropShadow);
        rightArrow.setX(150);
        rightArrow.setY(150);
        
        Text moveRight = new Text("move left");
        moveRight.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        moveRight.setFill(Color.WHITE);
        moveRight.setEffect(dropShadow);
        moveRight.setX(200);
        moveRight.setY(150);
        
        Text upArrow = new Text("↑");
        upArrow.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        upArrow.setFill(Color.WHITE);
        upArrow.setEffect(dropShadow);
        upArrow.setX(150);
        upArrow.setY(210);
        
        Text jump = new Text("jump");
        jump.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        jump.setFill(Color.WHITE);
        jump.setEffect(dropShadow);
        jump.setX(200);
        jump.setY(210);
        
        Text downArrow = new Text("↓");
        downArrow.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        downArrow.setFill(Color.WHITE);
        downArrow.setEffect(dropShadow);
        downArrow.setX(150);
        downArrow.setY(270);
        
        Text crouch = new Text("crouch");
        crouch.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        crouch.setFill(Color.WHITE);
        crouch.setEffect(dropShadow);
        crouch.setX(200);
        crouch.setY(270);
        
        Text A = new Text("A");
        A.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        A.setFill(Color.WHITE);
        A.setEffect(dropShadow);
        A.setX(150);
        A.setY(330);
        
        Text punch = new Text ("punch");
        punch.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        punch.setFill(Color.WHITE);
        punch.setEffect(dropShadow);
        punch.setX(200);
        punch.setY(330);

        Text Z = new Text("Z");
        Z.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        Z.setFill(Color.WHITE);
        Z.setEffect(dropShadow);
        Z.setX(150);
        Z.setY(390);
        
        Text kick = new Text ("kick");
        kick.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        kick.setFill(Color.WHITE);
        kick.setEffect(dropShadow);
        kick.setX(200);
        kick.setY(390);
        
        Text volumeText = new Text("Volume");
        volumeText.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        volumeText.setFill(Color.WHITE);
        volumeText.setEffect(dropShadow);
        volumeText.setX(565);
        volumeText.setY(175);
        
        Slider volumeSlider = new Slider(0, 1, 0);
        volumeSlider.setEffect(dropShadow);
        volumeSlider.setTranslateX(550);
        volumeSlider.setTranslateY(200);
        volumeSlider.setValue(0.5);
        
        menuMusic.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        gameMusic.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        punchSound.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        kickSound.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        
        Text volumePercent = new Text("50%");
        volumePercent.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 25));
        volumePercent.setFill(Color.WHITE);
        volumePercent.setEffect(dropShadow);
        volumePercent.setX(600);
        volumePercent.setY(250);
        
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volumePercent.setText(Math.round(newValue.doubleValue()*100) + "%");
            }
        });
        
        
        AnchorPane settingsPane = new AnchorPane();
        settingsPane.setBackground(menuBG);
        
        settingsPane.getChildren().add(volumePercent);
        settingsPane.getChildren().add(volumeText);
        settingsPane.getChildren().add(volumeSlider);
        settingsPane.getChildren().add(leftArrow);
        settingsPane.getChildren().add(moveLeft);
        settingsPane.getChildren().add(rightArrow);
        settingsPane.getChildren().add(moveRight);
        settingsPane.getChildren().add(upArrow);
        settingsPane.getChildren().add(jump);
        settingsPane.getChildren().add(downArrow);
        settingsPane.getChildren().add(crouch);
        settingsPane.getChildren().add(A);
        settingsPane.getChildren().add(punch);
        settingsPane.getChildren().add(Z);
        settingsPane.getChildren().add(kick);
        
        Scene settingsScene = new Scene(settingsPane, 800, 450);
        settingsScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ESCAPE)){
                    primaryStage.setScene(menuScene);
                }
            }
            
        });
        
        settingsButton.setOnAction((ActionEvent event) ->{
            primaryStage.setScene(settingsScene);
        });
        
        /*
        * Timer du jeu
        */
        
        Text timeText = new Text();
        timeText.setX(background.getWidth()-160);
        timeText.setY(27);
        timeText.setFont(wallpoet);
        timeText.setFill(Color.WHITE);
        timeText.setEffect(dropShadow);
        
        root.getChildren().add(iVPlayerKick);
        root.getChildren().add(iVPlayerKickL);
        root.getChildren().add(iVPlayer);
        root.getChildren().add(iVPlayerL);
        root.getChildren().add(iVPlayerPunchL);
        root.getChildren().add(iVPlayerPunch);
        root.getChildren().add(iVPlayerCrouch);
        root.getChildren().add(iVPlayerCrouchL);
        root.getChildren().add(iVPlayerJump);
        root.getChildren().add(iVPlayerJumpL);
        root.getChildren().add(iVPlayerJump2);
        root.getChildren().add(iVPlayerJump2L);
        root.getChildren().add(iVPlayerCrouchKick);
        root.getChildren().add(iVPlayerCrouchKickL);
        root.getChildren().add(iVPlayerCrouchPunch);
        root.getChildren().add(iVPlayerCrouchPunchL);
        root.getChildren().add(iVScoreBar);
        root.getChildren().add(textScore);
        root.getChildren().add(iVTarget);
        root.getChildren().add(timeText);
        root.getChildren().add(iVTarget2);
        root.getChildren().add(iVTarget3);
        iVTarget2.setVisible(false);
        iVTarget3.setVisible(false);
        iVPlayerKick.setVisible(false);
        iVPlayerKickL.setVisible(false);
        iVPlayerL.setVisible(false);
        iVPlayerPunch.setVisible(false);
        iVPlayerPunchL.setVisible(false);
        iVPlayerCrouch.setVisible(false);
        iVPlayerCrouchL.setVisible(false);
        iVPlayerJump.setVisible(false);
        iVPlayerJumpL.setVisible(false);
        iVPlayerJump2.setVisible(false);
        iVPlayerJump2L.setVisible(false);
        iVPlayerCrouchKick.setVisible(false);
        iVPlayerCrouchKickL.setVisible(false);
        iVPlayerCrouchPunch.setVisible(false);
        iVPlayerCrouchPunchL.setVisible(false);
        scene.setFill(Color.GREEN);
        
        /*
        * Fin de partie
        */
        Text replayText = new Text("Replay");
        replayText.setFill(Color.WHITE);
        replayText.setEffect(dropShadow);
        replayText.setFont(wallpoet);
        Button replay = new Button();
        replay.setBackground(new Background(buttonBackground));
        replay.setGraphic(replayText);
        replay.setMinHeight(50);
        replay.setMinWidth(120);
        replay.setTranslateX(350);
        replay.setTranslateY(225);
        
        Text menuText = new Text("Menu");
        menuText.setFill(Color.WHITE);
        menuText.setEffect(dropShadow);
        menuText.setFont(wallpoet);
        Button menu = new Button();
        menu.setBackground(new Background(buttonBackground));
        menu.setGraphic(menuText);
        menu.setMinHeight(50);
        menu.setMinWidth(120);
        menu.setTranslateX(350);
        menu.setTranslateY(300);
        
        Text finalScore = new Text();
        finalScore.setFont(wallpoetBigger);
        finalScore.setFill(Color.WHITE);
        finalScore.setEffect(dropShadow);
        finalScore.setX(170);
        finalScore.setY(100);
        
        Text yourPseudo = new Text("Your Pseudo : ");
        yourPseudo.setFont(wallpoet);
        yourPseudo.setFill(Color.WHITE);
        yourPseudo.setEffect(dropShadow);
        yourPseudo.setX(60);
        yourPseudo.setY(170);
        
        TextField pseudoEntry = new TextField();
        pseudoEntry.setTranslateX(300);
        pseudoEntry.setTranslateY(150);
        
        Text validate = new Text("Validate");
        validate.setFill(Color.WHITE);
        validate.setEffect(dropShadow);
        validate.setFont(wallpoet);
        Button validateButton = new Button();
        validateButton.setBackground(new Background(buttonBackground));
        validateButton.setGraphic(validate);
        validateButton.setMinHeight(30);
        validateButton.setMinWidth(80);
        validateButton.setTranslateX(500);
        validateButton.setTranslateY(150);
        
        root.getChildren().add(yourPseudo);
        root.getChildren().add(validateButton);
        root.getChildren().add(pseudoEntry);
        root.getChildren().add(replay);
        root.getChildren().add(menu);
        root.getChildren().add(finalScore);
        replay.setVisible(false);
        menu.setVisible(false);
        finalScore.setVisible(false);
        yourPseudo.setVisible(false);
        validateButton.setVisible(false);
        pseudoEntry.setVisible(false);
        
        StringProperty endGamePseudo = new SimpleStringProperty();
        
        validateButton.setOnAction((ActionEvent event) -> {
            try {
                PreparedStatement prep = con.prepareStatement("INSERT INTO Scores VALUES(?,?);");
                if(score.getScore()!=0){
                    prep.setInt(1, score.getScore());
                }else{
                    prep.setNull(1, score.getScore());
                }
                
                prep.setString(2, pseudoEntry.getText());
                prep.execute();
                prep.close();
            } catch (SQLException ex) {
                System.err.println("Exception 4 : "+ ex);
            }
            validateButton.setDisable(true);
            
        
            String[] bestsScores2 = new String[5];
            String[] bestsScorePseudo2 = new String[5];
        
            PreparedStatement p2 = null;
            ResultSet r2 = null;
        
            int j = 0;
                
            try
            {
                p2 = con.prepareStatement( sql );
                p2.clearParameters();
            
                r2 = p2.executeQuery();
            
                int points2;
                String pseudo2;
            
                while (r2.next() && j<5)
                {
                    points2 = r2.getInt(1);
                    pseudo2 = r2.getString("pseudo");
                
                    bestsScores2[j] = String.valueOf(points2);
                    bestsScorePseudo2[j] = pseudo2;
                
                    j++;
                }
                p2.close();
                r2.close();
            }
            catch ( SQLException s)
            {
                System.out.println("Exception 3 : "+ s);
            }
            
            pseudoPlayer1.setText(bestsScorePseudo2[0]);
            pseudoPlayer2.setText(bestsScorePseudo2[1]);
            pseudoPlayer3.setText(bestsScorePseudo2[2]);
            pseudoPlayer4.setText(bestsScorePseudo2[3]);
            pseudoPlayer5.setText(bestsScorePseudo2[4]);
        
            scorePlayer1.setText(bestsScores2[0]);
            scorePlayer2.setText(bestsScores2[1]);
            scorePlayer3.setText(bestsScores2[2]);
            scorePlayer4.setText(bestsScores2[3]);
            scorePlayer5.setText(bestsScores2[4]);
        });
        
        /*
        *Fenêtre pause
        */
        AnchorPane pausePane = new AnchorPane();
        pausePane.setBackground(menuBG);
        
        Text continueText = new Text("Resume");
        continueText.setFill(Color.WHITE);
        continueText.setEffect(dropShadow);
        continueText.setFont(wallpoetBigger);
        Button resume = new Button();
        resume.setBackground(new Background(buttonBackground));
        resume.setGraphic(continueText);
        resume.setMinHeight(70);
        resume.setMinWidth(300);
        resume.setTranslateX(250);
        resume.setTranslateY(100);
        
        Text pauseSettings = new Text("Settings");
        pauseSettings.setFill(Color.WHITE);
        pauseSettings.setEffect(dropShadow);
        pauseSettings.setFont(wallpoetBigger);
        Button pauseSettingsButton = new Button();
        pauseSettingsButton.setBackground(new Background(buttonBackground));
        pauseSettingsButton.setGraphic(pauseSettings);
        pauseSettingsButton.setMinHeight(70);
        pauseSettingsButton.setMinWidth(300);
        pauseSettingsButton.setTranslateX(250);
        pauseSettingsButton.setTranslateY(200);
        
        Text mainMenuText = new Text("Menu");
        mainMenuText.setFill(Color.WHITE);
        mainMenuText.setEffect(dropShadow);
        mainMenuText.setFont(wallpoetBigger);
        Button returnMenuButton = new Button();
        returnMenuButton.setBackground(new Background(buttonBackground));
        returnMenuButton.setGraphic(mainMenuText);
        returnMenuButton.setMinHeight(70);
        returnMenuButton.setMinWidth(300);
        returnMenuButton.setTranslateX(250);
        returnMenuButton.setTranslateY(300);
        
        pausePane.getChildren().add(resume);
        pausePane.getChildren().add(pauseSettingsButton);
        pausePane.getChildren().add(returnMenuButton);
        
        Scene pause = new Scene(pausePane, 800, 450);
        
        AnchorPane pauseSettingsPane = new AnchorPane();
        pauseSettingsPane.setBackground(menuBG);
        
        Text leftArrow2 = new Text("←");
        leftArrow2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        leftArrow2.setFill(Color.WHITE);
        leftArrow2.setEffect(dropShadow);
        leftArrow2.setX(150);
        leftArrow2.setY(90);
        
        Text moveLeft2 = new Text("move left");
        moveLeft2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        moveLeft2.setFill(Color.WHITE);
        moveLeft2.setEffect(dropShadow);
        moveLeft2.setX(200);
        moveLeft2.setY(90);
        
        Text rightArrow2 = new Text("→");
        rightArrow2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        rightArrow2.setFill(Color.WHITE);
        rightArrow2.setEffect(dropShadow);
        rightArrow2.setX(150);
        rightArrow2.setY(150);
        
        Text moveRight2 = new Text("move left");
        moveRight2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        moveRight2.setFill(Color.WHITE);
        moveRight2.setEffect(dropShadow);
        moveRight2.setX(200);
        moveRight2.setY(150);
        
        Text upArrow2 = new Text("↑");
        upArrow2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        upArrow2.setFill(Color.WHITE);
        upArrow2.setEffect(dropShadow);
        upArrow2.setX(150);
        upArrow2.setY(210);
        
        Text jump2 = new Text("jump");
        jump2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        jump2.setFill(Color.WHITE);
        jump2.setEffect(dropShadow);
        jump2.setX(200);
        jump2.setY(210);
        
        Text downArrow2 = new Text("↓");
        downArrow2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        downArrow2.setFill(Color.WHITE);
        downArrow2.setEffect(dropShadow);
        downArrow2.setX(150);
        downArrow2.setY(270);
        
        Text crouch2 = new Text("crouch");
        crouch2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        crouch2.setFill(Color.WHITE);
        crouch2.setEffect(dropShadow);
        crouch2.setX(200);
        crouch2.setY(270);
        
        Text A2 = new Text("A");
        A2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        A2.setFill(Color.WHITE);
        A2.setEffect(dropShadow);
        A2.setX(150);
        A2.setY(330);
        
        Text punch2 = new Text ("punch");
        punch2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        punch2.setFill(Color.WHITE);
        punch2.setEffect(dropShadow);
        punch2.setX(200);
        punch2.setY(330);

        Text Z2 = new Text("Z");
        Z2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        Z2.setFill(Color.WHITE);
        Z2.setEffect(dropShadow);
        Z2.setX(150);
        Z2.setY(390);
        
        Text kick2 = new Text ("kick");
        kick2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        kick2.setFill(Color.WHITE);
        kick2.setEffect(dropShadow);
        kick2.setX(200);
        kick2.setY(390);
        
        Text volumeText2 = new Text("Volume");
        volumeText2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        volumeText2.setFill(Color.WHITE);
        volumeText2.setEffect(dropShadow);
        volumeText2.setX(565);
        volumeText2.setY(175);
        
        Slider volumeSlider2 = new Slider(0, 1, 0);
        volumeSlider2.setEffect(dropShadow);
        volumeSlider2.setTranslateX(550);
        volumeSlider2.setTranslateY(200);
        volumeSlider2.setValue(0.5);
        
        menuMusic.volumeProperty().bindBidirectional(volumeSlider2.valueProperty());
        gameMusic.volumeProperty().bindBidirectional(volumeSlider2.valueProperty());
        punchSound.volumeProperty().bindBidirectional(volumeSlider2.valueProperty());
        kickSound.volumeProperty().bindBidirectional(volumeSlider2.valueProperty());
        
        Text volumePercent2 = new Text("50%");
        volumePercent2.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 25));
        volumePercent2.setFill(Color.WHITE);
        volumePercent2.setEffect(dropShadow);
        volumePercent2.setX(600);
        volumePercent2.setY(250);
        
        Scene settingsPause = new Scene(pauseSettingsPane, 800, 450);
        
        volumeSlider2.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volumePercent2.setText(Math.round(newValue.doubleValue()*100) + "%");
            }
        });
        
        Text mainMenu2 = new Text("Return");
        mainMenu2.setEffect(dropShadow);
        mainMenu2.setFill(Color.WHITE);
        mainMenu2.setFont(wallpoet);
        Button mainMenuButton2 = new Button();
        mainMenuButton2.setBackground(new Background(buttonBackground));
        mainMenuButton2.setGraphic(mainMenu2);
        mainMenuButton2.setMinHeight(50);
        mainMenuButton2.setMinWidth(100);
        mainMenuButton2.setTranslateX(10);
        mainMenuButton2.setTranslateY(10);
        
        mainMenuButton2.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(menuScene);
        });
                
        pauseSettingsPane.getChildren().add(mainMenuButton2);
        pauseSettingsPane.getChildren().add(volumePercent2);
        pauseSettingsPane.getChildren().add(volumeText2);
        pauseSettingsPane.getChildren().add(volumeSlider2);
        pauseSettingsPane.getChildren().add(leftArrow2);
        pauseSettingsPane.getChildren().add(moveLeft2);
        pauseSettingsPane.getChildren().add(rightArrow2);
        pauseSettingsPane.getChildren().add(moveRight2);
        pauseSettingsPane.getChildren().add(upArrow2);
        pauseSettingsPane.getChildren().add(jump2);
        pauseSettingsPane.getChildren().add(downArrow2);
        pauseSettingsPane.getChildren().add(crouch2);
        pauseSettingsPane.getChildren().add(A2);
        pauseSettingsPane.getChildren().add(punch2);
        pauseSettingsPane.getChildren().add(Z2);
        pauseSettingsPane.getChildren().add(kick2);
        
        
        
        pauseSettingsButton.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(settingsPause);
        });
        
        mainMenuButton2.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(pause);
        });
        
        resume.setOnAction((ActionEvent event) ->{
            primaryStage.setScene(scene);
            menuMusic.stop();
            gameMusic.setStartTime(Duration.seconds(musicTime));
            gameMusic.play();
            gameMusic.setStartTime(Duration.seconds(0));
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeText.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        menuMusic.play();
                        gameMusic.stop();
                        gameTimer.cancel();
                        time=120;
                        timeText.setText("Time : 0");
                        finalScore.setText("score : " + score.getScore() + " points");
                        replay.setVisible(true);
                        menu.setVisible(true);
                        finalScore.setVisible(true);
                        yourPseudo.setVisible(true);
                        validateButton.setVisible(true);
                        pseudoEntry.setVisible(true);
                        p1.getSkin().setVisible(false);
                        root.getChildren().remove(iVPlayer);
                        root.getChildren().remove(iVPlayerL);
                        root.getChildren().remove(iVPlayerPunchL);
                        root.getChildren().remove(iVPlayerPunch);
                        iVTarget.setVisible(false);
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        });
        
        returnMenuButton.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(menuScene);
        });
        
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
                                if(iVPlayerPunch.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())
                                        && iVPlayerPunch.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())){
                                    punchSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerL){
                                p1.setSkin(iVPlayerPunchL);
                                iVPlayerPunchL.setVisible(true);
                                iVPlayerL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerPunchL.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())
                                        && iVPlayerPunchL.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())){
                                    punchSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()==iVPlayerCrouch){
                                p1.setSkin(iVPlayerCrouchPunch);
                                iVPlayerCrouchPunch.setVisible(true);
                                iVPlayerCrouch.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerCrouchPunch.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())
                                        && iVPlayerCrouchPunch.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth()-50)){
                                    punchSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()==iVPlayerCrouchL){
                                p1.setSkin(iVPlayerCrouchPunchL);
                                iVPlayerCrouchPunchL.setVisible(true);
                                iVPlayerCrouchL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerCrouchPunchL.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())
                                        && iVPlayerCrouchPunchL.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth()-50)){
                                    punchSound.play();
                                    target.hit(p1.getDamage());
                                }
                            }
                            
                            if(target.getLifePoints() <= 0){
                                score.addPoints(target.getPoints());
                                root.getChildren().remove(iVTarget);
                            }
                        break;
                    case Z: if(p1.getSkin()== iVPlayer){
                                p1.setSkin(iVPlayerKick);
                                iVPlayerKick.setVisible(true);
                                iVPlayer.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayer.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())
                                        && iVPlayer.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())){
                                    kickSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerL){
                                p1.setSkin(iVPlayerKickL);
                                iVPlayerKickL.setVisible(true);
                                iVPlayerL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerL.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())
                                        && iVPlayerL.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())){
                                    kickSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerCrouch){
                                p1.setSkin(iVPlayerCrouchKick);
                                iVPlayerCrouchKick.setVisible(true);
                                iVPlayerCrouch.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerCrouchKick.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth())
                                        && iVPlayerCrouchKick.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth()-50)){
                                    kickSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerCrouchL){
                                p1.setSkin(iVPlayerCrouchKickL);
                                iVPlayerCrouchKickL.setVisible(true);
                                iVPlayerCrouchL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerCrouchKickL.getX() <= target.getSkin().getX()+(target.getSkin().getFitWidth())
                                        && iVPlayerCrouchKickL.getX() >= target.getSkin().getX()-(target.getSkin().getFitWidth()-50)){
                                    kickSound.play();
                                    target.hit(p1.getDamage());
                                }
                            }
                    
                            if(target.getLifePoints() <= 0){
                                score.addPoints(target.getPoints());
                                root.getChildren().remove(iVTarget);
                            }
                            break;
                    case LEFT: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL
                                    && p1.getSkin() != iVPlayerKick && p1.getSkin() != iVPlayerKickL 
                                    && p1.getSkin() != iVPlayerCrouch && p1.getSkin()!= iVPlayerCrouchL
                                    && p1.getSkin() != iVPlayerJump && p1.getSkin() != iVPlayerJumpL
                                    && p1.getSkin() != iVPlayerCrouchPunch && p1.getSkin()!=iVPlayerCrouchPunchL
                                    && p1.getSkin()!=iVPlayerCrouchKick && p1.getSkin()!=iVPlayerCrouchKickL){
                                p1.setSkin(iVPlayerL);
                                iVPlayerL.setVisible(true);
                                iVPlayer.setVisible(false);
                                iVPlayerJump2.setVisible(false);
                                iVPlayerJump2L.setVisible(false);
                                p1.setSpeed(-1);
                                vitesse=-1;
                            } else if(p1.getSkin()== iVPlayerCrouch || p1.getSkin()==iVPlayerCrouchL){
                                p1.setSpeed(-1);
                                vitesse=-1;
                            } else if(p1.getSkin()== iVPlayerJump || p1.getSkin()==iVPlayerJumpL){
                                p1.setSpeed(-1);
                                vitesse=-1;
                                p1.setSkin(iVPlayerJumpL);
                                iVPlayerJumpL.setVisible(true);
                                iVPlayerJump.setVisible(false);
                            }
                        break;
                    case RIGHT: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL
                            && p1.getSkin() != iVPlayerKick && p1.getSkin() != iVPlayerKickL
                            && p1.getSkin() != iVPlayerCrouch && p1.getSkin()!= iVPlayerCrouchL
                            && p1.getSkin() != iVPlayerJump && p1.getSkin() != iVPlayerJumpL
                            && p1.getSkin() != iVPlayerCrouchPunch && p1.getSkin()!=iVPlayerCrouchPunchL
                            && p1.getSkin()!=iVPlayerCrouchKick && p1.getSkin()!=iVPlayerCrouchKickL){
                            p1.setSkin(iVPlayer);
                            iVPlayer.setVisible(true);
                            iVPlayerL.setVisible(false);
                            iVPlayerJump2.setVisible(false);
                            iVPlayerJump2L.setVisible(false);
                            p1.setSpeed(1);
                            vitesse=1;
                        } else if(p1.getSkin()== iVPlayerCrouch || p1.getSkin()==iVPlayerCrouchL){
                            p1.setSpeed(1);
                            vitesse=1;
                        } else if(p1.getSkin()== iVPlayerJump || p1.getSkin()==iVPlayerJumpL){
                                p1.setSpeed(1);
                                vitesse=1;
                                p1.setSkin(iVPlayerJump);
                                iVPlayerJump.setVisible(true);
                                iVPlayerJumpL.setVisible(false);
                            }
                        break;
                    case DOWN: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL
                            && p1.getSkin() != iVPlayerKick && p1.getSkin() != iVPlayerKickL){
                            if(p1.getSkin()==iVPlayer){
                                p1.setSkin(iVPlayerCrouch);
                                iVPlayerCrouch.setVisible(true);
                                iVPlayer.setVisible(false);
                            } else if(p1.getSkin()==iVPlayerL){
                                p1.setSkin(iVPlayerCrouchL);
                                iVPlayerCrouchL.setVisible(true);
                                iVPlayerL.setVisible(false);
                            }
                        }
                    break;
                    case UP: if(p1.getSkin()!=iVPlayerPunch && p1.getSkin()!= iVPlayerPunchL
                            && p1.getSkin() != iVPlayerKick && p1.getSkin() != iVPlayerKickL){
                            if(p1.getSkin()==iVPlayer){
                                p1.setSkin(iVPlayerJump2);
                                iVPlayerJump2.setVisible(true);
                                iVPlayer.setVisible(false);
                            } else if(p1.getSkin()==iVPlayerL){
                                p1.setSkin(iVPlayerJump2L);
                                iVPlayerJump2L.setVisible(true);
                                iVPlayerL.setVisible(false);
                            }
                        }
                    break;
                    case ESCAPE: primaryStage.setScene(pause);
                                 timeTemp = time;
                                 gameTimer.cancel();
                                 time = timeTemp;
                                 musicTime = gameMusic.getCurrentTime().toSeconds();
                                 gameMusic.stop();
                                 menuMusic.play();break;
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
                } else if(p1.getSkin()==iVPlayerCrouchPunch){
                    p1.setSkin(iVPlayerCrouch);
                    iVPlayerCrouchPunch.setVisible(false);
                    iVPlayerCrouch.setVisible(true);
                } else if(p1.getSkin()==iVPlayerCrouchPunchL){
                    p1.setSkin(iVPlayerCrouchL);
                    iVPlayerCrouchPunchL.setVisible(false);
                    iVPlayerCrouchL.setVisible(true);
                }
                p1.setSpeed(vitesse);
                punchSound.setOnEndOfMedia(() ->{
                   punchSound.stop();
                });
            }else if(keyCode.equals(KeyCode.Z)){
                if(p1.getSkin()==iVPlayerKick){
                    p1.setSkin(iVPlayer);
                    iVPlayer.setVisible(true);
                    iVPlayerKick.setVisible(false);
                } else if(p1.getSkin()==iVPlayerKickL){
                    p1.setSkin(iVPlayerL);
                    iVPlayerL.setVisible(true);
                    iVPlayerKickL.setVisible(false);
                } else if(p1.getSkin()==iVPlayerCrouchKick){
                    p1.setSkin(iVPlayerCrouch);
                    iVPlayerCrouchKick.setVisible(false);
                    iVPlayerCrouch.setVisible(true);
                } else if(p1.getSkin()==iVPlayerCrouchKickL){
                    p1.setSkin(iVPlayerCrouchL);
                    iVPlayerCrouchKickL.setVisible(false);
                    iVPlayerCrouchL.setVisible(true);
                }
                p1.setSpeed(vitesse);
                kickSound.setOnEndOfMedia(() ->{
                   kickSound.stop();
                });
            }else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p1.setSpeed(0);
                vitesse=0;
            }else if(keyCode.equals(KeyCode.DOWN)){
                if(p1.getSkin()==iVPlayerCrouch){
                    p1.setSkin(iVPlayer);
                    iVPlayerCrouch.setVisible(false);
                    iVPlayer.setVisible(true);
                } else if(p1.getSkin()==iVPlayerCrouchL){
                    p1.setSkin(iVPlayerL);
                    iVPlayerCrouchL.setVisible(false);
                    iVPlayerL.setVisible(true);
                }
            } else if(keyCode.equals(KeyCode.UP)){
                if(p1.getSkin()==iVPlayerJump2){
                    p1.setSkin(iVPlayerJump);
                    iVPlayerJump.setVisible(true);
                    iVPlayerJump2.setVisible(false);
                    p1.setSpeedY(-1);
                } else if(p1.getSkin()==iVPlayerJump2L){
                    p1.setSkin(iVPlayerJumpL);
                    iVPlayerJumpL.setVisible(true);
                    iVPlayerJump2L.setVisible(false);
                    p1.setSpeedY(-1);
                }
            }
        });
        
        
        /*
        * Gestion des fenetres du jeu
        */
        play.setOnAction((ActionEvent event) ->{
            validateButton.setDisable(false);
            primaryStage.setScene(scene);
            menuMusic.stop();
            gameMusic.play();
            score.setScore(0);
            time = 120;
            p1.setX(50);
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeText.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        menuMusic.play();
                        gameMusic.stop();
                        gameTimer.cancel();
                        time=120;
                        timeText.setText("Time : 0");
                        finalScore.setText("score : " + score.getScore() + " points");
                        replay.setVisible(true);
                        menu.setVisible(true);
                        finalScore.setVisible(true);
                        yourPseudo.setVisible(true);
                        validateButton.setVisible(true);
                        pseudoEntry.setVisible(true);
                        p1.getSkin().setVisible(false);
                        p1.setX(50);
                        root.getChildren().remove(iVPlayer);
                        root.getChildren().remove(iVPlayerL);
                        root.getChildren().remove(iVPlayerPunchL);
                        root.getChildren().remove(iVPlayerPunch);
                        iVTarget.setVisible(false);
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
        
        replay.setOnAction((ActionEvent event) ->{
            validateButton.setDisable(false);
            menuMusic.stop();
            gameMusic.play();
            time = 120;
            root.getChildren().add(iVPlayer);
            root.getChildren().add(iVPlayerL);
            root.getChildren().add(iVPlayerPunchL);
            root.getChildren().add(iVPlayerPunch);
            replay.setVisible(false);
            menu.setVisible(false);
            finalScore.setVisible(false);
            yourPseudo.setVisible(false);
            validateButton.setVisible(false);
            pseudoEntry.setVisible(false);
            p1.getSkin().setVisible(true);
            iVTarget.setVisible(true);
            score.setScore(0);
            p1.setX(50);
            p1.setSkin(iVPlayer);
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeText.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        menuMusic.play();
                        gameMusic.stop();
                        gameTimer.cancel();
                        time=120;
                        timeText.setText("Time : 0");
                        finalScore.setText("score : " + score.getScore() + " points");
                        replay.setVisible(true);
                        menu.setVisible(true);
                        finalScore.setVisible(true);
                        yourPseudo.setVisible(true);
                        validateButton.setVisible(true);
                        pseudoEntry.setVisible(true);
                        root.getChildren().remove(iVPlayer);
                        root.getChildren().remove(iVPlayerL);
                        root.getChildren().remove(iVPlayerPunchL);
                        root.getChildren().remove(iVPlayerPunch);
                        iVTarget.setVisible(false);
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        });
        
        menu.setOnAction((ActionEvent event) ->{
            validateButton.setDisable(false);
            gameMusic.stop();
            primaryStage.setScene(menuScene);
            replay.setVisible(false);
            menu.setVisible(false);
            finalScore.setVisible(false);
            yourPseudo.setVisible(false);
            validateButton.setVisible(false);
            pseudoEntry.setVisible(false);
            root.getChildren().add(iVPlayer);
            root.getChildren().add(iVPlayerL);
            root.getChildren().add(iVPlayerPunchL);
            root.getChildren().add(iVPlayerPunch);
            replay.setVisible(false);
            menu.setVisible(false);
            p1.getSkin().setVisible(true);
            iVTarget.setVisible(true);
            score.setScore(0);
        });
    
        
        primaryStage.setTitle("Random Fight V2");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }   
    
    @Override
    public void stop() throws SQLException{
        try{
        itsTimer.cancel();
        gameTimer.cancel();
        }catch(NullPointerException e){
            System.err.println("nullPointerException capturée ligne 611");
        }
        con.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            launch(args);
    }
    
}

