package Engine;

import Elements.*;
import MapParametersPackage.MapParameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SimulationEngine implements IEngine {

    private final IWorldMap iWorldMap;
    private final Vector2d  mapLower;
    private final Vector2d  mapHigher;

    private final Player         player;
    private final List<Bot>      bots       = new ArrayList<>();
    private final List<Bullet>   bullets    = new ArrayList<>();
    private final List<Obstacle> obstacles  = new ArrayList<>();
    private final ArrayList<PowerUpManagement> pickedPowerUp = new ArrayList<>();

    private boolean isDoubledRound       = false;
    private boolean isDoubleSpeedBullet  = false;
    private boolean isPiercingBullet     = false;
    private boolean isBoundBullet        = false;

    public SimulationEngine(int botCount, int obstacleCount, IWorldMap iWorldMap, MapParameters mapParameters, Player player){

        this.iWorldMap  = iWorldMap;
        this.player     = player;
        this.mapLower   = mapParameters.getMapLower();
        this.mapHigher  = mapParameters.getMapHigher();

        for(int i=0; i<botCount; i++) {
            Bot bot = addBot();
            randPowerUp(bot);
        }
        for(int i=0; i<obstacleCount; i++) addObstacle();
    }

    public boolean run(int actualEpoch) {
        checkPlayerPickedPowerUp();

        if (!isDoubledRound) botTurn();
        UpdateActivePowerUp();

        moveBullets();
        bulletsCollision();
        removeDestroyedObject();

        if (isDoubleSpeedBullet) {
            onlyPlayerBullet();
            removeDestroyedObject();
        }

        reproduceObjects(actualEpoch);

        return isPlayerAlive();
    }


    private void checkPlayerPickedPowerUp(){
        PowerUpType powerUpType = iWorldMap.getMapPowerUp().get(player.getPosition());
        if(powerUpType != null){
            iWorldMap.removePowerUp(player.getPosition());
            if(powerUpType == PowerUpType.ExtraLife) player.addLive(1);
            else player.getTank().addPowerUp(powerUpType);
        }
    }
    private void botTurn() {
        for(Bot bot : bots){
            int actionType = new Random().nextInt(2);
            switch (actionType) {
                case 0 -> bot.moveTrackerLow(player);
                case 1 -> addBotBullet(bot);
            }

            PowerUpType powerUpType = iWorldMap.getMapPowerUp().get(bot.getPosition());
            if(powerUpType != null)iWorldMap.removePowerUp(bot.getPosition());
        }
    }
    private void UpdateActivePowerUp(){
        Iterator<PowerUpManagement> iterator = pickedPowerUp.iterator();
        while (iterator.hasNext()) {
            PowerUpManagement powerUpManagement = iterator.next();
            boolean toRemove = powerUpManagement.check();

            if (toRemove) {
                iterator.remove();
                if (powerUpManagement.getPowerUpType() == PowerUpType.DoubleTurn) isDoubledRound = false;
                if (powerUpManagement.getPowerUpType() == PowerUpType.BulletDoubleSpeed) isDoubleSpeedBullet = false;
                if (powerUpManagement.getPowerUpType() == PowerUpType.BulletPiercing) isPiercingBullet = false;
                if (powerUpManagement.getPowerUpType() == PowerUpType.BulletBound) isBoundBullet = false;
            }
            else powerUpManagement.update();
        }
    }
    private void moveBullets() {
        for( Bullet bullet : bullets) bullet.updatePosition();
    }
    private void bulletsCollision(){
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            Object object = iWorldMap.ObjectAt(bullet.getPosition());

            if(!(object instanceof PowerUp) && object != null) {

                int subtractLive = 1;
                if (isPiercingBullet) subtractLive += 1;

                if(object instanceof Tank) {
                    Tank tank = (Tank) object;
                    if (tank.getPowerUp(PowerUpType.Immortality) == null || tank.getPowerUp(PowerUpType.Immortality).getToEnd() <= 0) ((Tank) object).subtractLive(subtractLive);
                    if(bullet.isPlayerBullet()) player.addScore(1);
                }
                else if (object instanceof Obstacle){
                    ((Obstacle) object).subtractLive();
                }

                if(!isPiercingBullet && !isBoundBullet){
                    iWorldMap.removeBullet(bullet);
                    iterator.remove();
                }
                else if (isBoundBullet && !(object instanceof Tank)){
                    bullet.setDirection(bullet.getDirection().opposite());
                }
                else {
                    iWorldMap.removeBullet(bullet);
                    iterator.remove();
                }

            }
            else if(iWorldMap.isOffTheMap(bullet.getPosition()) && !isBoundBullet) {
                iWorldMap.removeBullet(bullet);
                iterator.remove();
            }
            else if (isBoundBullet && iWorldMap.isOffTheMap(bullet.getPosition())){
                bullet.setDirection(bullet.getDirection().opposite());
                bullet.updatePosition();
                bullet.updatePosition();
            }
        }
    }
    private void onlyPlayerBullet(){
        for( Bullet bullet : bullets) {
            if (bullet.isPlayerBullet()) bullet.updatePosition();
        }
    }
    private void removeDestroyedObject(){

        Iterator<Bot> iterator = bots.iterator();
        while (iterator.hasNext()) {
            Bot bot = iterator.next();
            if(bot.getLive() <= 0) {
                bot.positionChanged(bot, null);
                bot.removeObserver(bot);
                iterator.remove();
            }
        }

        Iterator<Obstacle> iterator2 = obstacles.iterator();
        while (iterator2.hasNext()) {
            Obstacle obstacle = iterator2.next();
            if(obstacle.getLive() <= 0) {
                iWorldMap.removeMapObject(obstacle.getPosition());
                iterator2.remove();
            }
        }
    }
    private void reproduceObjects(int actualEpoch){

        for(MapObject mapObject : MapObject.values()){
            boolean toReproduce = mapObject.checkReproduce(actualEpoch);

            if(toReproduce){
                Object object = mapObject.getObject();
                if (object.equals(Bot.class)) {
                    Bot bot = addBot();
                    randPowerUp(bot);
                }
                else if (object.equals(Obstacle.class)) addObstacle();
                else addPowerUpToMap((PowerUpType) object);
            }
        }

        if(getBotsCount() < 1) addBot();
    }
    public  void addObstacle(){
        Vector2d pos = randPosition(true);
        Obstacle obstacle = new Obstacle(pos);
        iWorldMap.addMapObject(obstacle.getPosition(),obstacle);
        obstacles.add(obstacle);
    }
    private boolean isPlayerAlive(){
        return player.getLive() >= 1;
    }


    public  void addBotBullet(Bot bot) {
        Bullet bullet = bot.shootTracker(player);
        bullets.add(bullet);
    }
    public  void addPlayerBullet(Player player) {
        Bullet bullet = player.shoot();
        bullets.add(bullet);
    }
    public  Bot  addBot(){
        Vector2d pos = randPosition(false);
        Bot bot = new Bot(iWorldMap, pos,1);
        bots.add(bot);
        return bot;
    }
    public void  addPowerUpToMap(PowerUpType powerUpType){ iWorldMap.addPowerUp(randPosition(true),powerUpType); }

    protected int getBotsCount() { return bots.size(); }

    public void activatePowerUp(Tank tank, PowerUpType powerUpType){
        PowerUp powerUpToActivate = tank.getPowerUp(powerUpType);
        if (powerUpToActivate != null) {
            if (powerUpToActivate.getCount() > 0) {
                PowerUpManagement powerUpManagement = new PowerUpManagement(powerUpToActivate);
                pickedPowerUp.add(powerUpManagement);
                powerUpManagement.activatePowerUp();
                tank.removePowerUp(powerUpType);

                if(powerUpType == PowerUpType.DoubleTurn) isDoubledRound = true;
                if(powerUpType == PowerUpType.BulletDoubleSpeed) isDoubleSpeedBullet = true;
                if(powerUpType == PowerUpType.BulletPiercing) isPiercingBullet = true;
                if(powerUpType == PowerUpType.BulletBound) isBoundBullet = true;
            }
        }
    }
    private Vector2d randPosition(boolean canSpawnOnMiddle){
        Random random = new Random();
        Vector2d pos = null;

        for(int i = 0; i< 1; i++){
            pos = new Vector2d(random.nextInt(iWorldMap.getMapWidth()),random.nextInt(iWorldMap.getMapHeight()));
            if (canSpawnOnMiddle && iWorldMap.isOccupied(pos)) i-=1;
            else if(!canSpawnOnMiddle && (iWorldMap.isOccupied(pos) || pos.isInMiddleRectangle(mapLower,mapHigher))) i-=1;
        }
        return pos;
    }
    public void      randPowerUp(Bot bot){
        PowerUpType powerUpType = PowerUpType.BulletDoubleSpeed;
        for(int i=1; i<4; i++){
            switch (i) {
                case 1 -> powerUpType = PowerUpType.BulletPiercing;
                case 2 -> powerUpType = PowerUpType.BulletBound;
                case 3 -> powerUpType = PowerUpType.ExtraLife;
            }

            Random random = new Random();
            int randNumber = random.nextInt(10000);
            if(randNumber > 1000 && randNumber < 2000) {
                bot.addPowerUp(powerUpType);
                if(powerUpType == PowerUpType.ExtraLife) bot.addLive(1);
                else activatePowerUp(bot,powerUpType);
            }
        }

    }
}