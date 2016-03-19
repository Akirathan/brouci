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

    }

    private Field[][] field ;

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
                /*if (field[i][j].getType() == FieldType.FREE) {
                    if (random.nextDouble() <= foodGeneration) {
                        field[i][j].setType(FieldType.FOOD);
                    }
                }*/
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
        Coordinate newCoordinate = brouk.getLocation().plus(direction.getCoordinate()) ;
        Field newField = field[newCoordinate.X][newCoordinate.Y] ;

        //co je na souradnicich, kam se dany brouk chce pohnout?
        switch (newField.getType()) {
            case BLOCK: //to tu byt nemusi
                break ;
            case FREE:
                brouk.setLocation(newCoordinate); //TODO
                break ;
            case BUGG:
                Brouk bugg = buggMap.get(newCoordinate) ; //vyhledat brouka podle indexu v mape
                //...
                break ;
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
        return new BuggNeighbourhood() ;
    }
}

