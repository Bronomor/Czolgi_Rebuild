package Elements;

public class PowerUp {
    private int count = 0;
    private int toEnd = 0;
    private final int duration;
    private final PowerUpType type;

    public PowerUp(int duration, PowerUpType type){
        this.duration = duration;
        this.type = type;
    }

    public int  getCount() { return this.count; }
    public void addCount(int value){ this.count += value; }
    public void subtractCount(int value){ this.count -= value; }

    public PowerUpType getType() { return this.type; }
    public int getDuration() { return this.duration; }
    public int getToEnd() { return this.toEnd; }

    public void activate() {
        if (count >= 0) toEnd = duration;
    }
    public void subtractToEnd(){
        if (toEnd > 0) toEnd -= 1;
    }

}
