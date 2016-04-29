package brouci;

/**
 * Created by bruchpa on 19.4.16.
 */
public class Coordinate implements Comparable<Coordinate> {
    public int X, Y ;

    public Coordinate plus(Coordinate other) {
        return new Coordinate(this.X + other.X, this.Y + other.Y) ;
    }

    Coordinate(int X, int Y) {
        this.X = X ;
        this.Y = Y ;
    }

    @Override
    public int compareTo(Coordinate o) {
        if (this.X < o.X ||
                (this.X == o.X && this.Y < o.Y))
            return -1 ;
        else if (this.X > o.X ||
                (this.X == o.X && this.Y > o.Y))
            return 1 ;
        else return 0 ;
    }

    @Override
    public String toString() {
        return "[" + X + "," + Y + "]" ;
    }
}
