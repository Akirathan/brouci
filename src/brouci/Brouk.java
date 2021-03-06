package brouci;


import java.util.*;

public class Brouk extends Field {

    private class GeneticInformation {
        private Map<BuggNeighbourhood, Direction> tableOfMoves ;

        public Map<BuggNeighbourhood, Direction> getTableOfMoves() {
            return tableOfMoves ;
        }

        /**
         * Konstruktor umele vytvoreneho brouka.
         */
        public GeneticInformation() {
            tableOfMoves = new TreeMap<>() ;
            initTable();
        }

        /**
         * Vytvori genetickou informati noveho potomka tim, ze
         * smicha tabulky otce a matky.
         */
        public GeneticInformation(GeneticInformation mother, GeneticInformation father) {
            tableOfMoves = mixTables(mother.getTableOfMoves(), father.getTableOfMoves()) ;
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
         * Smicha genetickou informaci dvou Brouku - tj. nahodne vybira prvky od otce a matky do
         * tableOfMoves potomka (potomek je jeste neexistujici objekt)
         * @return
         */
        private Map<BuggNeighbourhood, Direction> mixTables(Map<BuggNeighbourhood, Direction> motherTable, Map<BuggNeighbourhood, Direction> fatherTable) {
            Random random = new Random() ;
            Map<BuggNeighbourhood, Direction> ret = new TreeMap<>() ;
            for (Map.Entry<BuggNeighbourhood,Direction> entry : motherTable.entrySet()) {
                double num = random.nextDouble() ;
                if (num > 0.5) {
                    ret.put(entry.getKey(), motherTable.get(entry.getKey())) ; //vlozime zaznam z cizi tabulky
                }
                else {
                    ret.put(entry.getKey(), fatherTable.get(entry.getKey())) ; //vlozime zaznam z nasi tabulky
                }
            }
            return ret ;
        }
    }


    private GeneticInformation geneticInformation ;
    public enum buggState_t {PREGNANT, NORMAL}
    /**
     * Zbyvajici cas, po ktery bude brouk cekat potomka.
     */
    private int pregnantTime ;
    private Coordinate location ;
    private int energy ;
    private buggState_t state ;
    private int age ;
    /**
     * Jednoznacky identifikator brouka.
     */
    public int ID ;
    public static int IDCounter ;
    private int numberOfChildrenAsMother;
    private int numberOfChildrenAsFather;
    /**
     * Brouk ma referenci na Environment kvuli tomu, aby
     * se mohl sam pohybovat a nemusel s nim hybat Environment.
     */
    private Environment environment ;
    /**
     * Geneticka informace pro budouciho potomka.
     */
    private GeneticInformation childInformation ;


    /**
     * Potrebne pro Move.
     */
    public Brouk() {

    }

    public Brouk(Coordinate location, Environment environment) {
        ID = IDCounter++ ;
        this.environment = environment ;
        this.location = location ;
        energy = 200 ;
        geneticInformation = new GeneticInformation() ;
        state = buggState_t.NORMAL ;
    }

    public Brouk(Coordinate location, Environment environment, int energy, GeneticInformation geneticInformation) {
        ID = IDCounter++ ;
        this.environment = environment ;
        this.location = location ;
        this.energy = energy ;
        this.geneticInformation = geneticInformation ;
        state = buggState_t.NORMAL ;
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

    public int getAge() {
        return age ;
    }

    public int getNumberOfChildrenAsMother() {
        return numberOfChildrenAsMother ;
    }

    public int getNumberOfChildrenAsFather() {
        return numberOfChildrenAsFather ;
    }

    public buggState_t getState() {
        return state ;
    }

    public GeneticInformation getGeneticInformation() {
        return geneticInformation ;
    }

    /**
     * Snizi energii o konstantu.
     * Cim vyssi je vek, tim vic energie ubira
     */
    public void lowerEnergy() {
        energy -= 10 + age/2 ;
        if (energy < 0) {
            environment.killBugg(this) ;
        }
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
     * Pohne Broukem. Nemusime osetrovat pripady, kdy se Brouk chce pohnout do Blocku,
     * ty vyresime uz v metode initTable.
     */
    private void move() {
        BuggNeighbourhood actualLocation = new BuggNeighbourhood(location, environment) ;
        Direction direction = geneticInformation.getTableOfMoves().get(actualLocation) ; //najdi smer, kam se Brouk chce vydat
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

    /**
     * Zpracuje pohyb brouka. Pokud brouk ceka potomka,
     * tak snizi pregnantTime, jinak se hybne.
     */
    public void decideMove() {
        age ++ ;
        BuggNeighbourhood actualLocation = new BuggNeighbourhood(location, environment) ;
        if (state == buggState_t.PREGNANT) {
            energy -= 10 ;
            pregnantTime -- ;
            if (pregnantTime == 0) {
                giveBirth() ;
            }
        }
        else {
            for (int i = 0; i < 4; i++) {
                if (actualLocation.get(i) instanceof Brouk) {
                    Brouk brouk = (Brouk)actualLocation.get(i) ;
                    if (this.isProperPartner() && brouk.isProperPartner()) { //jestli jsou brouci schopni pareni
                        this.initMating(brouk);
                        return ;
                    }
                }
            }
            move();
        }
    }

    /**
     * Sni potravu a zvysi energii.
     * Cim je brouk starsi, tim mene ergie ziska.
     */
    public void eatFood(Food food) {
        energy += 20 - age/10 ;
    }

    /**
     * Je tento Brouk vhodny partner k pareni?
     * @return
     */
    private boolean isProperPartner() {
        return age >= 3 && energy >= 110 && state == buggState_t.NORMAL;
    }

    /**
     * Zpracuje vznik noveho potomka.
     * Matka da potomkovi nahodne mnozstvi energie.
     */
    private void giveBirth() {
        Direction direction = Direction.getRandomDirection() ;
        Coordinate childLocation = this.location.plus(direction.getCoordinate()) ;

        //najdi random misto v sousedstvi pro potomka
        while (!(environment.getField(childLocation) instanceof Free)) {
            direction = Direction.getRandomDirection() ;
            childLocation = this.location.plus(direction.getCoordinate()) ;
        }

        //vyber nahodne mnozstvi energie
        Random random = new Random() ;
        int giveEnergy = random.nextInt(this.energy - 30) ;
        energy -= giveEnergy ;

        //vytvor noveho potomka
        environment.addBuggChild(new Brouk(childLocation, environment, giveEnergy, childInformation));
        numberOfChildrenAsMother++ ;

        this.state = buggState_t.NORMAL ;
    }

    /**
     * Zapocne pareni dvou brouku.
     * this vystupuje jako otec a brouk jako matka.
     * @param brouk
     */
    private void initMating(Brouk brouk) {
        brouk.state = buggState_t.PREGNANT ;
        brouk.pregnantTime = environment.getBuggPregnantTime() ; //nastavime matce cas cekani na potomka
        brouk.childInformation = new GeneticInformation(brouk.geneticInformation, this.geneticInformation) ; //nastavime matce genetickou informaci pro potomka

        //predame matce nahodne mnozstvi energie
        int giveEnergy = new Random().nextInt(this.energy - 20) ;
        brouk.energy += giveEnergy ;
        this.energy -= giveEnergy ;

        this.numberOfChildrenAsFather++ ;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Brouk) {
            return ((Brouk)o).ID == this.ID ;
        }
        else
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


