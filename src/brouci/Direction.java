package brouci;

import java.util.Random;

/**
 * Created by bruchpa on 19.4.16.
 */
public enum Direction {
    UP(-1,0),
    RIGHT(0,1),
    DOWN(1,0),
    LEFT(0,-1),
    NONE(0,0) ;

    Direction(int X, int Y) {
        this.X = X ;
        this.Y = Y ;
    }


    private int X, Y ;

    /**
     * Vrati souradnice smeru.
     */
    public Coordinate getCoordinate() {
        return new Coordinate(X, Y) ;
    }

    static Direction getRandomDirection() {
        Random random = new Random() ;
        int a = random.nextInt(4) ;
        switch (a) {
            case 0 :
                return UP ;
            case 1 :
                return RIGHT ;
            case 2 :
                return DOWN;
            case 3 :
                return LEFT ;
        }
        return NONE ;
    }

    /**
     * Vraci nazev instance.
     * @return Nazev instance, tj. UP, RIGHT, apod.
     */
    @Override
    public String toString() {
        return this.name() ;
    }
}
