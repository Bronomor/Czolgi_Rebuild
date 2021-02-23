package Elements;

public class Obstacle {

    private final Vector2d obstaclePosition;
    private int live = 2;

    public Obstacle(Vector2d position){ this.obstaclePosition = position; }

    public Vector2d getPosition()   { return obstaclePosition; }
    @Override
    public String   toString()      { return "*"; }
    public int      getLive()       { return live; }
    public void     subtractLive()  { this.live -= 1; }

}
