package brouci;


public abstract class Field {

    public Field() {

    }


    @Override
    public abstract boolean equals(Object o) ;
}

class Block extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Block ;
    }

    @Override
    public int hashCode() {
        return 1 ;
    }

    @Override
    public String toString() {
        return "Block" ;
    }
}

class Water extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Water ;
    }

    @Override
    public String toString() {
        return "Water" ;
    }
}

class Food extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Food ;
    }

    @Override
    public int hashCode() {
        return 4 ;
    }

    @Override
    public String toString() {
        return "Food" ;
    }
}

class Free extends Field {

    @Override
    public boolean equals(Object o) {
        return o instanceof Free ;
    }

    @Override
    public int hashCode() {
        return 2 ;
    }

    @Override
    public String toString() {
        return "Free" ;
    }
}
