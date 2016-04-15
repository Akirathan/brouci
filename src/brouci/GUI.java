package brouci;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bruchpa on 15.4.16.
 */
public class GUI extends JFrame{

    private JButton[][] buttonField ;

    /**
     * field si rovnou ulozi od Environment v konstruktoru
     */
    private Field[][] field ;
    private JPanel jPanel1 ;

    public GUI() {
        initComponents();
        createPanel(10,10);
        add(jPanel1) ;
        pack();
        setVisible(true);
    }

    public void printField(Field[][] field) {
        for (int i = 0; i < buttonField.length; i++) {
            for (int j = 0; j < buttonField[0].length; j++) {
                if (field[i][j] instanceof Brouk) {
                    buttonField[i][j].setBackground(new Color(150,0,0));
                }
                else if (field[i][j] instanceof Block) {
                    buttonField[i][j].setBackground(new Color(0, 0, 0));
                }
                else if (field[i][j] instanceof Free) {
                    buttonField[i][j].setBackground(new Color(0, 100, 0));
                }
            }
        }
    }


    //N, M rozmery broukovnice
    private void createPanel(int N, int M) {
        jPanel1 = new JPanel(new GridLayout(N, M)) ;

        buttonField = new JButton[N][M] ;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                buttonField[i][j] = new JButton(i + " " + j) ;
            }
        }

        //fill panel
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                jPanel1.add(buttonField[i][j]) ;
            }
        }
    }

    private void initComponents() {

    }
}
