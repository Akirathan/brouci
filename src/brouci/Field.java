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

    @Override
    public abstract boolean equals(Object o) ;
}

class Block extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Block ;
    }
}

class Water extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Water ;
    }
}

class Food extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Food ;
    }
}

class Free extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Free ;
    }
}
