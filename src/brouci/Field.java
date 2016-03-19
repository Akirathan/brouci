package brouci;

public abstract class Field {
    //private FieldType type ;

    public Field() {
        //this.type = FieldType.UNKNOWN ;
    }

    /*
    public FieldType getType() {
        return this.type ;
    }

    public void setType(FieldType type) {
        this.type = type ;
    } */

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

        /*
        switch (type) {
            case BLOCK:
                return 'X' ;
            case FOOD:
                return 'F' ;
            case WATER:
                return 'W' ;
            case FREE:
                return ' ' ;
        }
        return ' ' ; */
    }
}

class Block extends Field {

}

class Water extends Field {

}

class Food extends Field {

}

class Free extends Field {

}
