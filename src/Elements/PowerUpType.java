package Elements;

public enum PowerUpType {

    DoubleTurn(1),
    BulletDoubleSpeed(10),
    BulletPiercing(10),
    BulletBound(10),
    Immortality(10),
    ExtraLife(0);

    private final int duration;
    PowerUpType(int duration){
        this.duration = duration;
    }

    public String toString(){
        return switch (this) {
            case DoubleTurn -> "Podwójna runda";
            case BulletDoubleSpeed -> "Podwójna szybkość pocisku";
            case BulletPiercing -> "Pocisk przeszywający";
            case BulletBound -> "Pociski się odbijają";
            case Immortality -> "Nieśmiertelność";
            case ExtraLife -> "Dodatkowe życie";
        };
    }

    public int getDuration() { return duration; }
}
