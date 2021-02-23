package Elements;

import MapParametersPackage.MapDirection;

import java.util.ArrayList;
import java.util.Collections;

public class Bullet {

    private MapDirection    direction;
    private Vector2d        position;
    private final IWorldMap iWorldMap;
    private final boolean   isPlayerBullet;

    public Bullet(MapDirection direction, Vector2d position, IWorldMap iWorldMap, boolean isPlayerBullet){
        this.direction      = direction;
        this.position       = position;
        this.iWorldMap      = iWorldMap;
        this.isPlayerBullet = isPlayerBullet;
    }

    public MapDirection getDirection() { return direction; }
    public Vector2d      getPosition() { return position; }
    public boolean    isPlayerBullet() { return isPlayerBullet; }

    public void     updatePosition() {
        ArrayList<Bullet> bulletins = iWorldMap.getBullets().get(position);
        bulletins.remove(this);
        iWorldMap.getBullets().put(position,bulletins);

        this.position = this.position.add(direction.toUnitVector());
        bulletins = iWorldMap.getBullets().get(position);
        if(bulletins != null) bulletins.add(this);
        else bulletins = new ArrayList<>(Collections.singleton(this));
        iWorldMap.getBullets().put(position,bulletins);
    }
    public void     setDirection(MapDirection mapDirection) { this.direction = mapDirection; }
}
