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
            tableOfMoves = geneticInformation.getTableOfMoves() ;
            //tableOfMoves = new TreeMap<>() ;
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

        /**
         * Smicha genetickou informaci dvou Brouku - tj. nahodne vybira prvky od otce a matky do
         * tableOfMoves potomka (potomek je jeste neexistujici objekt)
         * @param otherTable
         * @return
         */
        private Map<BuggNeighbourhood, Direction> mixTables(Map<BuggNeighbourhood, Direction> otherTable) {
            Random random = new Random() ;
            Map<BuggNeighbourhood, Direction> ret = new TreeMap<>() ;
            for (Map.Entry<BuggNeighbourhood,Direction> entry : this.tableOfMoves.entrySet()) {
                double num = random.nextDouble() ;
                if (num > 0.5) {
                    ret.put(entry.getKey(), otherTable.get(entry.getKey())) ; //vlozime zaznam z cizi tabulky
                }
                else {
                    ret.put(entry.getKey(), entry.getValue()) ; //vlozime zaznam z nasi tabulky
                }
            }
            return ret ;
        }


        private void printTable() {
            for (Map.Entry<BuggNeighbourhood,Direction> entry: tableOfMoves.entrySet()) {
                System.out.println(entry.getKey() + " --> " + entry.getValue());
            }
        }

        /**
         * Pohne Broukem. Nemusime osetrovat pripady, kdy se Brouk chce pohnout do Blocku,
         * ty vyresime uz v metode initTable.
         */
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


    private class GeneticInformation {
        private Map<BuggNeighbourhood, Direction> tableOfMoves ;

        public Map<BuggNeighbourhood, Direction> getTableOfMoves() {
            return tableOfMoves ;
        }

        public GeneticInformation() {
            move.initTable();
        }

        public GeneticInformation(GeneticInformation otherInformation) {
            move.mixTables(otherInformation.tableOfMoves) ;
        }
    }


    private GeneticInformation geneticInformation ;
    public enum buggState_t {PREGNANT, NORMAL}
    /**
     * Zbyvajici cas, po ktery bude brouk cekat potomka.
     */
    private int pregnantTime ;
    private Move move ;
    private Coordinate location ;
    private int energy ;
    private buggState_t state ;

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
        energy = 200 ;
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

    public Map<BuggNeighbourhood, Direction> getTableOfMoves() {
        return move.tableOfMoves ;
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

    /**
     * Zpracuje pohyb brouka. Pokud brouk ceka potomka,
     * tak snizi pregnantTime, jinak se hybne.
     */
    public void move() {
        if (state == buggState_t.PREGNANT) {
            pregnantTime -- ;
            if (pregnantTime == 0) {
                giveBirth() ;
            }
        }
        else {
            move.move();
        }
    }

    public void eatFood(Food food) {
        energy += 10 ;
    }

    /**
     * Je tento Brouk vhodny partner k pareni?
     * @return
     */
    private boolean isProperPartner() {
        return energy >= 60 ;
    }

    /**
     * Zpracuje interakci dvou brouku. Podiva se, jestli v okolnim BuggNeighbourhood
     * je nejaky jiny Brouk, jestli ano, tak zkontroluje, jestli je to vhodny partner
     * k pareni a popripade se s nim spari.
     */
    public void buggInteraction() {
        BuggNeighbourhood actualLocation = new BuggNeighbourhood(location, environment) ;
        for (int i = 0; i < 4; i++) {
            if (actualLocation.get(i) instanceof Brouk) {
                Brouk brouk = (Brouk)actualLocation.get(i) ;
                if (this.isProperPartner() && brouk.isProperPartner()) { //jestli jsou brouci schopni pareni
                    this.initMating(brouk);
                }
            }
        }
    }

    /**
     * Zpracuje vznik noveho potomka.
     * Matka da potomkovi nahodne mnozstvi energie.
     */
    private void giveBirth() {

    }

    /**
     * Zapocne pareni dvou brouku.
     * this vystupuje jako otec a brouk jako matka.
     * @param brouk
     */
    private void initMating(Brouk brouk) {
        brouk.state = buggState_t.PREGNANT ;
        brouk.pregnantTime = environment.getBuggPregnantTime() ; //nastavime matce cas cekani na potomka
        Map<BuggNeighbourhood, Direction> newTableOfMoves = this.move.mixTables(brouk.getTableOfMoves()) ; //mix geneticke informace
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


