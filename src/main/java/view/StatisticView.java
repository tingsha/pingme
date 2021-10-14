package main.java.view;

import main.java.view.utils.PropertiesHelper;
import main.java.controller.Controller;
import main.java.view.utils.Dragger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class StatisticView extends JWindow{
    private final JTextPane pingString = new JTextPane();
    private static StatisticView instance;
    private final Properties properties = PropertiesHelper.loadProperties();

    public static StatisticView getInstance() {
        if (instance == null)
            instance = new StatisticView();
        return instance;
    }

    private StatisticView(){
        Dragger.dragWindow(pingString, this);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        int red = Integer.parseInt(properties.getProperty("red"));
        int green = Integer.parseInt(properties.getProperty("green"));
        int blue = Integer.parseInt(properties.getProperty("blue"));
        int alpha = Integer.parseInt(properties.getProperty("alpha"));
        int fontSize = Integer.parseInt(properties.getProperty("size"));
        int locationX = Integer.parseInt(properties.getProperty("locationX"));
        int locationY = Integer.parseInt(properties.getProperty("locationY"));
        pingString.setText("connecting...");
        pingString.setForeground(new Color(red, green, blue, alpha));
        pingString.setFont(new Font("Consolas", Font.PLAIN, fontSize));
        pingString.setOpaque(false);
        pingString.setEditable(false);
        pingString.setBackground(new Color(0, 0, 0, 0));
        pingString.setBorder(null);
        pingString.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 200));
        add(pingString, BorderLayout.WEST);
        setLocation(locationX, locationY);
        setVisible(true);
        pack();
    }

    public void refresh(String ping, String upload, String download){
        if (ping == null || ping.equals(""))
            return;
        boolean isUploadSelected = Boolean.parseBoolean(properties.getProperty("upload"));
        boolean isDownloadSelected = Boolean.parseBoolean(properties.getProperty("download"));
        boolean isUnitsSelected = Boolean.parseBoolean(properties.getProperty("units"));
        boolean isLabelsSelected = Boolean.parseBoolean(properties.getProperty("labels"));

        StringBuilder text = new StringBuilder();
        if (ping.equals("loss"))
            text.append("loss\n");
        else {
            if (isLabelsSelected)
                text.append("ping: ");
            text.append(ping);
            if (isUnitsSelected)
                text.append("ms");
            text.append("\n");
        }

        if (isUploadSelected) {
            if (isLabelsSelected)
                text.append("upload: ");
            text.append(upload);
            if (isUnitsSelected)
                text.append("Mb/s");
            text.append("\n");
        }

        if (isDownloadSelected) {
            if (isLabelsSelected)
                text.append("download: ");
            text.append(download);
            if (isUnitsSelected)
                text.append("Mb/s");
            text.append("\n");
        }

        pingString.setText(text.toString());
        try {
            Thread.sleep(1000);
            pingString.setText("");
        } catch (InterruptedException ignored) {
        }
    }
}
