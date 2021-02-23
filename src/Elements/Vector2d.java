package Elements;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean precedes(Vector2d other) { return (this.x <= other.x && this.y <= other.y); }

    public boolean follows(Vector2d other) { return (this.x >= other.x && this.y >= other.y); }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Vector2d)) {
            return false;
        } else {
            Vector2d that = (Vector2d)other;
            return this.x == that.x && this.y == that.y;
        }
    }


    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    public double differenceVectors(Vector2d other ) { return Math.sqrt(Math.pow(this.y - other.y,2) + Math.pow(this.x - other.x,2)); }
    public boolean isInMiddleRectangle(Vector2d lower, Vector2d higher) {
        Vector2d middle = new Vector2d((higher.x-lower.x+1)/2,(higher.y-lower.y+1)/2);
        Vector2d RectangleLower = new Vector2d(middle.x-2, middle.y-2);
        Vector2d RectangleHigher = new Vector2d(middle.x+2, middle.y+2);
        return this.follows(RectangleLower) && this.precedes(RectangleHigher);
    }
}
