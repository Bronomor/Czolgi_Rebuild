package Elements;

import MapParametersPackage.MapParameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class GrassField implements IWorldMap, IPositionChangeObserver {


    private final HashMap<Vector2d, Object>         mapObject  = new HashMap<>();
    private final HashMap<Vector2d, PowerUpType>    mapPowerUp = new HashMap<>();
    private final HashMap<Vector2d, ArrayList<Bullet>> bullets = new HashMap<>();

    private final Vector2d mapLower;
    private final Vector2d mapHigher;

    public GrassField(MapParameters mapParameters) {
        mapLower = mapParameters.getMapLower();
        mapHigher = mapParameters.getMapHigher();
    }

    public boolean canMoveTo    (Vector2d newPosition)  {
        Object object = ObjectAt(newPosition);
        return (object == null || object instanceof PowerUpType) && !isOffTheMap(newPosition);
    }
    public boolean isOffTheMap  (Vector2d position)     { return !position.precedes(mapHigher) || !position.follows(mapLower); }
    public boolean isOccupied   (Vector2d position)     { return mapObject.containsKey(position) || mapPowerUp.containsKey(position); }
    public Object  ObjectAt     (Vector2d position)     {
        if (mapObject.get(position) != null) return mapObject.get(position);
        return mapPowerUp.get(position);
    }

    public void place(Tank tank) { positionChanged(tank, tank.getPosition()); }
    public void positionChanged(Tank tank, Vector2d newPosition)  {
        mapObject.remove(tank.getPosition());
        if(newPosition != null) mapObject.put(newPosition,tank);
    }

    public HashMap<Vector2d, ArrayList<Bullet>> getBullets() { return bullets; }
    public void addBullet(Bullet bullet) {
        ArrayList<Bullet> bulletins = bullets.get(bullet.getPosition());
        if (bulletins != null) bulletins.add(bullet);
        else bulletins = new ArrayList<>(Collections.singleton(bullet));
        bullets.put(bullet.getPosition(), bulletins);
    }
    public void removeBullet(Bullet bullet) {
        ArrayList<Bullet> bulletsMain = bullets.get(bullet.getPosition());
        bulletsMain.remove(bullet);
        bullets.replace(bullet.getPosition(),bulletsMain);
    }

    public HashMap<Vector2d, PowerUpType> getMapPowerUp() { return mapPowerUp; }
    public void addPowerUp(Vector2d position, PowerUpType powerUpType) {
        if (canMoveTo(position) && !mapPowerUp.containsKey(position))
            mapPowerUp.put(position,powerUpType);
    }
    public void removePowerUp(Vector2d position) { mapPowerUp.remove(position); }

    public Collection<Object> getMapObject() {
        return mapObject.values();
    }
    public void addMapObject(Vector2d position, Object object) { mapObject.put(position,object); }
    public void removeMapObject(Vector2d position) { mapObject.remove(position); }

    public Vector2d getMapLower() { return this.mapLower; }
    public Vector2d getMapHigher() { return this.mapHigher; }
    public int getMapWidth() { return mapHigher.x-mapLower.x + 1; }
    public int getMapHeight() { return mapHigher.y - mapLower.y + 1; }

}
