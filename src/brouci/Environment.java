package brouci;


import java.io.*;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Environment {

    private Field[][] field ;
    private GUI gui ;
    private boolean endOfSimulation ;

    /**
     * Slouzi pro rychle vyhledavani brouku podle souradnic.
     * Potrebne predtim, nez spolu dva brouci zacnou interagovat -
     * na jednoho brouka dostaneme odkaz a druheho brouka musime najit.
     */
    private Map<Coordinate, Brouk> buggMap ;

    public Environment(String filename) {
        buggMap = new TreeMap<>() ;
        loadFromFile(filename);
        gui = new GUI(this) ;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true) ;
            }
        });
        generateFood();
        drawField() ;
        //simulationCycle(); //DEBUG
    }

    public Environment() {
        field = new Field[0][0] ;
    }

    /**
     * Nastavi lokaci brouka a aktualizuje buggMap
     * @param brouk
     * @param location
     */
    public void setBroukLocation(Brouk brouk, Coordinate location) {
        Coordinate oldLocation = brouk.getLocation() ;
        brouk.setLocation(location);
        buggMap.remove(oldLocation) ;
        buggMap.put(location, brouk) ;

        //zmen field
        field[oldLocation.X][oldLocation.Y] = new Free() ;
        field[location.X][location.Y] = brouk ;
    }

    public int getFieldWidth() {
        return field[0].length ;
    }

    public int getFieldHeight() {
        return field.length ;
    }

    /**
     * Vrati obsah policka na danych souradnicich.
     * Pokud jsou souradnice mimo meze, tak vraci Block.
     */
    public Field getField(Coordinate coordinate) {
        if (coordinate.X >= field.length || coordinate.Y >= field[0].length ||
                coordinate.X < 0 || coordinate.Y < 0)
            return new Block() ;

        else return field[coordinate.X][coordinate.Y] ;
    }

    /**
     * Naplni field
     * @param filename
     */
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
                            field[i][j] = new Free() ;
                            break ;
                        case 'B' :
                            Coordinate coordinate = new Coordinate(i, j) ;
                            field[i][j] = new Brouk(coordinate, this) ;
                            buggMap.put(coordinate, (Brouk)field[i][j]) ;
                            break ;
                        case 'X' :
                            field[i][j] = new Block() ;
                            break ;
                        case 'W' :
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

    private void drawField() {
        gui.drawField();
    }

    private void generateFood() {
        double foodGeneration = 0.3 ; //nastavitelne
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
    private Brouk getNextBrouk() {
        return null ;
    }


    /**
     * Zpracuje pohyb Brouka.
     * Brouk sam osetruje nemoznost pohnout se na BLOCK (tato metoda to nemusi resit).
     */
    @Deprecated
    private void processMove(Brouk brouk, Direction direction) {
        Coordinate newCoordinate = brouk.getLocation().plus(direction.getCoordinate()) ; //souradnice, kam chce brouk jit
        Field newField = field[newCoordinate.X][newCoordinate.Y] ; //pole, kam chce brouk jit

        //co je na souradnicich, kam se dany brouk chce pohnout?
        if (newField instanceof Block) { //to tu byt nemusi

        }
        else if (newField instanceof Free) {
            brouk.lowerEnergy(); //sniz mu energii na zaklade narocnosti terenu (zatim napevno ubira 10 energie)
            setBroukLocation(brouk, newCoordinate); //nastavit novou lokaci brouka
        }
        else if (newField instanceof Brouk) {
            Brouk brouk1 = buggMap.get(newCoordinate) ; //vyhledat brouka podle indexu v mape
            buggInteraction(brouk, brouk1);
            //TODO
        }
        else if (newField instanceof Food) {
            brouk.eatFood((Food)newField);
            setBroukLocation(brouk, newCoordinate);
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
     * Vrati 4 sousedni policka kolem brouka.
     * Pozdeji se tyto 4 sousedni policka tomuto broukovi predaji,
     * na zaklade nich se bude brouk pohybovat.
     * @param brouk
     */
    BuggNeighbourhood getBuggNeighbourhood(Brouk brouk) {
        return new BuggNeighbourhood(brouk.getLocation(), this) ;
    }

    public void simulationCycle() {
        double simulationSpeed = 1.0 ; //PROPERTIES
        while(!endOfSimulation) {
            for (Map.Entry<Coordinate,Brouk> entry : buggMap.entrySet()) {
                BuggNeighbourhood buggNeighbourhood = getBuggNeighbourhood(entry.getValue()); //sousedni policka daneho brouka
                entry.getValue().move();
            }
            drawField();

            //pouze v tomhle bloku kodu se da simulationCycle prerusit
            try {
                Thread.sleep(2000); //todo prevod simulationSpeed na milis
            } catch (InterruptedException e) {
                System.out.println("simulationCycle interrupted.");
                return ;
            }
        }
    }

}

