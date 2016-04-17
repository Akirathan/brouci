package brouci;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bruchpa on 15.4.16.
 */
public class GUI extends JFrame{

    private JLabel[][] labelField;

    /**
     * field si rovnou ulozi od Environment v konstruktoru
     */
    private Field[][] field ;
    private JPanel jPanel1 ;
    private JPanel broukProperties;
    private JButton pauseButton ;
    private JButton startButton ;
    private Environment environment ;
    private Worker worker ;

    private class Worker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            environment.simulationCycle();
            return null;
        }
    }

    public GUI(Field[][] field, Environment environment) {
        this.field = field ;
        this.environment = environment ;
        worker = new Worker() ;
        initComponents();
        createPanel(field.length, field[0].length);
    }

    public void drawField() {
        for (int i = 0; i < labelField.length; i++) {
            for (int j = 0; j < labelField[0].length; j++) {
                if (field[i][j] instanceof Brouk) {
                    labelField[i][j].setBackground(new Color(150,0,0));
                }
                else if (field[i][j] instanceof Block) {
                    labelField[i][j].setBackground(new Color(0, 0, 0));
                }
                else if (field[i][j] instanceof Free) {
                    labelField[i][j].setBackground(new Color(0, 100, 0));
                }
                else if (field[i][j] instanceof Food) {
                    labelField[i][j].setBackground(new Color(200, 200, 100));
                }
            }
        }
    }


    //N, M rozmery broukovnice
    private void createPanel(int N, int M) {
        //jPanel1 = new JPanel(new GridLayout(N, M)) ;
        jPanel1.setLayout(new GridLayout(N,M));

        labelField = new JLabel[N][M] ;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                labelField[i][j] = new JLabel(" "+i + "" + j+" ") ;
                labelField[i][j].setForeground(new Color(255, 255, 255)); //barva pisma
                labelField[i][j].setOpaque(true); //znepruhledni
                labelField[i][j].setHorizontalAlignment(SwingConstants.CENTER); //vycentruje text
            }
        }

        //add labelField to jPanel1
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                jPanel1.add(labelField[i][j]) ;
            }
        }


    }

    private void initComponents() {

        pauseButton = new JButton();
        jPanel1 = new JPanel();
        broukProperties = new JPanel();
        startButton = new JButton();

        //pridano
        startButton.addActionListener(
                (ActionEvent event) -> {
                    worker = new Worker() ;
                    worker.execute();
                }
        );

        pauseButton.addActionListener(
                (ActionEvent event) -> {
                    worker.cancel(true) ;
                }
        );
        //---

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pauseButton.setText("Pause");

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 480, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 383, Short.MAX_VALUE)
        );

        GroupLayout BroukPropertiesLayout = new GroupLayout(broukProperties);
        broukProperties.setLayout(BroukPropertiesLayout);
        BroukPropertiesLayout.setHorizontalGroup(
                BroukPropertiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 151, Short.MAX_VALUE)
        );
        BroukPropertiesLayout.setVerticalGroup(
                BroukPropertiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 246, Short.MAX_VALUE)
        );

        startButton.setText("Start");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(startButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(pauseButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(broukProperties, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(76, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(pauseButton)
                                        .addComponent(startButton))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(broukProperties, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(118, Short.MAX_VALUE))
        );

        pack();
    }

}
