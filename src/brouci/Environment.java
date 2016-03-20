package brouci;


import java.io.*;
import java.util.Map;
import java.util.Random;

public class Environment {

    /**
     * Trida reprezentuje 4 sousedni policka kolem Brouka,
     * to je vse, co Brouk potrebuje videt z Environment.
     */
    class BuggNeighbourhood {
        private Field[] neighbourhood ;

        /**
         * Souradnice, kolem ktere je obestaveny cely BuggNeighbourhood
         */
        private Coordinate centralCoordinate ;

        /**
         * Vyplni vnitrni data BuggNeighbourhoodu podle parametru.
         * @param coordinate souradnice policka, kolem ktereho budeme vytvaret BuggNeighbourhood
         */
        public BuggNeighbourhood(Coordinate coordinate) {
            centralCoordinate = coordinate ;
            neighbourhood = new Field[4] ;
            setUp();
            setRight();
            setDown();
            setLeft();
        }

        private void setUp() {
            neighbourhood[0] = getField(new Coordinate(centralCoordinate.X - 1, centralCoordinate.Y)) ;
        }

        private void setRight() {
            neighbourhood[1] = getField(new Coordinate(centralCoordinate.X, centralCoordinate.Y + 1)) ;
        }

        private void setDown() {
            neighbourhood[1] = getField(new Coordinate(centralCoordinate.X + 1, centralCoordinate.Y)) ;
        }

        private void setLeft() {
            neighbourhood[1] = getField(new Coordinate(centralCoordinate.X, centralCoordinate.Y - 1)) ;
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
    }

    private Field[][] field ;

    /**
     * Vrati obsah policka na danych souradnicich
     */
    Field getField(Coordinate coordinate) {
        return field[coordinate.X][coordinate.Y] ;
    }

    /**
     * Slouzi pro rychle vyhledavani brouku podle souradnic.
     * Potrebne predtim, nez spolu dva brouci zacnou interagovat -
     * na jednoho brouka dostaneme odkaz a druheho brouka musime najit.
     */
    private Map<Coordinate, Brouk> buggMap ;

    public Environment(String filename) {
        loadFromFile(filename);
        generateFood();
        printField();
    }

    public Environment() {
        field = new Field[0][0] ;
    }

    private void loadFromFile(String filename) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename)) ;
            int N = Integer.parseInt(bufferedReader.readLine()) ;
            int M = Integer.parseInt(bufferedReader.readLine()) ;
            field = new Field[N][M] ;
            int i = 0, j = 0 ;
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine() ;
                j = 0 ;
                for (char c : line.toCharArray()) {
                    switch (c) {
                        case ' ' :
                            //field[i][j].setType(FieldType.FREE);
                            field[i][j] = new Free() ;
                            break ;
                        case 'X' :
                            //field[i][j].setType(FieldType.BLOCK);
                            field[i][j] = new Block() ;
                            break ;
                        case 'W' :
                            //field[i][j].setType(FieldType.WATER);
                            field[i][j] = new Water() ;
                            break ;
                        //TODO
                    }
                    j ++ ;
                }
                i ++ ;
            }
        } catch (IOException e) { }
    }

    private void printField() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                System.out.print(field[i][j].print());
            }
            System.out.println();
        }
    }

    private void generateFood() {
        double foodGeneration = 0.7 ; //nastavitelne
        Random random = new Random() ;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] instanceof Free) {
                    if (random.nextDouble() <= foodGeneration) {
                        field[i][j] = new Food() ;
                    }
                }
            }
        }
    }

    /**
     * Vrati dalsiho brouka, ktery je na tahu,
     * nebo null, pokud zadny takovy brouk neexistuje.
     * Vrati se null = nastal konec kola.
     * Na konci kola se muze napriklad zmenit pocasi.
     * TODO prejmenovat.
     * */
    private Brouk getNext() {
        return new Brouk() ;
    }

    public void method() {
        /*
         Brouk[] broukArray ;
         for (Brouk brouk : broukArray ) {
            Move move = brouk.getNextMove() ;
            evaluateMove(move)
         }
        */
        Brouk brouk = getNext() ;

    }

    /**
     * Zpracuje pohyb Brouka.
     * Brouk sam osetruje nemoznost pohnout se na BLOCK (tato metoda to nemusi resit).
     */
    void processMove(Brouk brouk, Direction direction) {
        Coordinate newCoordinate = brouk.getLocation().plus(direction.getCoordinate()) ; //souradnice, kam chce brouk jit
        Field newField = field[newCoordinate.X][newCoordinate.Y] ; //pole, kam chce brouk jit

        //co je na souradnicich, kam se dany brouk chce pohnout?
        if (newField instanceof Block) { //to tu byt nemusi

        }
        else if (newField instanceof Free) {
            brouk.setLocation(newCoordinate); //nastav broukovi nove souradnice
            brouk.lowerEnergy(); //sniz mu energii na zaklade narocnosti terenu (zatim napevno ubira 10 energie)
        }
        else if (newField instanceof Brouk) {
            Brouk bugg = buggMap.get(newCoordinate) ; //vyhledat brouka podle indexu v mape
        }
    }

    /**
     * Zpracovava interakci mezi dvema Broukama.
     */
    private void buggInteraction(Brouk bugg1, Brouk bugg2) {

    }

    /**
     * Vytvori a inicializuje vsechny Brouky.
     */
    void createBuggs() {

    }

    /**
     * Vrati 4 sousedni policka kolem brouk.
     * Pozdeji se tyto 4 sousedni policka tomuto broukovi predaji,
     * na zaklade nich se bude brouk pohybovat.
     * @param brouk
     */
    BuggNeighbourhood getBuggNeighbourhood(Brouk brouk) {
        return new BuggNeighbourhood(new Coordinate(0,0)) ; //TODO
    }
}

