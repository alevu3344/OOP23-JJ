package it.unibo.jetpackjoyride.core;

import it.unibo.jetpackjoyride.core.entities.barry.impl.PlayerMover;
import it.unibo.jetpackjoyride.core.handler.ChunkMakerImpl;
import it.unibo.jetpackjoyride.core.map.api.MapBackground;
import it.unibo.jetpackjoyride.core.map.impl.MapBackgroundImpl;
import it.unibo.jetpackjoyride.utilities.GameInfo;
import it.unibo.jetpackjoyride.utilities.InputHandler;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameLoop{
   
    private Scene gameScene;
    private GameInfo gameInfo;
    private AnimationTimer timer;
    private MapBackground map;
    private ChunkMakerImpl chunkMaker;
    Pane root ;
    private boolean isRunning;
    Group obstacleGroup;
    private final int FPS=70;
    private long nSecPerFrame= Math.round(1.0/FPS * 1e9);
    PlayerMover playerMover;

   
    private InputHandler inputH = new InputHandler();


    public GameLoop(){
        this.isRunning = false;
        initializeScene();
        initializeGameElements();
     
    }

    private void initializeScene() {
        root = new Pane();
        obstacleGroup = new Group();
        gameInfo = new GameInfo();
        gameScene = new Scene(root, gameInfo.getScreenWidth(), gameInfo.getScreenHeight());
        
        gameScene.setOnKeyPressed(event -> inputH.keyPressed(event.getCode()));
        gameScene.setOnKeyReleased(event -> inputH.keyReleased(event.getCode()));
        setupTimer();   
    }

    private void initializeGameElements(){
        
        map = new MapBackgroundImpl(gameInfo);

        chunkMaker = new ChunkMakerImpl();
        chunkMaker.initialize();

        playerMover = new PlayerMover();

        root.getChildren().add((Node)map);
        root.getChildren().add((Node)obstacleGroup);
    }

    private void setupTimer(){
        timer = new AnimationTimer() {

            private long lastUpdate=0;

            @Override
            public void handle(long now) {

                if(now - lastUpdate > nSecPerFrame){
                
               

                updateModel();
                updateView();
                chunkMaker.update(obstacleGroup);

                lastUpdate=now;
                }
                
            }
        };
    }


    private void updateModel(){ 
        
        playerMover.move(inputH.isSpacePressed());
        updateScreenSize();
        map.updateBackgroundModel();
        
    }

    private void updateView(){
        
        map.updateBackgroundView();
        playerMover.updateView(root);
        
    }

    private void updateScreenSize() {
        gameScene.widthProperty().addListener((obs, oldValue, newValue) -> {

            double newWidth = newValue.doubleValue();
            gameInfo.updateInfo(newWidth, gameInfo.getScreenHeight());
        });
    
        gameScene.heightProperty().addListener((obs, oldValue, newValue) -> {
           
            double newHeight = newValue.doubleValue();
            gameInfo.updateInfo(gameInfo.getScreenWidth(), newHeight);
        });
    }
    

 

    public void starLoop(){
        timer.start();
        this.isRunning = true;
    }

    public void endLoop(){
        this.isRunning = false;
        chunkMaker.over();
    }
    
    public Scene getScene(){
        return this.gameScene;
    }

}