/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rf_v2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.image.Image;
import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
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
    
    boolean played = false;
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        AnchorPane root = new AnchorPane();
        
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
        
        Player p1 = new Player(iVPlayer,50, background.getHeight()-200);
        
        /*
        * Création d'une cible
        */
        FileInputStream targetFile = new FileInputStream("src/RF_V2/images/ennemi.PNG");
        Image targetImg = new Image(targetFile, 150, 180, false, false);
        ImageView iVTarget = new ImageView(targetImg);
        iVTarget.setPreserveRatio(true);
        iVTarget.setFitWidth(90);
        
        Target target = new Target(iVTarget, 50, 2, background.getWidth()-250, background.getHeight()-180);
        
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
                        targetLife = randTargetLife.nextInt((4-1)+1);
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
        playText.setEffect(dropShadow);
        playText.setFill(Color.WHITE);
        playText.setFont(wallpoetBigger);
        Button play = new Button();
        play.setBackground(new Background(buttonBackground));
        play.setGraphic(playText);
        play.setMinHeight(70);
        play.setMinWidth(500);
        play.setTranslateX(background.getWidth()/2 - 250);
        play.setTranslateY(120);
        
        Text scoreText = new Text("Scores");
        scoreText.setEffect(dropShadow);
        scoreText.setFill(Color.WHITE);
        scoreText.setFont(wallpoetBigger);
        Button scoreButton = new Button();
        scoreButton.setBackground(new Background(buttonBackground));
        scoreButton.setGraphic(scoreText);
        scoreButton.setMinHeight(70);
        scoreButton.setMinWidth(500);
        scoreButton.setTranslateX(background.getWidth()/2 - 250);
        scoreButton.setTranslateY(200);
        
        Text settingsText = new Text("Settings");
        settingsText.setEffect(dropShadow);
        settingsText.setFill(Color.WHITE);
        settingsText.setFont(wallpoetBigger);
        Button settingsButton = new Button();
        settingsButton.setBackground(new Background(buttonBackground));
        settingsButton.setGraphic(settingsText);
        settingsButton.setMinHeight(70);
        settingsButton.setMinWidth(500);
        settingsButton.setTranslateX(background.getWidth()/2 - 250);
        settingsButton.setTranslateY(280);
        
        Text exitText = new Text("Exit");
        exitText.setEffect(dropShadow);
        exitText.setFill(Color.WHITE);
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
        
        Text mainMenu = new Text("Return");
        mainMenu.setEffect(dropShadow);
        mainMenu.setFill(Color.WHITE);
        mainMenu.setFont(wallpoet);
        Button mainMenuButton = new Button();
        mainMenuButton.setBackground(new Background(buttonBackground));
        mainMenuButton.setGraphic(mainMenu);
        mainMenuButton.setMinHeight(50);
        mainMenuButton.setMinWidth(100);
        mainMenuButton.setTranslateX(10);
        mainMenuButton.setTranslateY(10);
        
        mainMenuButton.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(menuScene);
        });
        
        AnchorPane settingsPane = new AnchorPane();
        settingsPane.setBackground(menuBG);
        
        settingsPane.getChildren().add(mainMenuButton);
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
        root.getChildren().add(iVScoreBar);
        root.getChildren().add(textScore);
        root.getChildren().add(iVTarget);
        root.getChildren().add(timeText);
        iVPlayerKick.setVisible(false);
        iVPlayerKickL.setVisible(false);
        iVPlayerL.setVisible(false);
        iVPlayerPunch.setVisible(false);
        iVPlayerPunchL.setVisible(false);
        scene.setFill(Color.GREEN);
        
        /*
        * Fin de partie
        */
        Text replayText = new Text("replay");
        replayText.setEffect(dropShadow);
        replayText.setFill(Color.WHITE);
        replayText.setFont(wallpoet);
        
        Button replay = new Button();
        replay.setBackground(new Background(buttonBackground));
        replay.setGraphic(replayText);
        replay.setMinHeight(50);
        replay.setMinWidth(120);
        replay.setTranslateX(350);
        replay.setTranslateY(150);
        
        Text menuText = new Text("Menu");
        menuText.setEffect(dropShadow);
        menuText.setFill(Color.WHITE);
        menuText.setFont(wallpoet);
        
        Button menu = new Button();
        menu.setBackground(new Background(buttonBackground));
        menu.setGraphic(menuText);
        menu.setMinHeight(50);
        menu.setMinWidth(120);
        menu.setTranslateX(350);
        menu.setTranslateY(250);
        
        Text finalScore = new Text();
        finalScore.setFont(wallpoetBigger);
        finalScore.setFill(Color.WHITE);
        finalScore.setEffect(dropShadow);
        finalScore.setX(170);
        finalScore.setY(100);
        
        root.getChildren().add(replay);
        root.getChildren().add(menu);
        root.getChildren().add(finalScore);
        replay.setVisible(false);
        menu.setVisible(false);
        finalScore.setVisible(false);
        
        /*
        *Fenêtre pause
        */
        AnchorPane pausePane = new AnchorPane();
        pausePane.setBackground(menuBG);
        
        Text continueText = new Text("Resume");
        continueText.setEffect(dropShadow);
        continueText.setFill(Color.WHITE);
        continueText.setFont(wallpoetBigger);
        Button resume = new Button();
        resume.setBackground(new Background(buttonBackground));
        resume.setGraphic(continueText);
        resume.setMinHeight(70);
        resume.setMinWidth(300);
        resume.setTranslateX(250);
        resume.setTranslateY(100);
        
        Text pauseSettings = new Text("Settings");
        pauseSettings.setEffect(dropShadow);
        pauseSettings.setFill(Color.WHITE);
        pauseSettings.setFont(wallpoetBigger);
        Button pauseSettingsButton = new Button();
        pauseSettingsButton.setBackground(new Background(buttonBackground));
        pauseSettingsButton.setGraphic(pauseSettings);
        pauseSettingsButton.setMinHeight(70);
        pauseSettingsButton.setMinWidth(300);
        pauseSettingsButton.setTranslateX(250);
        pauseSettingsButton.setTranslateY(200);
        
        Text mainMenuText = new Text("Menu");
        mainMenuText.setEffect(dropShadow);
        mainMenuText.setFill(Color.WHITE);
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
                                if(iVPlayer.getX() >= iVTarget.getX()-(iVTarget.getFitWidth())
                                        && iVPlayer.getX() <= iVTarget.getX()+iVTarget.getFitWidth()){
                                    punchSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerL){
                                p1.setSkin(iVPlayerPunchL);
                                iVPlayerPunchL.setVisible(true);
                                iVPlayerL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerL.getX() <= iVTarget.getX()+(iVTarget.getFitWidth())
                                        && iVPlayerL.getX() >= iVTarget.getX()-iVTarget.getFitWidth()){
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
                                if(iVPlayer.getX() >= iVTarget.getX()-(iVTarget.getFitWidth())
                                        && iVPlayer.getX() <= iVTarget.getX()+iVTarget.getFitWidth()){
                                    kickSound.play();
                                    target.hit(p1.getDamage());
                                }
                            } else if(p1.getSkin()== iVPlayerL){
                                p1.setSkin(iVPlayerKickL);
                                iVPlayerKickL.setVisible(true);
                                iVPlayerL.setVisible(false);
                                p1.setSpeed(0);
                                if(iVPlayerL.getX() <= iVTarget.getX()+(iVTarget.getFitWidth())
                                        && iVPlayerL.getX() >= iVTarget.getX()-iVTarget.getFitWidth()){
                                    kickSound.play();
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
                }
                p1.setSpeed(vitesse);
                kickSound.setOnEndOfMedia(() ->{
                   kickSound.stop();
                });
            }else if(keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.RIGHT)){
                p1.setSpeed(0);
                vitesse=0;
            }
        });
        
        
        /*
        * Gestion des fenetres du jeu
        */
        play.setOnAction((ActionEvent event) ->{
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
        
        
        exit.setOnAction((ActionEvent event) ->{
            primaryStage.close();
        });
        
        replay.setOnAction((ActionEvent event) ->{
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
            gameMusic.stop();
            primaryStage.setScene(menuScene);
            replay.setVisible(false);
            menu.setVisible(false);
            finalScore.setVisible(false);
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
    public void stop(){
        try{
        itsTimer.cancel();
        gameTimer.cancel();
        }catch(NullPointerException e){
            System.err.println("nullPointerException capturée ligne 611");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            launch(args);
    }
    
}
