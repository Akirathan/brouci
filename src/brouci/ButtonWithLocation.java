package brouci;

import javax.swing.*;

/**
 * Created by bruchpa on 21.4.16.
 */
public class ButtonWithLocation extends JButton {
    private Coordinate location ;

    public ButtonWithLocation(Coordinate coordinate) {
        this.location = coordinate ;
    }

    public ButtonWithLocation(String str, Coordinate coordinate) {
        super(str) ;
        this.location = coordinate ;
    }

    public Coordinate getCoordinate() {
        return location ;
    }
}
