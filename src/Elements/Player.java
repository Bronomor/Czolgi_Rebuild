package Elements;

import MapParametersPackage.MapDirection;

import java.util.ArrayList;

public class Player extends Tank {

    private final String name;
    private int score = 0;

    public Player(String name, IWorldMap map, Vector2d initialPosition, int live) {
        super(map,initialPosition,live);
        this.name = name;
    }

    public String getName()  { return this.name; }
    public int    getScore() { return this.score; }

    public void addScore(int value) { this.score += value; }

    public boolean move()  {
        if(orientation == MapDirection.NORTH || orientation == MapDirection.EAST || orientation == MapDirection.SOUTH || orientation == MapDirection.WEST){
            Vector2d positionNew = this.position.add(this.orientation.toUnitVector());
            if(map.canMoveTo(positionNew)) {
                this.positionChanged(this, positionNew);
                position = positionNew;
                return true;
            }
        }
        return false;
    }
    public Bullet  shoot() {
        Bullet bullet = new Bullet(orientation,position,map,true);
        addBulletToMap(bullet);
        return bullet;
    }
    public ArrayList<PowerUp> getPowerUpList() { return this.powerUpList; }
}
