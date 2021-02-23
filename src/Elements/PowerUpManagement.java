package Elements;

public class PowerUpManagement {

    private final PowerUp powerUp;
    private boolean isActive = false;
    private boolean used = false;

    public PowerUpManagement(PowerUp powerUp){
        this.powerUp = powerUp;
    }

    public PowerUpType getPowerUpType() { return powerUp.getType(); }
    public boolean check()      { return used && !isActive; }
    public void    activatePowerUp(){
        if(powerUp.getCount() >= 0) isActive = true;
    }
    public void    update(){
        if(isActive){
            if (!used) {
                powerUp.activate();
                used = true;
            }
            powerUp.subtractToEnd();

            if(powerUp.getToEnd() <= 0) {
                isActive = false;
            }
        }
    }

}
