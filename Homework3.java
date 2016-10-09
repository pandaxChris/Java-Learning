/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework.pkg3;

import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

/**
 * Chris Li
 */
public class Homework3 {
    /**
     * @param args the command line arguments
     */
    static JPanel bigPanel;
    static JFrame theFrame;
    public static void main(String[] args) {
        theFrame = new JFrame("Button With Colors");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(3, 4));    //Change to fit needs background colors if no colors change
        bigPanel.setSize(750, 750);
        for (int i = 0; i < 12; i++) {    //Change 12 to whatever number of buttons needed.
            bigPanel.add(new JButton());
        }
        Component[] components = bigPanel.getComponents();
        for (Component b : components) {
            if (b instanceof JButton) {
                JButton button = (JButton) b;    //Cast into button to use button functions
                Random rand = new Random();
                button.setBackground(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
                button.setOpaque(true);
                button.setBorderPainted(false);
                ButtonPress bp = new ButtonPress(button);
                bp.start();
                button.addActionListener(bp);
            }
        }
        theFrame.setSize(750, 750);
        theFrame.add(bigPanel);
        theFrame.setVisible(true);
    }
    static class ButtonPress extends Thread implements ActionListener {
        boolean canChange;
        JButton but;
        public ButtonPress(JButton b) {
            canChange = true;
            but = b;
        }
        @Override
        public void actionPerformed(ActionEvent ae) {
            canChange = !canChange;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    sleep(1000);
                    if (canChange) {
                        Random rand = new Random();
                        Color c = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
                        but.setBackground(c);
                        System.out.println(c);
                    }
                }
            } catch (Exception e) {
                System.out.println("Color Change");
            }
        }
    }
}
