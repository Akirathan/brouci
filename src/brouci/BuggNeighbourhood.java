package brouci;

/**
 * Trida reprezentuje 4 sousedni policka kolem Brouka,
 * to je vse, co Brouk potrebuje videt z Environment.
 */
class BuggNeighbourhood implements Comparable<BuggNeighbourhood> {
    private Field[] neighbourhood ;

    /**
     * Souradnice, kolem ktere je obestaveny cely BuggNeighbourhood
     */
    private Coordinate centralCoordinate ;
    private Environment environment ;


    private BuggNeighbourhood() {
        neighbourhood = new Field[4] ;
    }
    /**
     * Vyplni vnitrni data BuggNeighbourhoodu podle parametru.
     * @param coordinate souradnice policka, kolem ktereho budeme vytvaret BuggNeighbourhood
     */
    public BuggNeighbourhood(Coordinate coordinate, Environment environment) {
        this() ;
        this.environment = environment ;
        centralCoordinate = coordinate ;
        //neighbourhood = new Field[4] ;
        setUp();
        setRight();
        setDown();
        setLeft();
    }

    /**
     * Tenhle konstruktor nepotrebuje Environment, potrebne pro generovani
     * vsech moznych BuggNeighbourhoodu z Brouk.Move
     * @param up
     * @param right
     * @param down
     * @param left
     */
    public BuggNeighbourhood(Field up, Field right, Field down, Field left) {
        this() ;
        neighbourhood[0] = up ;
        neighbourhood[1] = right ;
        neighbourhood[2] = down ;
        neighbourhood[3] = left ;
    }

    private void setUp() {
        neighbourhood[0] = environment.getField(new Coordinate(centralCoordinate.X - 1, centralCoordinate.Y)) ;
    }

    private void setRight() {
        neighbourhood[1] = environment.getField(new Coordinate(centralCoordinate.X, centralCoordinate.Y + 1)) ;
    }

    private void setDown() {
        neighbourhood[2] = environment.getField(new Coordinate(centralCoordinate.X + 1, centralCoordinate.Y)) ;
    }

    private void setLeft() {
        neighbourhood[3] = environment.getField(new Coordinate(centralCoordinate.X, centralCoordinate.Y - 1)) ;
    }

    public Coordinate getCentralCoordinate() {
        return centralCoordinate ;
    }

    /**
     * Vrati sousedni pole podle zadaneho smeru.
     * 0 = nahore
     * 1 = vpravo, atd ..
     */
    public Field get(int direction) {
        switch (direction) {
            case 0 :
                return getUp() ;
            case 1 :
                return getRight() ;
            case 2 :
                return  getDown() ;
            case 3 :
                return getLeft() ;
        }
        return new Block() ;
    }

    /**
     * Vrati to, co je v neighbourhood nahore
     */
    private Field getUp() {
        return neighbourhood[0] ;
    }

    private Field getRight() {
        return neighbourhood[1] ;
    }

    private Field getDown() {
        return neighbourhood[2] ;
    }

    private Field getLeft() {
        return neighbourhood[3] ;
    }

    /**
     * Zatim nejednoznacny
     * @return
     */
    @Override
    public int hashCode() {
        return getUp().hashCode() + 3*getRight().hashCode() + 5*getDown().hashCode() + 7*getLeft().hashCode() ;
    }

    /**
     * Potrebne pri geneticke vymene rodicu -
     * budou se prochazet obe tabulky Brouk.move.tableOfMoves
     * @param other
     * @return
     */
    @Override
    public int compareTo(BuggNeighbourhood other) {
        for (int i = 0; i < neighbourhood.length; i++) {
            if (neighbourhood[i].hashCode() < other.neighbourhood[i].hashCode()) {
                return -1 ; //this < other
            }
            else if (neighbourhood[i].hashCode() > other.neighbourhood[i].hashCode()) {
                return 1 ;
            }
        }
        return 0 ;
    }

    @Override
    public boolean equals(Object o) {
        BuggNeighbourhood other = (BuggNeighbourhood)o ;
        if (other == null) return false ;
        else {
            if (this.compareTo(other) == 0) return true ;
            else return false ;
        }
    }

    @Override
    public String toString() {
        return "[" + neighbourhood[0] + ", " + neighbourhood[1] + ", " + neighbourhood[2] + ", " + neighbourhood[3] + "]" ;
    }
}