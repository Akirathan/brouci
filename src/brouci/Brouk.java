package brouci;


import java.util.ArrayList;

public class Brouk extends Field {

    private class Move {

        /**
         * Field[0] = smer nahoru
         * Field[1] = smer doprava, atd ...
         */
        private ArrayList<Field[]> moves ;

        public Move() {

        }

        /**
         * Najde posledni pohyb, ktery udelal s konfiguraci reprezentovanou pomoci buggNeighbourhood
         */
        private void findMove(Environment.BuggNeighbourhood buggNeighbourhood) {
            for (Field[] field : moves) {
                boolean found = false ;
                //zkontrolujeme, jestli field (tj. moves[i]) je stejny, jako buggNeighbourhood
                for (int i = 0; i < field.length; i++) {
                    //field = moves[i] = jedna zapamatovana situace
                    //buggNeighbourhood = aktualni situace
                    if (!field[i].equals(buggNeighbourhood.get(i))) {
                        found = false ;
                        break ;
                    }

                    if (i == field.length - 1) {
                        found = true ;
                    }
                }
                if (found) {
                    //TODO neco vrat
                }
            }
        }

        void getNextMove(Environment.BuggNeighbourhood buggNeighbourhood) {
        /*
        Location location = BroukgetLocation() ;
        FieldType ft1 = ...
        FieldType ft2 = ...
        FieldType ft3 = ...
        FieldType ft4 = ...
         */
        }

        void setLastMoveScore() {

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
     * Vrati souradnice, na ktere chce Brouk jit.
     * Samotne posunuti Brouka zpracuje Environment.
     * @return souradnice, na ktere brouk chce jit
     */
    public Direction getNextMove(Environment.BuggNeighbourhood buggNeighbourhood) {
        return Direction.UP ;
        //...
    }

}

enum Direction {
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
