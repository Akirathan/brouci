package brouci;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by bruchpa on 17.4.16.
 */
public class Zkouska implements Runnable {
    JButton startButton, stopButton, processButton ;
    Worker worker ;

    class Worker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            while (true) {
                processButton.setBackground(new Color(0, 0, 0));
                Thread.sleep(1);
                processButton.setBackground(new Color(255, 0, 0));
                Thread.sleep(1);
            }
        }
    }


    public static void main(String[] args) {
        /*Zkouska program = new Zkouska() ;
        SwingUtilities.invokeLater(program);*/
        int b = 3 ;
        assert (b == 1) : "b neni 1, ale je " + b ;
        if (b == 1) System.out.println("b == 1");
    }

    public Zkouska() {
        worker = new Worker() ;
    }

    private void createGraphics() {
        JFrame frame = new JFrame() ;
        frame.setLayout(new GridLayout(1,0));

        startButton = new JButton("START") ;
        startButton.addActionListener(
                (ActionEvent event) -> {
                    worker = new Worker() ;
                    worker.execute();
                }
        );
        processButton = new JButton("PROCESS") ;
        stopButton = new JButton("STOP");
        stopButton.addActionListener(
                (ActionEvent event) -> {
                    processButton.setBackground(new Color(255,255,255));
                    worker.cancel(true) ;
                }
        );


        frame.getContentPane().add(startButton) ;
        frame.getContentPane().add(stopButton) ;
        frame.getContentPane().add(processButton) ;

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack() ;
        frame.setVisible(true);
    }


    @Override
    public void run() {
        createGraphics();
    }
}



