package brouci;

import java.util.Map;
import java.util.TreeMap;

public class Brouk {

    private class Move {

        /**
         * Kazda vetev Trie reprezentuje konfiguraci Brouka, tj.
         * jak je Brouk lokalizovan (co ma nad sebou, pod sebou, atd.).
         */
        private class Trie {

            private class Node {
                private FieldType content ;
                private Map<FieldType, Node> potomci ;

                public Node() {
                    potomci = new TreeMap<>() ;
                    this.content = FieldType.UNKNOWN ;
                }

                public Node(FieldType fieldType) {
                    potomci = new TreeMap<>() ;
                    this.content = fieldType ;
                }

                public FieldType getContent() {
                    return content ;
                }

                /** V potomcich hleda fieldType */
                Node find(FieldType fieldType) {
                    return potomci.get(fieldType) ;
                }

                /** Do potomci prida noveho potomka noda*/
                void addPotomek(Node node) {
                    potomci.put(node.getContent(), node) ;
                }
            }

            private Node actualState ;
            private Node Root ;
            private Node lastMove ;
            private boolean giveRandomResult ;

            public Trie() {
                Root = new Node() ;
                actualState = Root ;
            }

            void setLastMoveScore() {

            }

            /**
             * zmeni actualState.
             */
            void nextState(FieldType fieldType) {
                Node node = actualState.find(fieldType) ; //najde potomka
                if (node == null){
                    //takoveho potomka nema --> musime ho vytvorit
                    giveRandomResult = true ;
                    node = new Node(fieldType) ;
                    actualState.addPotomek(node);
                }
                actualState = node ;
            }

            /**
             * Na zaklade parametru projde trii a vrati dalsi Move.
             * Jestli na konci trie neni zatim zadny zapamatovany vysledek, vrati se nahodny pohyb.
             * @param fieldTypes ma ctyri prvky
             */
            void method(FieldType[] fieldTypes) {
                for (int i = 0; i < fieldTypes.length; i++) {
                    nextState(fieldTypes[i]);
                }
            }
        }

        private Trie trie ;

        public Move() {
            trie = new Trie() ;
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
            trie.setLastMoveScore(); //
        }
    }

    private Move move ;
    private Coordinate location ;


    public Brouk() {
        move = new Move() ;
    }

    public Coordinate getLocation() {
        return location ;
    }

    public void setLocation(Coordinate coordinate) {
        location = coordinate ;
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
