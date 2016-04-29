package brouci;


import java.io.*;
import java.util.*;

public class Environment {


    BufferedWriter debugOutput ;
    private Field[][] field ;
    private GUI gui ;
    private boolean endOfSimulation ;
    /**
     * Pocet ubehnutych kol simulace.
     */
    private int roundCount ;
    /**
     * Doba, po kterou budou brouk cekat potomka.
     * Po tuto dobu se dany brouk nemuze hybat.
     */
    private int buggPregnantTime ;
    /**
     * Slouzi pro rychle vyhledavani brouku podle souradnic.
     * Potrebne predtim, nez spolu dva brouci zacnou interagovat -
     * na jednoho brouka dostaneme odkaz a druheho brouka musime najit.
     */
    private List<Brouk> buggMap ;
    /**
     * Seznam mist, kde jsou hrobecky brouku. Po dalsim kole se
     * z techto hrobecku stanou Free.
     */
    private List<Coordinate> buggGraves ;
    /**
     *
     */
    private List<Brouk> deadBugs ;
    /**
     * Docasny list potomku. Potrebujeme ho, aby se v simulationCycle
     * nerozbil cyklus pres buggMap v momente, kdy do buggMap vlozime
     * noveho potomka. Potomky budeme ukladat do buggChildren a az na
     * konci kola (tj. na konci simulationCycle) je zkopirujeme do
     * buggMap.
     */
    private List<Brouk> buggChildren ;


