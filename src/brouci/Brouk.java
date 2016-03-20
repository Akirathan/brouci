package brouci;


import java.util.ArrayList;
import java.util.Random;

public class Brouk extends Field {

    private class Move {

        /**
         * Trida reprezentujici zapamatovane pohyby.
         */
        private class MoveType {
            Field[] fields ;
            Direction direction ;

            MoveType() {
                fields = new Field[4] ;
                direction = Direction.NONE ;
            }

            /**
             * Zkopiruje konfiguraci z buggNeighbourhood.
             * Plne inicializuje MoveType.
             */
            MoveType(Environment.BuggNeighbourhood buggNeighbourhood) {
                this() ;
                for (int i = 0; i < fields.length; i++) {
                    fields[i] = buggNeighbourhood.get(i) ;
                }
            }

            /**
             * Vrati true, pokud dany MoveType s danym BuggNeighbourhood odpovidaji stejne konfiguraci.
             * Potrebne v metode getNextMove().
             * @param o BuggNeighbourhood
             * @return
             */
            @Override
            public boolean equals(Object o) {
                if (o instanceof Environment.BuggNeighbourhood) {
                    Environment.BuggNeighbourhood buggNeighbourhood = (Environment.BuggNeighbourhood)o ;
                    int i = 0 ;
                    while (fields[i].equals(buggNeighbourhood.get(i)) && i < 4)
                        i++ ;

                    if (i == 4) //prosli jsme cely fields[i]
                        return true ;
                    else
                        return false ;
                }
                else return false ;
            }
        }

        /**
         * Reprezentuje pole zapamatovanych pohybu.
         */
        private ArrayList<MoveType> moveTypes;

        public Move() {
            moveTypes = new ArrayList<>() ;
        }

        /**
         * Najde posledni pohyb, ktery udelal s konfiguraci reprezentovanou pomoci buggNeighbourhood.
         * Vrati smer, ktery byl zapamatovany, nebo nahodny smer, kdyz v pameti nenajde konfiguraci
         * odpovidajici buggNeighbourhood
         */
        public Direction getNextMove(Environment.BuggNeighbourhood buggNeighbourhood) {
            for (MoveType moveType : moveTypes) {
                if (moveType.equals(buggNeighbourhood)) {
                    return moveType.direction ;
                }
            }
            //dana konfigurace neni v pameti, vrat nahodny smer a zapamatuj si konfiguraci a smer
            MoveType movetype = new MoveType(buggNeighbourhood) ;
            Direction direction = Direction.RANDOM ;
            movetype.direction = direction ;
            moveTypes.add(movetype) ;
            return direction ;
        }
    }

    private Move move ;
    private Coordinate location ;
    private int energy ;


    public Brouk() {
        move = new Move() ;
        energy = 100 ;
    }

    public Coordinate getLocation() {
        return location ;
    }

    public void setLocation(Coordinate coordinate) {
        location = coordinate ;
    }

    /**
     * Snizi energii o konstantu.
     */
    public void lowerEnergy() {
        this.energy -= 10 ;
    }


    /**
     * Vrati smer, kam chce brouk jit
     * Samotne posunuti Brouka zpracuje Environment.
     * @return smer, kam chce brouk jit
     */
    public Direction getNextMove(Environment.BuggNeighbourhood buggNeighbourhood) {
        return move.getNextMove(buggNeighbourhood);
    }

    @Override
    public boolean equals(Object o) {
        return false ;
    }

}

enum Direction {
    UP(-1,0),
    RIGHT(0,1),
    DOWN(1,0),
    LEFT(0,-1),
    NONE(0,0),
    RANDOM() ;

    Direction(int X, int Y) {
        this.X = X ;
        this.Y = Y ;
    }

    Direction() {
        getRandomDirection() ;
    }

    private int X, Y ;

    /**
     * Vrati souradnice smeru.
     */
    public Coordinate getCoordinate() {
        return new Coordinate(X, Y) ;
    }

    private Direction getRandomDirection() {
        Random random = new Random() ;
        int a = random.nextInt(4) ;
        switch (a) {
            case 0 :
                return NONE ;
            case 1 :
                return UP ;
            case 2 :
                return RIGHT;
            case 3 :
                return DOWN ;
            case 4 :
                return LEFT ;
        }
        return NONE ;
    }
}


class Coordinate implements Comparable {
    public int X, Y ;

    public Coordinate plus(Coordinate other) {
        return new Coordinate(this.X + other.X, this.Y + other.Y) ;
    }

    Coordinate(int X, int Y) {
        this.X = X ;
        this.Y = Y ;
    }

    @Override
    public int compareTo(Object o) {
        Coordinate other = (Coordinate)o ;
        if (this.X < other.X ||
                (this.X == other.X && this.Y < other.Y))
            return -1 ;
        else if (this.X > other.X ||
                (this.X == other.X && this.Y > other.Y))
            return 1 ;
        else return 0 ;
    }
}
