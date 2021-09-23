package main.java.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Dragger {
     public static void dragWindow(JComponent component, Window frame) {
        final int[] pX = new int[1];
        final int[] pY = new int[1];
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                // Get x,y and store them
                pX[0] = me.getX();
                pY[0] = me.getY();
            }
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(frame.getLocation().x + me.getX() - pX[0],
                        frame.getLocation().y + me.getY() - pY[0]);
            }
        });
        component.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(frame.getLocation().x + me.getX() - pX[0],
                        frame.getLocation().y + me.getY() - pY[0]);
            }
        });
    }
}
