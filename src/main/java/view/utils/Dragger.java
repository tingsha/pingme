package main.java.view.utils;

import main.java.view.StatisticView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

public class Dragger {
     public static void dragWindow(JComponent component, Window frame) {
        final int[] pX = new int[1];
        final int[] pY = new int[1];
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                // Get x,y and store them
                pX[0] = me.getX();
                pY[0] = me.getY();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(frame.getLocation().x + me.getX() - pX[0],
                        frame.getLocation().y + me.getY() - pY[0]);
            }
        });
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                component.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(frame.getLocation().x + me.getX() - pX[0],
                        frame.getLocation().y + me.getY() - pY[0]);
                if (frame.getClass().getSimpleName().equals("StatisticView"))
                    PropertiesHelper.rewriteProperties(new HashMap<>(){{
                        put("locationX", String.valueOf(frame.getLocation().x));
                        put("locationY", String.valueOf(frame.getLocation().y));
                    }});
            }
        });
    }
}
