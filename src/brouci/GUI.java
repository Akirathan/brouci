package brouci;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by bruchpa on 15.4.16.
 */
public class GUI extends JFrame{


    private JPanel buggPanel;
    //--- komponenty patrici do buggPanel ---
    private ButtonWithLocation[][] buggField;
    //---------------------------------------


    private JPanel buggPropertiesPanel;
    //--- komponenty patrici do buggPropertiesPanel ---
    private JLabel energyLabel ;
    //-------------------------------------------------


    private JButton pauseButton ;
    private JButton startButton ;
    private JLabel stateLabel ;
    private Environment environment ;
    private Worker worker ;
    private enum simulationState_t {RUNNING, PAUSED, INITIALIZED}  ;
    private simulationState_t simulationState = simulationState_t.INITIALIZED ;

    private class Worker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            environment.simulationCycle();
            return null;
        }
    }

    public GUI(Environment environment) {
        this.environment = environment ;
        worker = new Worker() ;
        initComponents();
        createBuggPanel(environment.getFieldHeight(), environment.getFieldWidth());
        addListeners();
    }

    public void drawField() {
        for (int i = 0; i < buggField.length; i++) {
            for (int j = 0; j < buggField[0].length; j++) {
                Coordinate coordinate = new Coordinate(i,j) ;
                if (environment.getField(coordinate) instanceof Brouk) {
                    buggField[i][j].setBackground(new Color(150,0,0));
                }
                else if (environment.getField(coordinate) instanceof Block) {
                    buggField[i][j].setBackground(new Color(0, 0, 0));
                }
                else if (environment.getField(coordinate) instanceof Free) {
                    buggField[i][j].setBackground(new Color(0, 100, 0));
                }
                else if (environment.getField(coordinate) instanceof Food) {
                    buggField[i][j].setBackground(new Color(200, 200, 100));
                }
            }
        }
    }


    //N, M rozmery broukovnice
    private void createBuggPanel(int N, int M) {
        buggPanel.setLayout(new GridLayout(N,M));

        buggField = new ButtonWithLocation[N][M] ;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Coordinate coordinate = new Coordinate(i,j) ;
                buggField[i][j] = new ButtonWithLocation(" "+i + "" + j+" ", coordinate) ;
                buggField[i][j].setForeground(new Color(255, 255, 255)); //barva pisma
                buggField[i][j].setOpaque(true); //znepruhledni
                buggField[i][j].setHorizontalAlignment(SwingConstants.CENTER); //vycentruje text
                buggField[i][j].setBorderPainted(false); //to zajisti, ze JButton bude vypadat jako JLabel
                buggField[i][j].setEnabled(false);
            }
        }

        //add buggField to buggPanel
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                buggPanel.add(buggField[i][j]) ;
            }
        }


    }

    /**
     * Zastavi workera, nastavi text do stateLabel,
     * nastavi klikatelne buggField.
     */
    private void pauseButtonClicked(ActionEvent event) {
        worker.cancel(true) ;
        simulationState = simulationState_t.PAUSED ;
        stateLabel.setText("state: paused");
        for (int i = 0; i < buggField.length; i++) {
            for (int j = 0; j < buggField[0].length; j++) {
                buggField[i][j].setEnabled(true);
            }
        }
        energyLabel.setVisible(true);
    }

    private void startButtonClicked(ActionEvent event) {
        worker = new Worker() ;
        worker.execute();
        simulationState = simulationState_t.RUNNING ;
        stateLabel.setText("state: running");
        for (int i = 0; i < buggField.length; i++) {
            for (int j = 0; j < buggField[0].length; j++) {
                buggField[i][j].setEnabled(false);
            }
        }
        energyLabel.setVisible(false);
    }

    /**
     * Vypise informace o danem broukovi.
     */
    private void buggFieldClicked(ActionEvent event) {
        ButtonWithLocation source = (ButtonWithLocation)event.getSource() ;
        Field field = environment.getField(source.getCoordinate()) ;
        if (field instanceof Brouk) {
            Brouk brouk = (Brouk)field ;
            energyLabel.setText("energy: " + brouk.getEnergy());
        }
        //todo klikli jsme na neco jineho, nez na brouka
        else {
            energyLabel.setText("energy: ");
        }
    }

    private void addListeners() {
        startButton.addActionListener(
                (ActionEvent event) -> {
                    startButtonClicked(event);
                }
        );

        pauseButton.addActionListener(
                (ActionEvent event) -> {
                    pauseButtonClicked(event);
                }
        );

        //todo
        for (int i = 0; i < buggField.length; i++) {
            for (int j = 0; j < buggField[0].length; j++) {
                buggField[i][j].addActionListener(
                        (ActionEvent event) -> {
                            buggFieldClicked(event);
                        }
                );
            }
        }
    }

    private void initComponents() {

        pauseButton = new JButton();
        buggPanel = new JPanel();
        buggPropertiesPanel = new JPanel();
        energyLabel = new JLabel();
        startButton = new JButton();
        stateLabel = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pauseButton.setText("Pause");

        GroupLayout buggPanelLayout = new GroupLayout(buggPanel);
        buggPanel.setLayout(buggPanelLayout);
        buggPanelLayout.setHorizontalGroup(
                buggPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 480, Short.MAX_VALUE)
        );
        buggPanelLayout.setVerticalGroup(
                buggPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 383, Short.MAX_VALUE)
        );

        energyLabel.setText("f");

        GroupLayout broukPropertiesLayout = new GroupLayout(buggPropertiesPanel);
        buggPropertiesPanel.setLayout(broukPropertiesLayout);
        broukPropertiesLayout.setHorizontalGroup(
                broukPropertiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(broukPropertiesLayout.createSequentialGroup()
                                .addComponent(energyLabel)
                                .addGap(0, 100, Short.MAX_VALUE))
        );
        broukPropertiesLayout.setVerticalGroup(
                broukPropertiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(broukPropertiesLayout.createSequentialGroup()
                                .addComponent(energyLabel)
                                .addGap(0, 231, Short.MAX_VALUE))
        );

        startButton.setText("Start");

        stateLabel.setText("state: initiated");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(startButton)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(pauseButton))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(buggPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(buggPropertiesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(324, 324, 324)
                                                .addComponent(stateLabel)))
                                .addContainerGap(122, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(stateLabel)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(pauseButton)
                                        .addComponent(startButton))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(buggPropertiesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buggPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(118, Short.MAX_VALUE))
        );

        pack();
    }

}
