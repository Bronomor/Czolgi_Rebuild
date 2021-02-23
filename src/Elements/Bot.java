package Elements;

import MapParametersPackage.MapDirection;

import java.util.TreeMap;

public class Bot extends Tank {

    public Bot(IWorldMap map, Vector2d initialPosition, int live) {
        super(map,initialPosition,live);
    }

    public void moveTrackerLow(Tank player){
        TreeMap<Double, MapDirection> orderDirections = sortDirectionsDistanceToPlayer(player);
        for (MapDirection mapDirection : orderDirections.values()){
            Vector2d pos = this.position.add(mapDirection.toUnitVector());
            if(map.canMoveTo(pos)){
                positionChanged(this,pos);
                this.position = pos;
                this.orientation = mapDirection;
                break;
            }
        }
    }
    public  Bullet shootTracker(Tank player) {

        MapDirection orientation;
        if (this.getPosition().x == player.getPosition().x) {
            if (this.getPosition().y > player.getPosition().y) orientation = MapDirection.NORTH;
            else orientation = MapDirection.SOUTH;
        }
        else if (this.getPosition().y == player.getPosition().y) {
            if (this.getPosition().x > player.getPosition().x) orientation = MapDirection.WEST;
            else orientation = MapDirection.EAST;
        }
        else {
            if (this.getPosition().x > player.getPosition().x) {
                if (this.getPosition().y > player.getPosition().y) orientation = MapDirection.NORTHWEST;
                else orientation = MapDirection.SOUTHWEST;
            }
            else {
                if (this.getPosition().y > player.getPosition().y) orientation = MapDirection.NORTHEAST;
                else orientation = MapDirection.SOUTHEAST;
            }
        }

        Bullet bullet = new Bullet(orientation,position,map,false);
        addBulletToMap(bullet);
        return bullet;
    }

    private TreeMap<Double, MapDirection> sortDirectionsDistanceToPlayer(Tank player){
        MapDirection mapDirections = MapDirection.NORTH;
        TreeMap <Double, MapDirection> sortedMap = new TreeMap<>();

        for(int i=0; i<4; i++){
            double distance = this.position.add(mapDirections.toUnitVector()).differenceVectors(player.getPosition());
            sortedMap.put(distance,mapDirections);
            mapDirections = mapDirections.next().next();
        }

        return sortedMap;
    }
}
