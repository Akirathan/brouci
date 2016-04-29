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
    private JLabel ageLabel ;
    private JLabel childrenAsMotherLabel ;
    private JLabel childrenAsFatherLabel ;
    private JLabel IDLabel ;
    //-------------------------------------------------


    private JButton pauseButton ;
    private JButton startButton ;
    private JLabel roundCountLabel ;
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

    public void draw() {
        roundCountLabel.setText("round: " + environment.getRoundCount());
        for (int i = 0; i < buggField.length; i++) {
            for (int j = 0; j < buggField[0].length; j++) {
                Coordinate coordinate = new Coordinate(i,j) ;
                Field field = environment.getField(coordinate) ;
                if (field instanceof Brouk) {
                    switch (((Brouk) field).getState()) {
                        case NORMAL:
                            buggField[i][j].setBackground(new Color(150,0,0));
                            break ;
                        case PREGNANT:
                            buggField[i][j].setBackground(new Color(150, 70, 101));
                            break ;
                    }
                }
                else if (field instanceof Block) {
                    if (field instanceof BuggGrave) {
                        buggField[i][j].setBackground(new Color(148, 92, 3));
                    }
                    else {
                        buggField[i][j].setBackground(new Color(0, 0, 0));
                    }
                }
                else if (field instanceof Free) {
                    buggField[i][j].setBackground(new Color(0, 100, 0));
                }
                else if (field instanceof Food) {
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

    private void resetBuggPropertiesPanel() {
        IDLabel.setText("ID: ");
        energyLabel.setText("energy: ");
        ageLabel.setText("age: ");
        childrenAsMotherLabel.setText("children(mother): ");
        childrenAsFatherLabel.setText("children(father): ");
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
        energyLabel.setText("energy: ");
        buggPropertiesPanel.setVisible(true);
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
        buggPropertiesPanel.setVisible(false);
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
            ageLabel.setText("age: " + brouk.getAge());
            childrenAsMotherLabel.setText("children(mother): " + brouk.getNumberOfChildrenAsMother());
            childrenAsFatherLabel.setText("children(father): " + brouk.getNumberOfChildrenAsFather());
            IDLabel.setText("ID: " + brouk.ID);
        }
        //todo klikli jsme na neco jineho, nez na brouka
        else {
            resetBuggPropertiesPanel();
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

        pauseButton = new javax.swing.JButton();
        buggPanel = new javax.swing.JPanel();
        buggPropertiesPanel = new javax.swing.JPanel();
        ageLabel = new javax.swing.JLabel();
        energyLabel = new javax.swing.JLabel();
        childrenAsMotherLabel = new javax.swing.JLabel();
        IDLabel = new javax.swing.JLabel();
        childrenAsFatherLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        stateLabel = new javax.swing.JLabel();
        roundCountLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pauseButton.setText("Pause");

        javax.swing.GroupLayout buggPanelLayout = new javax.swing.GroupLayout(buggPanel);
        buggPanel.setLayout(buggPanelLayout);
        buggPanelLayout.setHorizontalGroup(
                buggPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 480, Short.MAX_VALUE)
        );
        buggPanelLayout.setVerticalGroup(
                buggPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 383, Short.MAX_VALUE)
        );

        ageLabel.setText("age: ");

        energyLabel.setText("energy: ");

        childrenAsMotherLabel.setText("children(mother): ");

        IDLabel.setText("ID: ");

        childrenAsFatherLabel.setText("children(father): ");

        javax.swing.GroupLayout buggPropertiesPanelLayout = new javax.swing.GroupLayout(buggPropertiesPanel);
        buggPropertiesPanel.setLayout(buggPropertiesPanelLayout);
        buggPropertiesPanelLayout.setHorizontalGroup(
                buggPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buggPropertiesPanelLayout.createSequentialGroup()
                                .addGroup(buggPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ageLabel)
                                        .addComponent(energyLabel)
                                        .addComponent(childrenAsMotherLabel)
                                        .addComponent(IDLabel)
                                        .addComponent(childrenAsFatherLabel))
                                .addGap(0, 94, Short.MAX_VALUE))
        );
        buggPropertiesPanelLayout.setVerticalGroup(
                buggPropertiesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(buggPropertiesPanelLayout.createSequentialGroup()
                                .addComponent(IDLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ageLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(energyLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(childrenAsMotherLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(childrenAsFatherLabel)
                                .addContainerGap(147, Short.MAX_VALUE))
        );

        startButton.setText("Start");

        stateLabel.setText("state: initiated");

        roundCountLabel.setText("round: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addComponent(buggPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(buggPropertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(42, 42, 42)
                                                .addComponent(startButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(pauseButton))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(324, 324, 324)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(stateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(roundCountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(stateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(roundCountLabel)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(pauseButton)
                                        .addComponent(startButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buggPropertiesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buggPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(118, Short.MAX_VALUE))
        );

        pack();
    }

}
