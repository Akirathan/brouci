package brouci;


import java.util.*;

public class Brouk extends Field {

    private class Move {

        /**
         * Nemenna tabulka reprezentujici pohybovou funkci Brouka.
         * f(BuggNeighbourhood) --> Direction
         */
        private Map<BuggNeighbourhood, Direction> tableOfMoves ;


        public Move() {
            tableOfMoves = new TreeMap<>() ;
            initTable();
        }

        /**
         * Vyplni tableOfMoves tim, ze vygeneruje vsechny BuggNeighbourhood
         */
        private void initTable() {
            List<Field> list = Arrays.asList(new Block(), new Free(), new Brouk(), new Food()) ;
            int cycleCounter = 0 ;
            for (Field field1 : list) {
                for (Field field2 : list) {
                    for (Field field3 : list) {
                        for (Field field4 : list) {
                            BuggNeighbourhood buggNeighbourhood = new BuggNeighbourhood(field1, field2, field3, field4) ;
                            tableOfMoves.put(buggNeighbourhood, Direction.getRandomDirection()) ;
                            cycleCounter ++ ;
                        }
                    }
                }
            }
            System.out.println();
        }

        //D
        private void printTable() {
            for (Map.Entry<BuggNeighbourhood,Direction> entry: tableOfMoves.entrySet()) {
                System.out.println(entry.getKey() + " --> " + entry.getValue());
            }
        }
    }

    private Move move ;
    private Coordinate location ;
    private int energy ;


    /**
     * Potrebne pro Move.
     */
    public Brouk() {

    }

    public Brouk(Coordinate location) {
        move = new Move() ;
        energy = 100 ;
        this.location = location ;
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
    public Direction getNextMove(BuggNeighbourhood buggNeighbourhood) {
        Direction direction = null;

        return direction ;
    }

    public void eatFood(Food food) {
        energy += 10 ;
    }

    @Override
    public boolean equals(Object o) {
        return false ;
    }

    @Override
    public String toString() {
        return "Brouk" ;
    }

    @Override
    public int hashCode() {
        return 3 ;
    }

}
