package Engine;

import Elements.Bot;
import Elements.Obstacle;
import Elements.PowerUpType;

public enum MapObject {

    BotReproduction      (10,0, Bot.class                       ),
    ObstacleReproduction (10,0, Obstacle.class                  ),
    ImmortalPowerUpRep   (40,0, PowerUpType.Immortality         ),
    ExtraLifePowerUp     (16,0, PowerUpType.ExtraLife           ),
    DoubleTurnPowerUp    (25,0, PowerUpType.DoubleTurn          ),
    DoubleSpeedBullet    (35,0, PowerUpType.BulletDoubleSpeed   ),
    PiercingBulletPowerUp(29,0, PowerUpType.BulletPiercing      ),
    BounceBulletPowerUp  (30,0, PowerUpType.BulletBound         );

    private final Reproducer reproducer;
    private final Object     object;

    MapObject(int respawnDelay, int startEpoch, Object object){
        this.reproducer = new Reproducer(respawnDelay,startEpoch);
        this.object = object;
    }

    public Object  getObject() { return this.object; }
    public boolean checkReproduce(int actualEpoch) { return reproducer.checkReproduce(actualEpoch); }
}
