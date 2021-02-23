package Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public interface IWorldMap {

    boolean canMoveTo   (Vector2d newPosition);
    boolean isOffTheMap (Vector2d position);
    boolean isOccupied  (Vector2d position);
    Object  ObjectAt    (Vector2d position);


    void place(Tank animal);

    HashMap<Vector2d, ArrayList<Bullet>> getBullets();
    void addBullet(Bullet bullet);
    void removeBullet(Bullet bullet);

    HashMap<Vector2d, PowerUpType> getMapPowerUp();
    void addPowerUp(Vector2d position, PowerUpType powerUpType);
    void removePowerUp(Vector2d position);

    Collection<Object> getMapObject();
    void addMapObject(Vector2d position,Object object);
    void removeMapObject(Vector2d position);


    Vector2d getMapLower();
    Vector2d getMapHigher();
    int getMapWidth();
    int getMapHeight();


}