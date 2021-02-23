package MapParametersPackage;

import Elements.Vector2d;

public class MapParameters {
    private final int worldWidth;
    private final int worldHeight;
    private final Vector2d worldLower;
    private final Vector2d worldHigher;
    private final int botCount;
    private final int obstacleCount;

    public MapParameters() {
        ReadJSONMapParameters readJSONMapParameters = new ReadJSONMapParameters();
        int[] Parameters = readJSONMapParameters.readParameters();

        if(Parameters[2] == 0) throw new IllegalArgumentException("Jungle ratio is 0");
        for(int i : Parameters){
            if(i < 0) throw new IllegalArgumentException("Invalid data! Parameters are negative. Change it to positive");
        }

        worldWidth  = Parameters[0];
        worldHeight = Parameters[1];
        worldLower  = new Vector2d(0,0);
        worldHigher = new Vector2d(worldWidth-1,worldHeight-1);
        botCount    = Parameters[2];
        obstacleCount = Parameters[3];
    }

    public int      getMapWidth()        { return this.worldWidth; }
    public int      getMapHeight(){ return this.worldHeight; }
    public Vector2d getMapLower() { return this.worldLower; }
    public Vector2d getMapHigher() { return this.worldHigher; }
    public int      getBotCount() { return this.botCount; }
    public int      getObstacleCount() { return this.obstacleCount; }
}
