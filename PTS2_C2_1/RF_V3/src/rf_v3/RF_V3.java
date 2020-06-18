/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
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
 * @author ewen
 */
public class RF_V3 extends Application {
    
    Connection con = null;
    
    Timer itsTimer;
    Timer gameTimer;
    
    int time =120;
    int oldScene = 1;
    int gamePlay = 0;
    int target;
    int vitesse;
    int vitesse2;
    int timeTemp;
    int life = 10;
    int luckBoss =0;
    int map=1;
    
    double musicTime;
    double randomX;
    double targetPosX;
    
    Random rand = new Random();
    Random randTarget = new Random();
    
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        
        AnchorPane arcadePane = new AnchorPane();
        
        // =====================================================================
        // Connection à la base
        // =====================================================================
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
        
        
        
        // =====================================================================
        // Images des personnages
        // =====================================================================
        FileInputStream file = new FileInputStream("src/RF_V3/images/jamyCour.png");
        Image iPlayer = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyCourG.png");
        Image iPlayerL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyCoupPoing.png");
        Image iPlayerPunch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyCoupPoingG.png");
        Image iPlayerPunchL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyCoupPied.png");
        Image iPlayerKick = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyCoupPiedG.png");
        Image iPlayerKickL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupie.png");
        Image iPlayerCrouch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupieG.png");
        Image iPlayerCrouchL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamySaut.png");
        Image iPlayerJump = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamySautG.png");
        Image iPlayerJumpL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamySaut2.png");
        Image iPlayerJump2 = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamySaut2G.png");
        Image iPlayerJump2L = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupieCoupPied.png");
        Image iPlayerCrouchKick = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupieCoupPiedG.png");
        Image iPlayerCrouchKickL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupieCoupPoing.png");
        Image iPlayerCrouchPunch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/jamyAcroupieCoupPoingG.png");
        Image iPlayerCrouchPunchL = new Image(file,140,170,false,false);
        
        
        ImageView iVPlayer = new ImageView(iPlayer);
        iVPlayer.setPreserveRatio(true);
        iVPlayer.setFitWidth(110);
        
        ImageView iVPlayer2 = new ImageView(iPlayer);
        iVPlayer2.setPreserveRatio(true);
        iVPlayer2.setFitWidth(110);
        
        file = new FileInputStream("src/RF_V3/images/benderMouvement.png");
        Image iBender = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderMouvementG.png");
        Image iBenderL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderCoupPoing.png");
        Image iBenderPunch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderCoupPoingG.png");
        Image iBenderPunchL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderCoupPied.png");
        Image iBenderKick = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderCoupPiedG.png");
        Image iBenderKickL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupie.png");
        Image iBenderCrouch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupieG.png");
        Image iBenderCrouchL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderSaut.png");
        Image iBenderJump = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderSautG.png");
        Image iBenderJumpL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderSaut2.png");
        Image iBenderJump2 = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderSaut2G.png");
        Image iBenderJump2L = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupiePied.png");
        Image iBenderCrouchKick = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupiePiedG.png");
        Image iBenderCrouchKickL = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupiePoing.png");
        Image iBenderCrouchPunch = new Image(file,140,170,false,false);
        
        file = new FileInputStream("src/RF_V3/images/benderAcroupiePoingG.png");
        Image iBenderCrouchPunchL = new Image(file,140,170,false,false);
        
        ImageView iVPlayer3 = new ImageView(iBenderL);
        iVPlayer3.setPreserveRatio(true);
        iVPlayer3.setFitWidth(110);
        
        
        // =====================================================================
        // Images des ennemis
        // =====================================================================
        
        FileInputStream targetFile = new FileInputStream("src/RF_V3/images/ennemi.PNG");
        Image iTarget1 = new Image(targetFile, 150, 180, false, false);;
        
        FileInputStream targetFile2 = new FileInputStream("src/RF_V3/images/ennemiLVL2.PNG");
        Image iTarget2 = new Image(targetFile2, 150, 180, false, false);
        
        FileInputStream targetFile3 = new FileInputStream("src/RF_V3/images/ennemiLVL3.PNG");
        Image iTarget3 = new Image(targetFile3, 150, 180, false, false);
        
        FileInputStream bossFile = new FileInputStream("src/RF_V3/images/monster.PNG");
        Image iBoss = new Image(bossFile, 150, 180, false, false);
        
        ImageView iVTarget = new ImageView(iTarget1);
        iVTarget.setPreserveRatio(true);
        iVTarget.setFitWidth(100);

        
        
        // =====================================================================
        // Chargement des sons et musiques
        // =====================================================================
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
        
        
        
        // =====================================================================
        // Création du background du jeu
        // =====================================================================
  
