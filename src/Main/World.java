package Main;

import Elements.*;
import Engine.SimulationEngine;
import MapParametersPackage.MapParameters;
import PopUpWindow.GameOverWindow;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class World {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;

    private final SimulationEngine engine;
    private final IWorldMap grassField;
    private final Vector2d mapScale;
    private int actualEpoch = 0;

   private final VBox mapLayout  = new VBox();
   private final VBox menuLayout = new VBox();
   private final Player player;
   private final Stage window   = new Stage();

    public World(MapParameters mapParameters){
        int width = (WINDOW_WIDTH-200) / mapParameters.getMapWidth();
        int height =  WINDOW_HEIGHT / mapParameters.getMapHeight();
        mapScale = new Vector2d(width,height);

        grassField = new GrassField(mapParameters);
        Vector2d middle = new Vector2d(mapParameters.getMapWidth()/2,mapParameters.getMapHeight()/2);
        player = new Player("Mistrzuniu strzela", grassField, middle,10);
        engine = new SimulationEngine(mapParameters.getBotCount(),mapParameters.getObstacleCount(),grassField, mapParameters,player);
    }
    public void start() {
        try {

            HBox hBox   = new HBox(mapLayout,menuLayout);
            Scene scene = new Scene(hBox,WINDOW_WIDTH,WINDOW_HEIGHT);

            window.setTitle("Fight arena");
            window.setScene(scene);
            window.show();
            display();

            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.W) {
                    boolean goodMove = player.move();
                    if(goodMove) update();
                }
                else if(e.getCode() == KeyCode.K){
                    engine.addPlayerBullet(player);
                    update();
                }
                else if(e.getCode() == KeyCode.LEFT)   player.leftRotation();
                else if(e.getCode() == KeyCode.RIGHT)  player.rightRotation();
                else if(e.getCode() == KeyCode.DIGIT1) engine.activatePowerUp(player, PowerUpType.DoubleTurn);
                else if(e.getCode() == KeyCode.DIGIT2) engine.activatePowerUp(player, PowerUpType.BulletDoubleSpeed);
                else if(e.getCode() == KeyCode.DIGIT3) engine.activatePowerUp(player, PowerUpType.BulletPiercing);
                else if(e.getCode() == KeyCode.DIGIT4) engine.activatePowerUp(player, PowerUpType.BulletBound);
                else if(e.getCode() == KeyCode.DIGIT5) engine.activatePowerUp(player, PowerUpType.Immortality);
                display();
            });

        } catch (Exception e){
            System.out.println("Something wrong with preparation in single World");
            e.printStackTrace();
        }
    }
    private void display(){
        Canvas canvas           = new Canvas(WINDOW_WIDTH-200,WINDOW_HEIGHT);
        Visualizer visualizer   = new Visualizer(mapScale, grassField);
        visualizer.drawOnMap(player,mapLayout,canvas);
        visualizer.drawMenu(player,menuLayout);
    }
    private void update(){
        actualEpoch +=1;
        boolean isPlayerAlive = engine.run(actualEpoch);

        if (!isPlayerAlive) {
            window.close();

            MenuStart menuStart = new MenuStart();
            menuStart.start(new Stage());
            GameOverWindow gameOverWindow = new GameOverWindow();
            gameOverWindow.start(player);
        }
    }

}