    public Environment(String filename) {
        try {
            debugOutput = new BufferedWriter(new FileWriter("/afs/ms/u/b/bruchpa/IntelliJ_projects/Brouci/debug.txt")) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        field = new Field[0][0] ;
        buggPregnantTime = 2 ;
        buggMap = new ArrayList<>() ;
        buggGraves = new ArrayList<>() ;
        try {
            loadFromFile(filename);
        } catch (MapFormatException e) {
            loadDefaultFile() ;
        }
        gui = new GUI(this) ;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui.setVisible(true) ;
            }
        });
        generateFood();
        draw() ;
        //simulationCycle(); //DEBUG
    }

    public Environment() {

    }

    public int getRoundCount() {
        return roundCount ;
    }

    /**
     * Nastavi lokaci brouka a aktualizuje buggMap
     * @param brouk
     * @param location
     */
    public void setBroukLocation(Brouk brouk, Coordinate location) {
        Coordinate oldLocation = brouk.getLocation() ;
        brouk.setLocation(location);

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

    public int getBuggPregnantTime() {
        return buggPregnantTime ;
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
     * Vrati 4 sousedni policka kolem brouka.
     * Pozdeji se tyto 4 sousedni policka tomuto broukovi predaji,
     * na zaklade nich se bude brouk pohybovat.
     * @param brouk
     */
    BuggNeighbourhood getBuggNeighbourhood(Brouk brouk) {
        return new BuggNeighbourhood(brouk.getLocation(), this) ;
    }

    /**
     *
     */
    public void addBuggChild(Brouk child) {
        buggChildren.add(child) ;
        field[child.getLocation().X][child.getLocation().Y] = child ;
    }

    /**
     * Potrebna v pripade, kdy loadFromFile vyhodi MapFormatException.
     */
    private void loadDefaultFile() {
        //loadFromFile("default.txt") ;
    }

    /**
     * Naplni field
     * @param filename
     */
    private void loadFromFile(String filename) throws MapFormatException {
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
                    if (i >= N || j >= M) {
                        throw new MapFormatException("Incorrect format of input file") ;
                    }
                    switch (c) {
                        case ' ' :
                            field[i][j] = new Free() ;
                            break ;
                        case 'B' :
                            Coordinate coordinate = new Coordinate(i, j) ;
                            field[i][j] = new Brouk(coordinate, this) ;
                            buggMap.add((Brouk)field[i][j]) ;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void draw() {
        gui.draw();
    }

    private void generateFood() {
        double foodGeneration = 0.1 ; //nastavitelne
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
            //Brouk brouk1 = buggMap.get(newCoordinate) ; //vyhledat brouka podle indexu v mape
            //buggInteraction(brouk, brouk1);
            //todo
        }
        else if (newField instanceof Food) {
            brouk.eatFood((Food)newField);
            setBroukLocation(brouk, newCoordinate);
        }
    }

    /**
     * Zabije brouka. Metoda je volana v momente, kdy prochazime
     * buggMap. Coz znamena, ze brouka nesmime odstranit z buggMap,
     * jinak bychom invalidovali vsechny iteratory, takze ho priradime
     * do seznamu deadBugs.
     * @param bugg brouk, ktery chce zabit, protoze mu
     *             dosla energie.
     */
    public void killBugg(Brouk bugg) {
        deadBugs.add(bugg) ;
        buggChildren.remove(bugg) ; //odsud ho odstranit muzeme, zadny iterator tim neinvalidujeme
        field[bugg.getLocation().X][bugg.getLocation().Y] = new BuggGrave() ; //na misto mrtveho brouka dej hrobecek
        buggGraves.add(bugg.getLocation()) ; //pridej hrobecek do seznamu hrobecku
    }

    /**
     * Odstrani vsechny hrobecky brouku na mape.
     */
    private void removeGraves() {
        for (Coordinate coordinate : buggGraves) {
            field[coordinate.X][coordinate.Y] = new Free() ;
        }
        buggGraves = new ArrayList<>() ;
    }

    public void simulationCycle() {
        double simulationSpeed = 1.0 ; //PROPERTIES
        while(!endOfSimulation) {
            removeGraves();
            buggChildren = new ArrayList<>() ;
            deadBugs = new ArrayList<>() ;

            int bound = buggMap.size() ;
            for (int i = 0; i < bound; i++) {
                buggMap.get(i).decideMove();
            }

            //zkopirujeme buggChildren do buggMap
            for (Brouk child : buggChildren) {
                buggMap.add(child) ;
            }

            //odstranime z buggMap mrtve brouky
            for (Brouk dead : deadBugs) {
                buggMap.remove(dead) ;
            }

            generateFood();
            roundCount++ ;
            draw();

            //pouze v tomhle bloku kodu se da simulationCycle prerusit
            try {
                Thread.sleep(2000); //todo prevod simulationSpeed na milis
            } catch (InterruptedException e) {
                return ;
            }

            record() ;
        }
    }

    private void record() {
        try {
            debugOutput.write("============="); debugOutput.newLine();
            debugOutput.write("round: "+roundCount); debugOutput.newLine();
            debugOutput.write("============="); debugOutput.newLine();
            debugOutput.newLine();
            for (Brouk brouk : buggMap) {
                debugOutput.write("======(brouk)======") ; debugOutput.newLine();
                debugOutput.write("ID: " + brouk.ID); debugOutput.newLine();
                debugOutput.write("location: " + brouk.getLocation()); debugOutput.newLine();
                debugOutput.write("energy: " + brouk.getEnergy()); debugOutput.newLine();
                debugOutput.write("state: " + brouk.getState()); debugOutput.newLine();
                debugOutput.write("age: " + brouk.getAge()); debugOutput.newLine();
            }
            debugOutput.newLine();
            debugOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


class MapFormatException extends Exception {
    MapFormatException(String message) {
        super(message) ;
    }
}

//zkouska
class Kontejner<T> implements Iterable<T>{

    class Prochazec implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public T next() {
            return null;
        }
    }

    private List<T> list ;
    /**
     * Ukazuje na index listu, kam az sahaji
     * validni prvky - tj. ty, ktere nebyly
     * pridany po cas iterace.
     */
    private int validIndex ;

    public Kontejner() {
        list = new ArrayList<>() ;
    }

    public void add(T t) {
        list.add(t) ;
        validIndex++ ;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }
}

