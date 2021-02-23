package Elements;

import MapParametersPackage.MapDirection;

import java.util.ArrayList;
import java.util.List;

public class Tank implements IPositionChangeObserver {

    protected IWorldMap     map;
    protected Vector2d      position;
    protected MapDirection  orientation = MapDirection.NORTH;
    protected int           live;
    protected ArrayList<PowerUp>            powerUpList = new ArrayList<>();
    protected List<IPositionChangeObserver> observers   = new ArrayList<>();

    public Tank(IWorldMap map, Vector2d initialPosition, int live) {
        this.map        = map;
        this.position   = initialPosition;
        this.live       = live;

        for(PowerUpType powerUpType : PowerUpType.values()){ powerUpList.add(new PowerUp(powerUpType.getDuration(),powerUpType)); }
        addObserver((IPositionChangeObserver) map);
        map.place(this);


    }

    public String toString(){
        return switch (this.orientation) {
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case NORTHWEST -> "NW";
            case SOUTH -> "S";
            case SOUTHEAST -> "SE";
            case SOUTHWEST -> "SW";
            case EAST -> "E";
            case WEST -> "W";
        };
    }

    public int  getLive() { return this.live; }
    public void addLive(int live) { this.live += live; }
    public void subtractLive(int live) { this.live -= live; }

    public void addObserver(IPositionChangeObserver observer){ observers.add(observer); }
    public void positionChanged(Tank tank, Vector2d newPosition){ for(IPositionChangeObserver obs : observers) obs.positionChanged(tank,newPosition); }
    public void removeObserver(IPositionChangeObserver observer){ observers.remove(observer); }

    public PowerUp  getPowerUp(PowerUpType powerUpType) {
        for(PowerUp powerUp : powerUpList) {
            if(powerUp.getType() == powerUpType) return powerUp;
        }
        return null;
    }
    public void     addPowerUp(PowerUpType powerUpType){
        PowerUp powerUpReplace = getPowerUp(powerUpType);
        powerUpReplace.addCount(1);
    }
    public void     removePowerUp(PowerUpType powerUpType){
        PowerUp powerUpReplace = getPowerUp(powerUpType);
        powerUpReplace.subtractCount(1);
    }

    public MapDirection getOrientation(){ return this.orientation; }
    public Vector2d     getPosition()   { return this.position; }
    public Tank         getTank()       { return this; }

    protected void addBulletToMap(Bullet bullet) { this.map.addBullet(bullet); }
    public    void leftRotation() { this.orientation = this.orientation.previous(); }
    public    void rightRotation() { this.orientation = this.orientation.next(); }
}
