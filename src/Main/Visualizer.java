package Main;

import Elements.*;
import MapParametersPackage.MapDirection;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Visualizer {

    private final Vector2d mapScale;
    private final Vector2d mapLower;
    private final Vector2d mapHigher;
    private final IWorldMap iWorldMap;

    public Visualizer(Vector2d mapScale, IWorldMap iWorldMap){
        this.mapScale = mapScale;
        this.mapLower = iWorldMap.getMapLower();
        this.mapHigher = iWorldMap.getMapHigher();
        this.iWorldMap = iWorldMap;
    }

    public void drawMenu(Player player, VBox menu){
        menu.getChildren().clear();

        Font font = new Font(20);
        Text playerName = new Text("\n Nazwa gracza: \n " + player.getName()+ "\n Ilość żyć: " + player.getLive());
        playerName.setFont(font);
        playerName.setWrappingWidth(200);
        playerName.setTextAlignment(TextAlignment.CENTER);

        Text playerScore = new Text("Wynik: \n " + player.getScore() + "\n");
        playerScore.setFont(font);
        playerScore.setWrappingWidth(200);
        playerScore.setTextAlignment(TextAlignment.CENTER);

        Text powerUpText = new Text(" Power-Up:\n");
        powerUpText.setFont(new Font(20));
        powerUpText.setWrappingWidth(200);
        powerUpText.setTextAlignment(TextAlignment.CENTER);

        Text powerUpTextDetails = new Text("( Do końca | Ilość | Długość)\n");
        powerUpTextDetails.setFont(new Font(16));
        powerUpTextDetails.setWrappingWidth(200);
        powerUpTextDetails.setTextAlignment(TextAlignment.CENTER);

        menu.getChildren().addAll(playerName, playerScore, powerUpText,powerUpTextDetails);

        int i=1;
        for(PowerUp powerUp : player.getPowerUpList()){
            Text doubleRound = new Text();
            doubleRound.setFont(new Font(16));
            doubleRound.setWrappingWidth(200);
            doubleRound.setTextAlignment(TextAlignment.CENTER);
            if(i != 6) doubleRound.setText(i + ". " + powerUp.getType().toString() + "\n" + powerUp.getToEnd() +  " | " + powerUp.getCount() + " | " + powerUp.getDuration());
            else doubleRound.setText( i + ". " + powerUp.getType().toString() + "\n" + "Automatycznie aktywne");
            menu.getChildren().add(doubleRound);
            i+=1;
        }
    }

    public void drawOnMap(Player player, VBox map, Canvas canvas){
        map.getChildren().clear();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        mapSquares(gc);
        mapPlayer(gc,player);
        for(Object object : iWorldMap.getMapObject()){
            if (object instanceof Tank) mapBot(gc,player, (Tank) object);
            else mapObstacle(gc,(Obstacle) object);
        }

        for(Vector2d pos : iWorldMap.getMapPowerUp().keySet())
            mapPowerUp(gc, pos);

        for(ArrayList<Bullet> bullets : iWorldMap.getBullets().values()){
            for(Bullet bullet : bullets)
                mapBullets(gc,bullet);
        }

        map.getChildren().add(canvas);
    }

    public void mapSquares(GraphicsContext gc){
        gc.setFill(Color.rgb(0, 0, 0));
        int rectangleCountRow = (mapHigher.x- mapLower.x+1);
        int rectangleCountCol = (mapHigher.y- mapLower.y+1);
        int xCord = 0, yCord = 0;
        for(int i=0; i< rectangleCountCol; i++){
            for(int j=0; j< rectangleCountRow; j++){
                gc.fillRect(xCord+1,yCord+1, mapScale.x-1, mapScale.y-1); // leave room for white edges
                xCord += mapScale.x;
            }
            yCord += mapScale.y;
            xCord = 0;
        }
    }
    public void mapPlayer(GraphicsContext gc,Player player) {
        Tank playerTank = player.getTank();
        gc.setFill(Color.rgb(0, 255, 0));
        gc.fillRect(playerTank.getPosition().x * mapScale.x + (mapScale.x >> 2), playerTank.getPosition().y * mapScale.y + (mapScale.y >> 2), mapScale.x >> 1, mapScale.y >> 1);

        gc.setStroke(Color.rgb(0, 80, 0));
        gc.setLineWidth(3);
        barrel(gc,playerTank);
    }
    public void mapBot(GraphicsContext gc,Player player, Tank tank) {
        if (tank != player.getTank()) {
            gc.setFill(Color.rgb(105, 16, 16));
            gc.fillRect(tank.getPosition().x * mapScale.x + (mapScale.x >> 2), tank.getPosition().y * mapScale.y + (mapScale.y >> 2), mapScale.x >> 1, mapScale.y >> 1);
            gc.setStroke(Color.rgb(176, 28, 28));
            gc.setLineWidth(3);
            barrel(gc,tank);
        }
    }
    public void mapObstacle(GraphicsContext gc,Obstacle obstacle){
        gc.setFill(Color.DARKRED);
        gc.fillRect((obstacle.getPosition().x * mapScale.x) + (mapScale.x / 3), (obstacle.getPosition().y * mapScale.y) + (mapScale.y / 3), mapScale.x /3, mapScale.y /3);
    }
    public void mapBullets(GraphicsContext gc,Bullet bullet) {
        gc.setFill(Color.YELLOW);
        gc.fillOval(bullet.getPosition().x * mapScale.x + mapScale.x *7/16,bullet.getPosition().y * mapScale.y + mapScale.y*3/8, mapScale.x /6, mapScale.y /6);
    }

    private void barrel(GraphicsContext gc,Tank tank) {
        MapDirection orientation = tank.getOrientation();

        double[] slopes = slopesBarrel(orientation);
        double posX = tank.getPosition().x * mapScale.x,
                posY = tank.getPosition().y * mapScale.y;

        gc.strokeLine(posX + (mapScale.x >> 1), posY + (mapScale.y >> 1), posX + mapScale.x * slopes[0] ,posY + mapScale.y * slopes[1]);
    }

    private double[] slopesBarrel(MapDirection orientation){

        double slope1, slope2;

        if (orientation == MapDirection.NORTH || orientation == MapDirection.NORTHEAST || orientation == MapDirection.NORTHWEST) slope2 = 0.125;
        else if (orientation == MapDirection.SOUTH || orientation == MapDirection.SOUTHEAST || orientation == MapDirection.SOUTHWEST) slope2 = 0.875;
        else slope2 = 0.5;

        if (orientation == MapDirection.NORTH || orientation == MapDirection.SOUTH) slope1 = 0.5;
        else if (orientation == MapDirection.NORTHEAST || orientation == MapDirection.SOUTHEAST || orientation == MapDirection.EAST) slope1 = 0.875;
        else slope1 = 0.125;

        return (new double[] {slope1,slope2});
    }

    public void mapPowerUp(GraphicsContext gc, Vector2d pos) {
        gc.setFill(Color.GREEN);
        gc.fillOval(pos.x * mapScale.x + (mapScale.x >> 2),pos.y * mapScale.y + (mapScale.y >> 2), mapScale.x >> 1, mapScale.y >> 1);
    }
}
