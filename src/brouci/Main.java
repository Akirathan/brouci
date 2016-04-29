package brouci;


import javax.swing.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        /*Environment environment = new Environment("/afs/ms/u/b/bruchpa/zk.txt") ;
        System.out.println();*/

        /*BuggNeighbourhood buggNeighbourhood1 = new BuggNeighbourhood(new Free(), new Block(), new Block(), new Block()) ;
        BuggNeighbourhood buggNeighbourhood2 = new BuggNeighbourhood(new Block(), new Block(), new Block(), new Free()) ;
        buggNeighbourhood1.equals(buggNeighbourhood2) ;

        Field field1 = new Block() ;
        Field field2 = new Block() ;
        System.out.println();*/

        List<String> list = new ArrayList<>() ;
        list.add("jedna") ;
        list.add("dva") ;
        list.add("tri") ;
        int bound = list.size() ;
        for (int i = 0; i < bound; i++) {
            System.out.println(list.get(i));
            list.add("novy " + i) ;
        }
        System.out.println();
    }
}

