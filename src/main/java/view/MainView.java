package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class MainView extends JFrame implements View {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private Controller controller;
    private SystemTray tray;

    public MainView() throws HeadlessException {
        setUndecorated(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(77, 77, 77), 1));

        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);
        cr.setSnapSize(new Dimension(10, 10));
        cr.setMaximumSize(new Dimension(1600, 800));
        cr.setMinimumSize(new Dimension(800, 500));

        try {
            createNewTray();
        } catch (IOException e) {
            logger.error("Can't create tray! " + e);
            System.exit(-1);
        }

        add(new ServersView(), BorderLayout.CENTER);
        add(new ToolbarView(this), BorderLayout.NORTH);

        pack();
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void createNewTray() throws IOException {
        tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        TrayIcon trayIcon = new TrayIcon(ImageIO.read(new File("src/main/resources/img/tray/tray.png")), "pingme!", popup);
        trayIcon.setImageAutoSize(true);
        MenuItem Items = new MenuItem("Open");
        Items.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                setExtendedState(JFrame.NORMAL);
                tray.remove(trayIcon);
            }
        });
        popup.add(Items);
        Items = new MenuItem("Exit");
        ActionListener exitListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        Items.addActionListener(exitListener);
        popup.add(Items);
    }
}
