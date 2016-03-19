package brouci;

import java.util.Random;

public abstract class Field {

    public Field() {

    }

    char print() {
        if (Block.class.isAssignableFrom(this.getClass())) {
            return 'X' ;
        }
        else if (Water.class.isAssignableFrom(this.getClass())) {
            return 'W' ;
        }
        else if (Food.class.isAssignableFrom(this.getClass())) {
            return 'F' ;
        }
        else if (Free.class.isAssignableFrom(this.getClass())) {
            return ' ' ;
        }
        return ' ';
    }
}

class Block extends Field {

}

class Water extends Field {

}

class Food extends Field {

}

class Free extends Field {
    /**
     * Nahodne generovan.
     */
    //int terrainSeverity ;

    public Free() {
        /*Random random = new Random() ;
        terrainSeverity = random.nextInt(30) ;*/
    }
}
