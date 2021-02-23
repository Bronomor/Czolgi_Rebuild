package Engine;

import java.util.Random;

public class Reproducer {

    private final int respawnDelay;
    private int lastSpawn;

    public Reproducer(int respawnDelay, int lastSpawn){
        this.respawnDelay= respawnDelay;
        this.lastSpawn = lastSpawn;
    }
    public boolean checkReproduce(int actualEpoch){
        if(actualEpoch >= lastSpawn + respawnDelay) {
            lastSpawn = actualEpoch;
            return true;
        }
        else {
            Random random = new Random();
            int randomInt = random.nextInt(10000);
            if(randomInt > 1500 && randomInt < 1600) {
                lastSpawn = actualEpoch;
                return true;
            }
        }
        return false;
    }
}
