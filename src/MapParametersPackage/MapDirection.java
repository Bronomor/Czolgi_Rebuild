package MapParametersPackage;

import Elements.Vector2d;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;


    public String toString() {
        return switch (this) {
            case NORTH -> "North";
            case NORTHEAST -> "North-East";
            case NORTHWEST -> "North-West";
            case SOUTH -> "South";
            case SOUTHEAST -> "South-East";
            case SOUTHWEST -> "South-West";
            case EAST -> "East";
            case WEST -> "West";
        };
    }

    public MapDirection next() {
        return switch (this) {
            case NORTH -> NORTHEAST;
            case NORTHEAST -> EAST;
            case EAST -> SOUTHEAST;
            case SOUTHEAST -> SOUTH;
            case SOUTH -> SOUTHWEST;
            case SOUTHWEST -> WEST;
            case WEST -> NORTHWEST;
            case NORTHWEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch (this) {
            case NORTHEAST -> NORTH;
            case EAST -> NORTHEAST;
            case SOUTHEAST -> EAST;
            case SOUTH -> SOUTHEAST ;
            case SOUTHWEST -> SOUTH;
            case WEST -> SOUTHWEST;
            case NORTHWEST -> WEST;
            case NORTH  -> NORTHWEST;
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, -1);
            case NORTHEAST -> new Vector2d(1, -1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, 1);
            case SOUTH -> new Vector2d(0, 1);
            case SOUTHWEST -> new Vector2d(-1, 1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, -1);
        };
    }

    public MapDirection opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case NORTHEAST -> SOUTHWEST;
            case NORTHWEST -> SOUTHEAST;
            case SOUTH -> NORTH;
            case SOUTHEAST ->NORTHWEST;
            case SOUTHWEST ->NORTHEAST;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
