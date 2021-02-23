package Engine;


import Elements.Bot;
import Elements.Player;
import Elements.PowerUpType;

public interface IEngine {

    boolean run(int ActualEpoch);
    void randPowerUp(Bot bot);

    void addBotBullet(Bot bot);
    void addPlayerBullet(Player player);
    Bot  addBot();
    void addObstacle();
    void addPowerUpToMap(PowerUpType powerUpType);

}

