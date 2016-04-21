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
         * Zkontroluje, jestli je zadany stav blokujici, tj. stavy jako
         * [Block, Brouk, Block, Block], ...
         * @param buggNeighbourhood
         * @return
         */
        private boolean isBlocked(BuggNeighbourhood buggNeighbourhood) {
            for (int i = 0; i < 4; i++) {
                if (!(buggNeighbourhood.get(i) instanceof Block ||
                        buggNeighbourhood.get(i) instanceof Brouk))
                    return false ;
            }
            return true ;
        }

        /**
         * Vyplni tableOfMoves tim, ze vygeneruje vsechny BuggNeighbourhood
         */
        private void initTable() {
            List<Field> list = Arrays.asList(new Block(), new Free(), new Brouk(), new Food()) ;
            for (Field field1 : list) {
                for (Field field2 : list) {
                    for (Field field3 : list) {
                        for (Field field4 : list) {
                            BuggNeighbourhood buggNeighbourhood = new BuggNeighbourhood(field1, field2, field3, field4) ;
                            Direction direction = Direction.getRandomDirection() ;
                            Field field = buggNeighbourhood.get(direction.ordinal()) ; //field = pole, kam mame namireno

                            if (isBlocked(buggNeighbourhood)) {
                                tableOfMoves.put(buggNeighbourhood, Direction.NONE) ;
                                continue ;
                            }

                            //zkontroluje, jestli direction nemiri do Blocku
                            //tj. generuje smery dokud miri do Blocku
                            while (field instanceof Block || field instanceof Brouk) {
                                direction = Direction.getRandomDirection() ;
                                field = buggNeighbourhood.get(direction.ordinal()) ;
                            }
                            tableOfMoves.put(buggNeighbourhood, direction);
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

        public void move() {
            BuggNeighbourhood actualLocation = new BuggNeighbourhood(location, environment) ;
            Direction direction = tableOfMoves.get(actualLocation) ; //najdi smer, kam se Brouk chce vydat
            Coordinate newCoordinate = location.plus(direction.getCoordinate()) ; //vypocte nove souradnice
            Field newField = environment.getField(newCoordinate) ;

            if (newField instanceof Free) {
                environment.setBroukLocation(Brouk.this, newCoordinate);
                lowerEnergy();
            }
            else if (newField instanceof Food) {
                environment.setBroukLocation(Brouk.this, newCoordinate);
                eatFood((Food)newField);
            }
        }
    }

    private Move move ;
    private Coordinate location ;
    private int energy ;

    /**
     * Brouk ma referenci na Environment kvuli tomu, aby
     * se mohl sam pohybovat a nemusel s nim hybat Environment.
     */
    private Environment environment ;


    /**
     * Potrebne pro Move.
     */
    public Brouk() {

    }

    public Brouk(Coordinate location, Environment environment) {
        this.environment = environment ;
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

    public int getEnergy() {
        return energy ;
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
    @Deprecated
    public Direction getNextMove(BuggNeighbourhood buggNeighbourhood) {
        Direction direction = null;

        return direction ;
    }

    public void move() {
        move.move() ;
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