        FileInputStream backgroundFile = new FileInputStream("src/RF_V3/images/background.png");
        Image background = new Image(backgroundFile);
        BackgroundImage background_img = new BackgroundImage(background,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG1 = new Background(background_img);
        
        backgroundFile = new FileInputStream("src/RF_V3/images/Map3.jpg");
        Image background2 = new Image(backgroundFile);
        BackgroundImage background_img2 = new BackgroundImage(background2,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG2 = new Background(background_img2);
        
        backgroundFile = new FileInputStream("src/RF_V3/images/Map4.png");
        Image background3 = new Image(backgroundFile);
        BackgroundImage background_img3 = new BackgroundImage(background3,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background BG3 = new Background(background_img3);
        
        
        FileInputStream menuBackgroundFile = new FileInputStream("src/RF_V3/images/menuBackground.png");
        Image menuBackground = new Image(menuBackgroundFile);
        BackgroundImage menuBackgroundImg = new BackgroundImage(menuBackground,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundRepeat.NO_REPEAT,
                                                            BackgroundPosition.CENTER,
                                                            BackgroundSize.DEFAULT);
        Background menuBG = new Background(menuBackgroundImg);
        
        arcadePane.setBackground(BG2);
        
        Scene arcadeScene = new Scene(arcadePane, background.getWidth(), background.getHeight());
        
        
        
        // =====================================================================
        //    Création des graphismes
        // =====================================================================
                
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
        
        
        
        
        // ======================================================================================
        // CREATION DES FENETRES
        
        // =====================================================================
        // Menu principal
        // =====================================================================
        
        AnchorPane menuPane = new AnchorPane();
        
       
        menuPane.setBackground(menuBG);
        
        Scene menuScene = new Scene(menuPane, 800, 450);
                
        FileInputStream titleMenu = new FileInputStream("src/RF_V3/images/Title.png");
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
        
        
        
        // =====================================================================
        // Selection mode de jeu
        // =====================================================================
        
        AnchorPane selectionPane = new AnchorPane();
        
        selectionPane.setBackground(menuBG);
        
        Scene selectionScene = new Scene(selectionPane, 800, 450);
        
        Text arcadeText = new Text("Arcade");
        arcadeText.setFill(Color.WHITE);
        arcadeText.setEffect(dropShadow);
        arcadeText.setFont(wallpoetBigger);
        Button playArcade = new Button();
        playArcade.setBackground(new Background(buttonBackground));
        playArcade.setGraphic(arcadeText);
        playArcade.setMinHeight(70);
        playArcade.setMinWidth(500);
        playArcade.setTranslateX(background.getWidth()/2 - 250);
        playArcade.setTranslateY(130);
        
        Text optionsMultiText = new Text("Multiplayer");
        optionsMultiText.setFill(Color.WHITE);
        optionsMultiText.setEffect(dropShadow);
        optionsMultiText.setFont(wallpoetBigger);
        Button playMultiOptions = new Button();
        playMultiOptions.setBackground(new Background(buttonBackground));
        playMultiOptions.setGraphic(optionsMultiText);
        playMultiOptions.setMinHeight(70);
        playMultiOptions.setMinWidth(500);
        playMultiOptions.setTranslateX(background.getWidth()/2 - 250);
        playMultiOptions.setTranslateY(230);
        
        
        selectionPane.getChildren().add(playArcade);
        selectionPane.getChildren().add(playMultiOptions);
        
        
        
        // =====================================================================
        // Menu Score
        // =====================================================================
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
            r.close();
            p.close();
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
        
        
        
        // =====================================================================
        // Menu paramètres
        // =====================================================================
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
       
        Text volumeSoundsText = new Text("Sounds volume");
        volumeSoundsText.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 30));
        volumeSoundsText.setFill(Color.WHITE);
        volumeSoundsText.setEffect(dropShadow);
        volumeSoundsText.setX(530);
        volumeSoundsText.setY(250);
        
        Slider volumeSoundsSlider = new Slider(0, 1, 0);
        volumeSoundsSlider.setEffect(dropShadow);
        volumeSoundsSlider.setTranslateX(550);
        volumeSoundsSlider.setTranslateY(275);
        volumeSoundsSlider.setValue(0.5);
        
        punchSound.volumeProperty().bindBidirectional(volumeSoundsSlider.valueProperty());
        kickSound.volumeProperty().bindBidirectional(volumeSoundsSlider.valueProperty());
        
        Text volumeSoundsPercent = new Text("50%");
        volumeSoundsPercent.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, FontPosture.REGULAR, 25));
        volumeSoundsPercent.setFill(Color.WHITE);
        volumeSoundsPercent.setEffect(dropShadow);
        volumeSoundsPercent.setX(600);
        volumeSoundsPercent.setY(325);
        
        volumeSoundsSlider.valueProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                volumeSoundsPercent.setText(Math.round(newValue.doubleValue()*100) + "%");
            }
        });
        
        AnchorPane settingsPane = new AnchorPane();
        settingsPane.setBackground(menuBG);
        
        settingsPane.getChildren().add(volumePercent);
        settingsPane.getChildren().add(volumeText);
        settingsPane.getChildren().add(volumeSlider);
        settingsPane.getChildren().add(volumeSoundsPercent);
        settingsPane.getChildren().add(volumeSoundsText);
        settingsPane.getChildren().add(volumeSoundsSlider);
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
        
        
        
        // =====================================================================
        // Jeu arcade
        // =====================================================================
        
        FileInputStream scoreBar = new FileInputStream("src/RF_V3/images/LaBarre.png");
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
        
        Text timeText = new Text();
        timeText.setX(background.getWidth()-160);
        timeText.setY(27);
        timeText.setFont(wallpoet);
        timeText.setFill(Color.WHITE);
        timeText.setEffect(dropShadow);
        
        arcadePane.getChildren().add(iVPlayer);
        arcadePane.getChildren().add(iVScoreBar);
        arcadePane.getChildren().add(textScore);
        arcadePane.getChildren().add(iVTarget);
        arcadePane.getChildren().add(timeText);
        
        Player p1 = new Player(iVPlayer,1,50, 250);
        
        
        
        
        // =====================================================================
        // Jeu multi options
        // =====================================================================
        AnchorPane multiOptionsPane = new AnchorPane();
        multiOptionsPane.setBackground(menuBG);
        
        Text tAddTime = new Text("plus");
        tAddTime.setFill(Color.WHITE);
        tAddTime.setEffect(dropShadow);
        tAddTime.setFont(wallpoet);
        Button addTime = new Button();
        addTime.setBackground(new Background(buttonBackground));
        addTime.setGraphic(tAddTime);
        addTime.setMinHeight(30);
        addTime.setMinWidth(30);
        addTime.setTranslateX(350);
        addTime.setTranslateY(100);
        
        Text tReduceTime = new Text("moins");
        tReduceTime.setFill(Color.WHITE);
        tReduceTime.setEffect(dropShadow);
        tReduceTime.setFont(wallpoet);
        Button reduceTime = new Button();
        reduceTime.setBackground(new Background(buttonBackground));
        reduceTime.setGraphic(tReduceTime);
        reduceTime.setMinHeight(30);
        reduceTime.setMinWidth(30);
        reduceTime.setTranslateX(80);
        reduceTime.setTranslateY(100);
        
        Text tTime = new Text("Temps :");
        tTime.setX(200);
        tTime.setY(50);
        tTime.setFont(wallpoet);
        tTime.setFill(Color.WHITE);
        tTime.setEffect(dropShadow);
        
        Text tTimeValue = new Text("120s");
        tTimeValue.setX(220);
        tTimeValue.setY(120);
        tTimeValue.setFont(wallpoet);
        tTimeValue.setFill(Color.WHITE);
        tTimeValue.setEffect(dropShadow);
        
        Text tAddLife = new Text("plus");
        tAddLife.setFill(Color.WHITE);
        tAddLife.setEffect(dropShadow);
        tAddLife.setFont(wallpoet);
        Button addLife = new Button();
        addLife.setBackground(new Background(buttonBackground));
        addLife.setGraphic(tAddLife);
        addLife.setMinHeight(30);
        addLife.setMinWidth(30);
        addLife.setTranslateX(350);
        addLife.setTranslateY(250);
        
        Text tReduceLife = new Text("moins");
        tReduceLife.setFill(Color.WHITE);
        tReduceLife.setEffect(dropShadow);
        tReduceLife.setFont(wallpoet);
        Button reduceLife = new Button();
        reduceLife.setBackground(new Background(buttonBackground));
        reduceLife.setGraphic(tReduceLife);
        reduceLife.setMinHeight(30);
        reduceLife.setMinWidth(30);
        reduceLife.setTranslateX(80);
        reduceLife.setTranslateY(250);
        
        Text tLife = new Text("points de vies :");
        tLife.setX(200);
        tLife.setY(200);
        tLife.setFont(wallpoet);
        tLife.setFill(Color.WHITE);
        tLife.setEffect(dropShadow);
        
        Text tLifeValue = new Text("10");
        tLifeValue.setX(220);
        tLifeValue.setY(270);
        tLifeValue.setFont(wallpoet);
        tLifeValue.setFill(Color.WHITE);
        tLifeValue.setEffect(dropShadow);
        
        Text tMapValue = new Text("map 1");
        tMapValue.setX(550);
        tMapValue.setY(80);
        tMapValue.setFont(wallpoet);
        tMapValue.setFill(Color.WHITE);
        tMapValue.setEffect(dropShadow);
        
        Text tMap1 = new Text("map 1");
        tMap1.setFill(Color.WHITE);
        tMap1.setEffect(dropShadow);
        tMap1.setFont(wallpoet);
        Button map1 = new Button();
        map1.setBackground(new Background(buttonBackground));
        map1.setGraphic(tMap1);
        map1.setMinHeight(30);
        map1.setMinWidth(40);
        map1.setTranslateX(450);
        map1.setTranslateY(130);
        
        Text tMap2 = new Text("map 2");
        tMap2.setFill(Color.WHITE);
        tMap2.setEffect(dropShadow);
        tMap2.setFont(wallpoet);
        Button map2 = new Button();
        map2.setBackground(new Background(buttonBackground));
        map2.setGraphic(tMap2);
        map2.setMinHeight(30);
        map2.setMinWidth(30);
        map2.setTranslateX(550);
        map2.setTranslateY(130);
        
        Text tMap3 = new Text("map 2");
        tMap3.setFill(Color.WHITE);
        tMap3.setEffect(dropShadow);
        tMap3.setFont(wallpoet);
        Button map3 = new Button();
        map3.setBackground(new Background(buttonBackground));
        map3.setGraphic(tMap3);
        map3.setMinHeight(30);
        map3.setMinWidth(30);
        map3.setTranslateX(660);
        map3.setTranslateY(130);
        
        Text multiText = new Text("Play");
        multiText.setFill(Color.WHITE);
        multiText.setEffect(dropShadow);
        multiText.setFont(wallpoetBigger);
        Button playMulti = new Button();
        playMulti.setBackground(new Background(buttonBackground));
        playMulti.setGraphic(multiText);
        playMulti.setMinHeight(70);
        playMulti.setMinWidth(500);
        playMulti.setTranslateX(130);
        playMulti.setTranslateY(350);
        
        multiOptionsPane.getChildren().add(addTime);
        multiOptionsPane.getChildren().add(reduceTime);
        multiOptionsPane.getChildren().add(tTime);
        multiOptionsPane.getChildren().add(tTimeValue);
        multiOptionsPane.getChildren().add(addLife);
        multiOptionsPane.getChildren().add(reduceLife);
        multiOptionsPane.getChildren().add(tLife);
        multiOptionsPane.getChildren().add(tLifeValue);
        multiOptionsPane.getChildren().add(playMulti);
        multiOptionsPane.getChildren().add(tMapValue);
        multiOptionsPane.getChildren().add(map1);
        multiOptionsPane.getChildren().add(map2);
        multiOptionsPane.getChildren().add(map3);
        
        Scene multiOptionsScene = new Scene(multiOptionsPane, 800, 450);
        
        
        
        // =====================================================================
        // Jeu multi
        // =====================================================================
        
        AnchorPane multiPane = new AnchorPane();
        multiPane.setBackground(BG1);
        
        Text tP2Life = new Text();
        tP2Life.setX(27);
        tP2Life.setY(27);
        tP2Life.setFont(wallpoet);
        tP2Life.setFill(Color.WHITE);
        tP2Life.setEffect(dropShadow);
        
        Text tP3Life = new Text();
        tP3Life.setX(background.getWidth()-150);
        tP3Life.setY(27);
        tP3Life.setFont(wallpoet);
        tP3Life.setFill(Color.WHITE);
        tP3Life.setEffect(dropShadow);
        
        Text timeTextMulti = new Text();
        timeTextMulti.setX(background.getWidth()-450);
        timeTextMulti.setY(27);
        timeTextMulti.setFont(wallpoet);
        timeTextMulti.setFill(Color.WHITE);
        timeTextMulti.setEffect(dropShadow);
        
        multiPane.getChildren().add(tP2Life);
        multiPane.getChildren().add(tP3Life);
        multiPane.getChildren().add(iVPlayer2);
        multiPane.getChildren().add(iVPlayer3);
        multiPane.getChildren().add(timeTextMulti);
        
        Scene multiScene = new Scene(multiPane, 800, 450);
        
        Player p2 = new Player(iVPlayer2,10, 250 ,250);
        Player p3 = new Player(iVPlayer3,10, 550 ,250);
        
        
        // =====================================================================
        // Fin de partie
        // =====================================================================
        
        Text replayText = new Text("Replay");
        replayText.setFill(Color.WHITE);
        replayText.setEffect(dropShadow);
        replayText.setFont(wallpoet);
        Button replay = new Button();
        replay.setBackground(new Background(buttonBackground));
        replay.setGraphic(replayText);
        replay.setMinHeight(50);
        replay.setMinWidth(120);
        replay.setTranslateX(335);
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
        menu.setTranslateX(335);
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
        
        Text tWinner = new Text();
        tWinner.setFont(wallpoet);
        tWinner.setFill(Color.WHITE);
        tWinner.setEffect(dropShadow);
        tWinner.setX(300);
        tWinner.setY(170);
        
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
        
        arcadePane.getChildren().add(yourPseudo);
        arcadePane.getChildren().add(validateButton);
        arcadePane.getChildren().add(pseudoEntry);
        arcadePane.getChildren().add(replay);
        arcadePane.getChildren().add(menu);
        arcadePane.getChildren().add(finalScore);
        multiPane.getChildren().add(menu);
        multiPane.getChildren().add(replay);
        multiPane.getChildren().add(tWinner);
        tWinner.setVisible(false);
        replay.setVisible(false);
        menu.setVisible(false);
        finalScore.setVisible(false);
        yourPseudo.setVisible(false);
        validateButton.setVisible(false);
        pseudoEntry.setVisible(false);
        
        StringProperty endGamePseudo = new SimpleStringProperty();
        
        //Limite du pseudo à 10 caractères
        pseudoEntry.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length() >10){
                    pseudoEntry.setText(oldValue);
                }
            }
            
        });
        
        // =====================================================================
        // Fenêtre pause
        // =====================================================================
        
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
        
        Scene pauseScene = new Scene(pausePane, 800, 450);
        
        
        
        // =========================================================================
        // ACTION DES BOUTONS DES MENUS
        // =====================================================================
        // Retour echap
        // =====================================================================
        
        scoreTable.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.ESCAPE){
                    primaryStage.setScene(menuScene);
                }
            }
            
        });
        
        settingsScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ESCAPE)){
                    primaryStage.setScene(menuScene);
                }
            }
            
        });
        
        selectionScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ESCAPE)){
                    primaryStage.setScene(menuScene);
                }
            }
            
        });
        
        multiOptionsScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ESCAPE)){
                    primaryStage.setScene(selectionScene);
                }
            }
            
        });
        
        
        // =====================================================================
        // bouton
        // =====================================================================
        
        scoreButton.setOnAction((ActionEvent event) ->{
            oldScene = 1;
            primaryStage.setScene(scoreTable);
        });
        
        settingsButton.setOnAction((ActionEvent event) ->{
            oldScene = 1;
            primaryStage.setScene(settingsScene);
        });
        
        
        pauseSettingsButton.setOnAction((ActionEvent event) -> {
            oldScene = 2;
            primaryStage.setScene(settingsScene);
        });
        
        returnMenuButton.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(menuScene);
        });
        
        exit.setOnAction((ActionEvent event) ->{
            primaryStage.close();
        });
        
        playMultiOptions.setOnAction((ActionEvent event) ->{
            oldScene=4;
            primaryStage.setScene(multiOptionsScene);
        });
        
        menu.setOnAction((ActionEvent event) ->{
            validateButton.setDisable(false);
            gameMusic.stop();
            replay.setVisible(false);
            menu.setVisible(false);
            finalScore.setVisible(false);
            yourPseudo.setVisible(false);
            validateButton.setVisible(false);
            pseudoEntry.setVisible(false);
            replay.setVisible(false);
            menu.setVisible(false);
            score.setScore(0);
            
            primaryStage.setScene(menuScene);
        });
        
        play.setOnAction((ActionEvent event) ->{
            primaryStage.setScene(selectionScene);
        });
        
        addTime.setOnAction((ActionEvent event) ->{
            if(time<200){
            time += 10;
            }
            tTimeValue.setText(String.valueOf(time));
        });
        
        reduceTime.setOnAction((ActionEvent event) ->{
            if(time>10){
            time -= 10;
            }
            tTimeValue.setText(String.valueOf(time));
        });
        
        addLife.setOnAction((ActionEvent event) ->{
            if(life<=24)
            life += 1;
            tLifeValue.setText(String.valueOf(life));
        });
        
        reduceLife.setOnAction((ActionEvent event) ->{
            if(life>1){
            life -= 1;
            }
            tLifeValue.setText(String.valueOf(life));
        });
        
        map1.setOnAction((ActionEvent event) ->{
            map =1;
            multiPane.setBackground(BG1);
            tMapValue.setText("map 1");
        });
        
        map2.setOnAction((ActionEvent event) ->{
            map =2;
            multiPane.setBackground(BG2);
            tMapValue.setText("map 2");
        });
        
        map3.setOnAction((ActionEvent event) ->{
            map =3;
            multiPane.setBackground(BG3);
            tMapValue.setText("map 3");
        });
        
        validateButton.setOnAction((ActionEvent event) -> {
            if(pseudoEntry.getText().length() >0){
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
            }else{
                Alert pseudoAlert = new Alert(Alert.AlertType.WARNING);
                pseudoAlert.setTitle("Pseudo warning !");
                pseudoAlert.setHeaderText("Empty pseudo");
                pseudoAlert.setContentText("You have to type a correct peudo ");
                pseudoAlert.showAndWait();
            }
            
        
            String[] bestsScores2 = new String[5];
            String[] bestsScorePseudo2 = new String[5];
        
            PreparedStatement pS2 = null;
            ResultSet r2 = null;
        
            int j = 0;
                
            try
            {
                pS2 = con.prepareStatement( sql );
                pS2.clearParameters();
            
                r2 = pS2.executeQuery();
            
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
                pS2.close();
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
        
        // =====================================================================
        //
        // =====================================================================
        
        playArcade.setOnAction((ActionEvent event) ->{
            gamePlay=1;
            validateButton.setDisable(false);
            primaryStage.setScene(arcadeScene);
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
                        arcadePane.getChildren().remove(iVPlayer);
                        iVTarget.setVisible(false);
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        });
        
        resume.setOnAction((ActionEvent event) ->{
            if(gamePlay==1){
            primaryStage.setScene(arcadeScene);
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
                        arcadePane.getChildren().remove(iVPlayer);
                        p1.getSkin().setVisible(false);
                    }
                });
            }
            
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        
        }else if(gamePlay==2){
            primaryStage.setScene(multiScene);
            menuMusic.stop();
            gameMusic.setStartTime(Duration.seconds(musicTime));
            gameMusic.play();
            gameMusic.setStartTime(Duration.seconds(0));
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeTextMulti.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        menuMusic.play();
                        gameMusic.stop();
                        gameTimer.cancel();
                        time=120;
                        timeTextMulti.setText("Time : 0");
                        menu.setVisible(true);
                        replay.setVisible(true);
                        tWinner.setVisible(true);
                            if(p2.getLife()>p3.getLife()){
                            tWinner.setText("joueur 1 gagne!");
                        } else if(p3.getLife()>p2.getLife()){
                            tWinner.setText("joueur 2 gagne!");
                        } else {
                            tWinner.setText("égalité!");
                        }
                            p2.getSkin().setVisible(false);
                            p3.getSkin().setVisible(false);
                    }
                });
            }
            
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        }
        });
        
        replay.setOnAction((ActionEvent event) ->{
            if(gamePlay==0){
            validateButton.setDisable(false);
            menuMusic.stop();
            gameMusic.play();
            time = 120;
            arcadePane.getChildren().add(iVPlayer);
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
                        arcadePane.getChildren().remove(iVPlayer);
                    }
                });
            }
            };
            gameTimer = new Timer();
            gameTimer.schedule(timerTask, 1000,1000);
            
            } else if(gamePlay==2){
                menuMusic.stop();
                gameMusic.play();
                menu.setVisible(false);
                replay.setVisible(false);
                tWinner.setVisible(false);
                p2.setX(50);
                iVPlayer2.setImage(iPlayer);
                iVPlayer2.setVisible(true);
                p3.setX(550);
                p2.setLife(life);
                p3.setLife(life);
                iVPlayer3.setImage(iBenderL);
                iVPlayer3.setVisible(true);
                TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() ->{
                        time--;
                        timeTextMulti.setText("Time : " + String.valueOf(time));
                        if(time <=0){
                            menuMusic.play();
                            gameMusic.stop();
                            gameTimer.cancel();
                            time=120;
                            timeTextMulti.setText("Time : 0");
                            replay.setVisible(true);
                            menu.setVisible(true);
                            tWinner.setVisible(true);
                            if(p2.getLife()>p3.getLife()){
                            tWinner.setText("joueur 1 gagne!");
                        } else if(p3.getLife()>p2.getLife()){
                            tWinner.setText("joueur 2 gagne!");
                        } else {
                            tWinner.setText("égalité!");
                        }
                            p2.getSkin().setVisible(false);
                            p3.getSkin().setVisible(false);
                        }
                    });
                }
            };
            gameTimer = new Timer();
            gameTimer.schedule(timerTask, 1000,1000);
            }
        });
        
        
        playMulti.setOnAction((ActionEvent event) ->{
            gamePlay=2;
            primaryStage.setScene(multiScene);
            menuMusic.stop();
            gameMusic.play();
            p2.setX(50);
            p3.setX(550);
            p2.setLife(life);
            p3.setLife(life);
            iVPlayer2.setImage(iPlayer);
            iVPlayer3.setImage(iBenderL);
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    time--;
                    timeTextMulti.setText("Time : " + String.valueOf(time));
                    if(time <=0){
                        menuMusic.play();
                        gameMusic.stop();
                        gameTimer.cancel();
                        time=120;
                        timeTextMulti.setText("Time : 0");
                        replay.setVisible(true);
                        menu.setVisible(true);
                        tWinner.setVisible(true);
                        pseudoEntry.setVisible(true);
                        p2.getSkin().setVisible(false);
                        p3.getSkin().setVisible(false);
                        if(p2.getLife()>p3.getLife()){
                            tWinner.setText("joueur 1 gagne!");
                        } else if(p3.getLife()>p2.getLife()){
                            tWinner.setText("joueur 2 gagne!");
                        } else {
                            tWinner.setText("égalité!");
                        }
                    }
                });
            }
        };
        gameTimer = new Timer();
        gameTimer.schedule(timerTask, 1000,1000);
        });
        
        
        // =====================================================================
        // Evenement du jeu
        // =====================================================================
        
            

            Target target1 = new Target(TypeTarget.SIMPLE , iVTarget , 25 , 1 , background.getWidth()-250, background.getHeight()-180);
            target1.setSpeed(1);
            
            TimerTask gameLoop = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    if(gamePlay==1){
                        if(!target1.isAlive()){
                        iVTarget.setFitWidth(100);
                        target1.setY(270);
                        }
                    p1.draw();
                    target1.draw();
                    if(!target1.isAlive()){
                        iVTarget.setFitWidth(100);
                        target1.setY(270);
                        do{
                        randomX = rand.nextInt(740 - 0 + 1) + 0;
                        targetPosX = randomX;
                        target = randTarget.nextInt((5-1)+1);
                        switch(target){
                            case 1: 
                                    target1.setLife(1);
                                    target1.setPoints(25);
                                    target1.setX(randomX);
                                    iVTarget.setImage(iTarget1);
                                    target1.setTypeTarget(TypeTarget.SIMPLE);
                                    break;
                            case 2: 
                                    target1.setLife(2);
                                    target1.setPoints(50);
                                    target1.setX(randomX);
                                    iVTarget.setImage(iTarget2);
                                    target1.setTypeTarget(TypeTarget.MEDIUM);
                                    break;
                            case 3: 
                                    target1.setLife(3);
                                    target1.setPoints(75);
                                    target1.setX(randomX);
                                    iVTarget.setImage(iTarget3);
                                    target1.setTypeTarget(TypeTarget.HARD);
                                    break;
                            case 4:
                                    luckBoss++;
                                    if(luckBoss==3){
                                    target1.setLife(10);
                                    target1.setPoints(250);
                                    target1.setX(randomX);
                                    iVTarget.setImage(iBoss);
                                    iVTarget.setFitWidth(200);
                                    target1.setY(150);
                                    target1.setTypeTarget(TypeTarget.BOSS);
                                    luckBoss=0;
                                    }
                                    break;
                            default:break;
                       }
                        /*try{
                            arcadePane.getChildren().add(iVTarget1);
                        }catch(IllegalArgumentException e){
                            System.out.println("IllegalArgumentException capturée ligne 218");
                        }*/
                        
                        }while( randomX >= p1.getX()-30 && randomX <=p1.getX()+140);
                    }
                    
                    if(target1.isAlive()){
                        if(target1.getX()>=targetPosX+50){
                            target1.setSpeed(-1);
                        } else if (target1.getX()<=targetPosX-50){
                            target1.setSpeed(1);
                        }
                    }
                    
                    
                    textScore.setText("Score : " + String.valueOf(score.getScore()));
                    if(target1.isAlive()){
                        p1.setX(target1.intersect(p1.getX(),p1.getY()));
                    }
                    
                    if(p1.getY()==250){
                        if(p1.getSkin().getImage()==iPlayerJump){
                            iVPlayer.setImage(iPlayer);
                        } else if(p1.getSkin().getImage()==iPlayerJumpL){
                            iVPlayer.setImage(iPlayerL);
                        }
                    }
                    } else if(gamePlay==2){
                        p2.draw();
                        p3.draw();
                        if(p2.getY()>=200 && p3.getSkin().getY()>=200){
                        p2.setX(p3.intersect(p2.getX(),p2.getY()));
                        p3.setX(p2.intersect(p3.getX(),p3.getY()));
                        }
                        tP2Life.setText("P1 : " + String.valueOf(p2.getLife()) + "pv");
                        tP3Life.setText("P2 : " + String.valueOf(p3.getLife()) + "pv");
                        if(p2.getY()>=250){
                            if(p2.getSkin().getImage()==iPlayerJump){
                                iVPlayer2.setImage(iPlayer);
                            } else if(p2.getSkin().getImage()==iPlayerJumpL){
                                iVPlayer2.setImage(iPlayerL);
                            }
                        }
                        if(p3.getY()>=250){
                            if(p3.getSkin().getImage()==iBenderJump){
                                iVPlayer3.setImage(iBender);
                            } else if(p3.getSkin().getImage()==iBenderJumpL){
                                iVPlayer3.setImage(iBenderL);
                            }
                        }
                        if(!p2.isAlive() || !p3.isAlive()){
                            time=0;
                        }
                    }
                });
            }
        };
        itsTimer = new Timer();
        itsTimer.schedule(gameLoop,1000, 1);
        
        arcadeScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                KeyCode keyCode = ke.getCode();
                switch(keyCode){
                    case A: if(p1.getSkin().getImage()== iPlayer){
                                iVPlayer.setImage(iPlayerPunch);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitLeft(p1.getSkin().getX())){
                                    punchSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()== iPlayerL){
                                iVPlayer.setImage(iPlayerPunchL);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitRight(p1.getSkin().getX())){
                                    punchSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()==iPlayerCrouch){
                                iVPlayer.setImage(iPlayerCrouchPunch);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitLeft(p1.getSkin().getX())){
                                    punchSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()==iPlayerCrouchL){
                                iVPlayer.setImage(iPlayerCrouchPunchL);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitRight(p1.getSkin().getX())){
                                    punchSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            }
                            
                                if(target1.getLifePoints() <= 0){
                                    score.addPoints(target1.getPoints());
                                }
                        break;
                    case Z: if(p1.getSkin().getImage()== iPlayer){
                                iVPlayer.setImage(iPlayerKick);
                                p1.setSpeed(0);
                                 if(target1.isAlive() && target1.hitLeft(p1.getSkin().getX())){
                                    kickSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()== iPlayerL){
                                iVPlayer.setImage(iPlayerKickL);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitRight(p1.getSkin().getX())){
                                    kickSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()== iPlayerCrouch){
                                iVPlayer.setImage(iPlayerCrouchKick);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitLeft(p1.getSkin().getX())){
                                    kickSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin().getImage()== iPlayerCrouchL){
                                iVPlayer.setImage(iPlayerCrouchKickL);
                                p1.setSpeed(0);
                                if(target1.isAlive() && target1.hitRight(p1.getSkin().getX())){
                                    kickSound.play();
                                    target1.hit(p1.getDamage());
                                }
                            }
                    
                                if(target1.getLifePoints() <= 0){
                                    score.addPoints(target1.getPoints());
                                }
                            break;
                    case LEFT: if(p1.getSkin().getImage()!=iPlayerPunch && p1.getSkin().getImage()!= iPlayerPunchL
                                    && p1.getSkin().getImage() != iPlayerKick && p1.getSkin().getImage() != iPlayerKickL 
                                    && p1.getSkin().getImage() != iPlayerCrouch && p1.getSkin().getImage()!= iPlayerCrouchL
                                    && p1.getSkin().getImage() != iPlayerJump && p1.getSkin().getImage() != iPlayerJumpL
                                    && p1.getSkin().getImage() != iPlayerCrouchPunch && p1.getSkin().getImage()!=iPlayerCrouchPunchL
                                    && p1.getSkin().getImage() !=iPlayerCrouchKick && p1.getSkin().getImage() !=iPlayerCrouchKickL){
                                        iVPlayer.setImage(iPlayerL);
                                        p1.setSpeed(-1);
                                        vitesse=-1;
                                } else if(p1.getSkin().getImage() == iPlayerCrouch || p1.getSkin().getImage()==iPlayerCrouchL){
                                        p1.setSpeed(-1);
                                        vitesse=-1;
                                } else if(p1.getSkin().getImage()== iPlayerJump || p1.getSkin().getImage()==iPlayerJumpL){
                                        p1.setSpeed(-1);
                                        vitesse=-1;
                                        iVPlayer.setImage(iPlayerJumpL);
                                }
                        break;
                    case RIGHT: if(p1.getSkin().getImage()!=iPlayerPunch && p1.getSkin().getImage()!= iPlayerPunchL
                                    && p1.getSkin().getImage() != iPlayerKick && p1.getSkin().getImage() != iPlayerKickL 
                                    && p1.getSkin().getImage() != iPlayerCrouch && p1.getSkin().getImage()!= iPlayerCrouchL
                                    && p1.getSkin().getImage() != iPlayerJump && p1.getSkin().getImage() != iPlayerJumpL
                                    && p1.getSkin().getImage() != iPlayerCrouchPunch && p1.getSkin().getImage()!=iPlayerCrouchPunchL
                                    && p1.getSkin().getImage() !=iPlayerCrouchKick && p1.getSkin().getImage() !=iPlayerCrouchKickL){
                                        iVPlayer.setImage(iPlayer);
                                        p1.setSpeed(1);
                                        vitesse=1;
                                } else if(p1.getSkin().getImage() == iPlayerCrouch || p1.getSkin().getImage()==iPlayerCrouchL){
                                        p1.setSpeed(1);
                                        vitesse=1;
                                } else if(p1.getSkin().getImage()== iPlayerJump || p1.getSkin().getImage()==iPlayerJumpL){
                                p1.setSpeed(1);
                                vitesse=1;
                                iVPlayer.setImage(iPlayerJump);
                            }
                        break;
                    case DOWN: if(p1.getSkin().getImage()!=iPlayerPunch && p1.getSkin().getImage()!= iPlayerPunchL
                            && p1.getSkin().getImage() != iPlayerKick && p1.getSkin().getImage() != iPlayerKickL){
                            if(p1.getSkin().getImage()==iPlayer){
                                iVPlayer.setImage(iPlayerCrouch);
                            } else if(p1.getSkin().getImage()==iPlayerL){
                                iVPlayer.setImage(iPlayerCrouchL);
                            }
                        }
                    break;
                    case UP: if(p1.getSkin().getImage()!=iPlayerPunch && p1.getSkin().getImage()!= iPlayerPunchL
                            && p1.getSkin().getImage() != iPlayerKick && p1.getSkin().getImage() != iPlayerKickL){
                            if(p1.getSkin().getImage()==iPlayer){
                                iVPlayer.setImage(iPlayerJump2);
                            } else if(p1.getSkin().getImage()==iPlayerL){
                                iVPlayer.setImage(iPlayerJump2L);
                            }
                        }
                    break;
                    case ESCAPE: primaryStage.setScene(pauseScene);
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
        
        arcadeScene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if(keyCode.equals(KeyCode.A)){
                if(p1.getSkin().getImage()==iPlayerPunch){
                    iVPlayer.setImage(iPlayer);
                } else if(p1.getSkin().getImage()==iPlayerPunchL){
                    iVPlayer.setImage(iPlayerL);
                } else if(p1.getSkin().getImage()==iPlayerCrouchPunch){
                    iVPlayer.setImage(iPlayerCrouch);
                } else if(p1.getSkin().getImage()==iPlayerCrouchPunchL){
                    iVPlayer.setImage(iPlayerCrouchL);
                }
                p1.setSpeed(vitesse);
                punchSound.setOnEndOfMedia(() ->{
                   punchSound.stop();
                });
            }else if(keyCode.equals(KeyCode.Z)){
                if(p1.getSkin().getImage()==iPlayerKick){
                    iVPlayer.setImage(iPlayer);
                } else if(p1.getSkin().getImage()==iPlayerKickL){
                    iVPlayer.setImage(iPlayerL);
                } else if(p1.getSkin().getImage()==iPlayerCrouchKick){
                    iVPlayer.setImage(iPlayerCrouch);
                } else if(p1.getSkin().getImage()==iPlayerCrouchKickL){
                    iVPlayer.setImage(iPlayerCrouchL);
                }
                p1.setSpeed(vitesse);
                kickSound.setOnEndOfMedia(() ->{
                   kickSound.stop();
                });
            }else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p1.setSpeed(0);
                vitesse=0;
            }else if(keyCode.equals(KeyCode.DOWN)){
                if(p1.getSkin().getImage()==iPlayerCrouch){
                    iVPlayer.setImage(iPlayer);
                } else if(p1.getSkin().getImage()==iPlayerCrouchL){
                    iVPlayer.setImage(iPlayerL);
                }
            } else if(keyCode.equals(KeyCode.UP)){
                if(p1.getSkin().getImage()==iPlayerJump2){
                    iVPlayer.setImage(iPlayerJump);
                    p1.setSpeedY(-1);
                } else if(p1.getSkin().getImage()==iPlayerJump2L){
                    iVPlayer.setImage(iPlayerJumpL);
                    p1.setSpeedY(-1);
                }
            }
        
        });
        
        
        
         multiScene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke){
                KeyCode keyCode = ke.getCode();
                switch(keyCode){
                    case Z: if(p2.getSkin().getImage()== iPlayer){
                                iVPlayer2.setImage(iPlayerPunch);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitLeft(p2.getSkin().getX())){
                                    punchSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()== iPlayerL){
                                iVPlayer2.setImage(iPlayerPunchL);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitRight(p2.getSkin().getX())){
                                    punchSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()==iPlayerCrouch){
                                iVPlayer2.setImage(iPlayerCrouchPunch);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitLeft(p2.getSkin().getX())){
                                    punchSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()==iPlayerCrouchL){
                                iVPlayer2.setImage(iPlayerCrouchPunchL);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitRight(p2.getSkin().getX())){
                                    punchSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            }
                            
                        break;
                    case S: if(p2.getSkin().getImage()== iPlayer){
                                iVPlayer2.setImage(iPlayerKick);
                                p2.setSpeed(0);
                                 if(p3.isAlive() && p3.hitLeft(p2.getSkin().getX())){
                                    kickSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()== iPlayerL){
                                iVPlayer2.setImage(iPlayerKickL);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitRight(p2.getSkin().getX())){
                                    kickSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()== iPlayerCrouch){
                                iVPlayer2.setImage(iPlayerCrouchKick);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitLeft(p2.getSkin().getX())){
                                    kickSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            } else if(p2.getSkin().getImage()== iPlayerCrouchL){
                                iVPlayer2.setImage(iPlayerCrouchKickL);
                                p2.setSpeed(0);
                                if(p3.isAlive() && p3.hitRight(p2.getSkin().getX())){
                                    kickSound.play();
                                    p3.hit(p2.getDamage());
                                }
                            }
                            break;
                    case Q: if(p2.getSkin().getImage()!=iPlayerPunch && p2.getSkin().getImage()!= iPlayerPunchL
                                    && p2.getSkin().getImage() != iPlayerKick && p2.getSkin().getImage() != iPlayerKickL 
                                    && p2.getSkin().getImage() != iPlayerCrouch && p2.getSkin().getImage()!= iPlayerCrouchL
                                    && p2.getSkin().getImage() != iPlayerJump && p2.getSkin().getImage() != iPlayerJumpL
                                    && p2.getSkin().getImage() != iPlayerCrouchPunch && p2.getSkin().getImage()!=iPlayerCrouchPunchL
                                    && p2.getSkin().getImage() !=iPlayerCrouchKick && p2.getSkin().getImage() !=iPlayerCrouchKickL){
                                        iVPlayer2.setImage(iPlayerL);
                                        p2.setSpeed(-1);
                                        vitesse=-1;
                                } else if(p2.getSkin().getImage() == iPlayerCrouch || p2.getSkin().getImage()==iPlayerCrouchL){
                                        p2.setSpeed(-1);
                                        vitesse=-1;
                                } else if(p2.getSkin().getImage()== iPlayerJump || p2.getSkin().getImage()==iPlayerJumpL){
                                        p2.setSpeed(-1);
                                        vitesse=-1;
                                        iVPlayer2.setImage(iPlayerJumpL);
                                }
                        break;
                    case D: if(p2.getSkin().getImage()!=iPlayerPunch && p2.getSkin().getImage()!= iPlayerPunchL
                                    && p2.getSkin().getImage() != iPlayerKick && p2.getSkin().getImage() != iPlayerKickL 
                                    && p2.getSkin().getImage() != iPlayerCrouch && p2.getSkin().getImage()!= iPlayerCrouchL
                                    && p2.getSkin().getImage() != iPlayerJump && p2.getSkin().getImage() != iPlayerJumpL
                                    && p2.getSkin().getImage() != iPlayerCrouchPunch && p2.getSkin().getImage()!=iPlayerCrouchPunchL
                                    && p2.getSkin().getImage() !=iPlayerCrouchKick && p2.getSkin().getImage() !=iPlayerCrouchKickL){
                                        iVPlayer2.setImage(iPlayer);
                                        p2.setSpeed(1);
                                        vitesse=1;
                                } else if(p2.getSkin().getImage() == iPlayerCrouch || p2.getSkin().getImage()==iPlayerCrouchL){
                                        p2.setSpeed(1);
                                        vitesse=1;
                                } else if(p2.getSkin().getImage()== iPlayerJump || p2.getSkin().getImage()==iPlayerJumpL){
                                p2.setSpeed(1);
                                vitesse=1;
                                iVPlayer2.setImage(iPlayerJump);
                            }
                        break;
                    case SHIFT: if(p2.getSkin().getImage()!=iPlayerPunch && p2.getSkin().getImage()!= iPlayerPunchL
                            && p2.getSkin().getImage() != iPlayerKick && p2.getSkin().getImage() != iPlayerKickL){
                            if(p2.getSkin().getImage()==iPlayer){
                                iVPlayer2.setImage(iPlayerCrouch);
                            } else if(p2.getSkin().getImage()==iPlayerL){
                                iVPlayer2.setImage(iPlayerCrouchL);
                            }
                        }
                    break;
                    case SPACE: if(p2.getSkin().getImage()!=iPlayerPunch && p2.getSkin().getImage()!= iPlayerPunchL
                            && p2.getSkin().getImage() != iPlayerKick && p2.getSkin().getImage() != iPlayerKickL){
                            if(p2.getSkin().getImage()==iPlayer){
                                iVPlayer2.setImage(iPlayerJump2);
                            } else if(p2.getSkin().getImage()==iPlayerL){
                                iVPlayer2.setImage(iPlayerJump2L);
                            }
                        }
                    break;
                    
                    case NUMPAD0: if(p3.getSkin().getImage()== iBender){
                                iVPlayer3.setImage(iBenderPunch);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitLeft(p3.getSkin().getX())){
                                    punchSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()== iBenderL){
                                iVPlayer3.setImage(iBenderPunchL);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitRight(p3.getSkin().getX())){
                                    punchSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()==iBenderCrouch){
                                iVPlayer3.setImage(iBenderCrouchPunch);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitLeft(p3.getSkin().getX())){
                                    punchSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()==iBenderCrouchL){
                                iVPlayer3.setImage(iBenderCrouchPunchL);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitRight(p3.getSkin().getX())){
                                    punchSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            }
                            
                        break;
                    case NUMPAD2: if(p3.getSkin().getImage()== iBender){
                                iVPlayer3.setImage(iBenderKick);
                                p3.setSpeed(0);
                                 if(p2.isAlive() && p2.hitLeft(p3.getSkin().getX())){
                                    kickSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()== iBenderL){
                                iVPlayer3.setImage(iBenderKickL);
                                p2.setSpeed(0);
                                if(p2.isAlive() && p2.hitRight(p3.getSkin().getX())){
                                    kickSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()== iBenderCrouch){
                                iVPlayer3.setImage(iBenderCrouchKick);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitLeft(p3.getSkin().getX())){
                                    kickSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            } else if(p3.getSkin().getImage()== iBenderCrouchL){
                                iVPlayer3.setImage(iBenderCrouchKickL);
                                p3.setSpeed(0);
                                if(p2.isAlive() && p2.hitRight(p3.getSkin().getX())){
                                    kickSound.play();
                                    p2.hit(p3.getDamage());
                                }
                            }
                            break;
                    case LEFT: if(p3.getSkin().getImage()!=iBenderPunch && p3.getSkin().getImage()!= iBenderPunchL
                                    && p3.getSkin().getImage() != iBenderKick && p3.getSkin().getImage() != iBenderKickL 
                                    && p3.getSkin().getImage() != iBenderCrouch && p3.getSkin().getImage()!= iBenderCrouchL
                                    && p3.getSkin().getImage() != iBenderJump && p3.getSkin().getImage() != iBenderJumpL
                                    && p3.getSkin().getImage() != iBenderCrouchPunch && p3.getSkin().getImage()!=iBenderCrouchPunchL
                                    && p3.getSkin().getImage() !=iBenderCrouchKick && p3.getSkin().getImage() !=iBenderCrouchKickL){
                                        iVPlayer3.setImage(iBenderL);
                                        p3.setSpeed(-1);
                                        vitesse2=-1;
                                } else if(p3.getSkin().getImage() == iBenderCrouch || p3.getSkin().getImage()==iBenderCrouchL){
                                        p3.setSpeed(-1);
                                        vitesse2=-1;
                                } else if(p3.getSkin().getImage()== iBenderJump || p3.getSkin().getImage()==iBenderJumpL){
                                        p3.setSpeed(-1);
                                        vitesse2=-1;
                                        iVPlayer3.setImage(iBenderJumpL);
                                }
                        break;
                    case RIGHT: if(p3.getSkin().getImage()!=iBenderPunch && p3.getSkin().getImage()!= iBenderPunchL
                                    && p3.getSkin().getImage() != iBenderKick && p3.getSkin().getImage() != iBenderKickL 
                                    && p3.getSkin().getImage() != iBenderCrouch && p3.getSkin().getImage()!= iBenderCrouchL
                                    && p3.getSkin().getImage() != iBenderJump && p3.getSkin().getImage() != iBenderJumpL
                                    && p3.getSkin().getImage() != iBenderCrouchPunch && p3.getSkin().getImage()!=iBenderCrouchPunchL
                                    && p3.getSkin().getImage() !=iBenderCrouchKick && p3.getSkin().getImage() !=iBenderCrouchKickL){
                                        iVPlayer3.setImage(iBender);
                                        p3.setSpeed(1);
                                        vitesse2=1;
                                } else if(p3.getSkin().getImage() == iBenderCrouch || p3.getSkin().getImage()==iBenderCrouchL){
                                        p3.setSpeed(1);
                                        vitesse2=1;
                                } else if(p3.getSkin().getImage()== iBenderJump || p3.getSkin().getImage()==iBenderJumpL){
                                        p3.setSpeed(1);
                                        vitesse2=1;
                                        iVPlayer3.setImage(iBenderJump);
                            }
                        break;
                    case DOWN: if(p3.getSkin().getImage()!=iBenderPunch && p3.getSkin().getImage()!= iBenderPunchL
                            && p3.getSkin().getImage() != iBenderKick && p3.getSkin().getImage() != iBenderKickL){
                            if(p3.getSkin().getImage()==iBender){
                                iVPlayer3.setImage(iBenderCrouch);
                            } else if(p3.getSkin().getImage()==iBenderL){
                                iVPlayer3.setImage(iBenderCrouchL);
                            }
                        }
                    break;
                    case UP: if(p3.getSkin().getImage()!=iBenderPunch && p3.getSkin().getImage()!= iBenderPunchL
                            && p3.getSkin().getImage() != iBenderKick && p3.getSkin().getImage() != iBenderKickL){
                            if(p3.getSkin().getImage()==iBender){
                                iVPlayer3.setImage(iBenderJump2);
                            } else if(p3.getSkin().getImage()==iBenderL){
                                iVPlayer3.setImage(iBenderJump2L);
                            }
                        }
                    break;
                    case ESCAPE: primaryStage.setScene(pauseScene);
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
         
         
        multiScene.setOnKeyReleased(ke -> {
            KeyCode keyCode = ke.getCode();
            if(keyCode.equals(KeyCode.Z)){
                if(p2.getSkin().getImage()==iPlayerPunch){
                    iVPlayer2.setImage(iPlayer);
                } else if(p2.getSkin().getImage()==iPlayerPunchL){
                    iVPlayer2.setImage(iPlayerL);
                } else if(p2.getSkin().getImage()==iPlayerCrouchPunch){
                    iVPlayer2.setImage(iPlayerCrouch);
                } else if(p2.getSkin().getImage()==iPlayerCrouchPunchL){
                    iVPlayer2.setImage(iPlayerCrouchL);
                }
                p2.setSpeed(vitesse);
                punchSound.setOnEndOfMedia(() ->{
                   punchSound.stop();
                });
            }else if(keyCode.equals(KeyCode.S)){
                if(p2.getSkin().getImage()==iPlayerKick){
                    iVPlayer2.setImage(iPlayer);
                } else if(p2.getSkin().getImage()==iPlayerKickL){
                    iVPlayer2.setImage(iPlayerL);
                } else if(p2.getSkin().getImage()==iPlayerCrouchKick){
                    iVPlayer2.setImage(iPlayerCrouch);
                } else if(p2.getSkin().getImage()==iPlayerCrouchKickL){
                    iVPlayer2.setImage(iPlayerCrouchL);
                }
                p2.setSpeed(vitesse);
                kickSound.setOnEndOfMedia(() ->{
                   kickSound.stop();
                });
            }else if(keyCode.equals(KeyCode.Q) || keyCode.equals(KeyCode.D)){
                p2.setSpeed(0);
                vitesse=0;
            }else if(keyCode.equals(KeyCode.SHIFT)){
                if(p2.getSkin().getImage()==iPlayerCrouch){
                    iVPlayer2.setImage(iPlayer);
                } else if(p2.getSkin().getImage()==iPlayerCrouchL){
                    iVPlayer2.setImage(iPlayerL);
                }
            } else if(keyCode.equals(KeyCode.SPACE)){
                if(p2.getSkin().getImage()==iPlayerJump2){
                    iVPlayer2.setImage(iPlayerJump);
                    p2.setSpeedY(-1);
                } else if(p2.getSkin().getImage()==iPlayerJump2L){
                    iVPlayer2.setImage(iPlayerJumpL);
                    p2.setSpeedY(-1);
                }
            } else if(keyCode.equals(KeyCode.NUMPAD0)){
                if(p3.getSkin().getImage()==iBenderPunch){
                    iVPlayer3.setImage(iBender);
                } else if(p3.getSkin().getImage()==iBenderPunchL){
                    iVPlayer3.setImage(iBenderL);
                } else if(p3.getSkin().getImage()==iBenderCrouchPunch){
                    iVPlayer3.setImage(iBenderCrouch);
                } else if(p3.getSkin().getImage()==iBenderCrouchPunchL){
                    iVPlayer3.setImage(iBenderCrouchL);
                }
                p3.setSpeed(vitesse2);
                punchSound.setOnEndOfMedia(() ->{
                   punchSound.stop();
                });
            }else if(keyCode.equals(KeyCode.NUMPAD2)){
                if(p3.getSkin().getImage()==iBenderKick){
                    iVPlayer3.setImage(iBender);
                } else if(p3.getSkin().getImage()==iBenderKickL){
                    iVPlayer3.setImage(iBenderL);
                } else if(p3.getSkin().getImage()==iBenderCrouchKick){
                    iVPlayer3.setImage(iBenderCrouch);
                } else if(p3.getSkin().getImage()==iBenderCrouchKickL){
                    iVPlayer3.setImage(iBenderCrouchL);
                }
                p3.setSpeed(vitesse2);
                kickSound.setOnEndOfMedia(() ->{
                   kickSound.stop();
                });
            }else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p3.setSpeed(0);
                vitesse2=0;
            }else if(keyCode.equals(KeyCode.DOWN)){
                if(p3.getSkin().getImage()==iBenderCrouch){
                    iVPlayer3.setImage(iBender);
                } else if(p3.getSkin().getImage()==iBenderCrouchL){
                    iVPlayer3.setImage(iBenderL);
                }
            } else if(keyCode.equals(KeyCode.UP)){
                if(p3.getSkin().getImage()==iBenderJump2){
                    iVPlayer3.setImage(iBenderJump);
                    p3.setSpeedY(-1);
                } else if(p3.getSkin().getImage()==iBenderJump2L){
                    iVPlayer3.setImage(iBenderJumpL);
                    p3.setSpeedY(-1);
                }
            }
        
        });

        
        
        
        
        
        
        primaryStage.getIcons().add(new Image(RF_V3.class.getResourceAsStream("images/icon.png")));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Random Fight V3");
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
