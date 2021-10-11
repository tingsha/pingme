package main.java.view;

import main.java.controller.Controller;
import main.java.view.settings.ToggleBtn;
import main.java.view.utils.Dragger;
import main.java.view.utils.ImageWriteEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StatisticView extends JWindow implements View{
    private final static Logger logger = LoggerFactory.getLogger(StatisticView.class);
    private Controller controller;
    private final JTextPane pingString = new JTextPane();
    private static StatisticView instance;

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
        Properties properties = loadProperties();
        int red = Integer.parseInt(properties.getProperty("red"));
        int green = Integer.parseInt(properties.getProperty("green"));
        int blue = Integer.parseInt(properties.getProperty("blue"));
        int alpha = Integer.parseInt(properties.getProperty("alpha"));
        int fontSize = Integer.parseInt(properties.getProperty("size"));
        pingString.setText("connecting...");
        pingString.setForeground(new Color(red, green, blue, alpha));
        pingString.setFont(new Font("Consolas", Font.PLAIN, fontSize));
        pingString.setOpaque(false);
        pingString.setEditable(false);
        pingString.setBackground(new Color(0, 0, 0, 0));
        pingString.setBorder(null);
        pingString.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 200));
        add(pingString, BorderLayout.WEST);
        setVisible(true);
        pack();
    }

    public void refresh(String ping, String upload, String download){
        if (ping == null || ping.equals(""))
            return;
        Properties properties = loadProperties();
        boolean isUploadSelected = Boolean.parseBoolean(properties.getProperty("upload"));
        boolean isDownloadSelected = Boolean.parseBoolean(properties.getProperty("download"));
        boolean isUnitsSelected = Boolean.parseBoolean(properties.getProperty("units"));
        boolean isLabelsSelected = Boolean.parseBoolean(properties.getProperty("labels"));

        StringBuilder text = new StringBuilder();
        if (isLabelsSelected)
            text.append("ping: ");
        text.append(ping);
        if (isUnitsSelected)
            text.append("ms");
        text.append("\n");

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

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Can't load settings " + e.getMessage());
        }
        return properties;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
