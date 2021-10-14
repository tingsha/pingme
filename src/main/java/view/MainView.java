package main.java.view;

import main.java.controller.Controller;
import main.java.model.PingTask;
import main.java.view.utils.ComponentResizer;
import main.java.view.utils.Dragger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainView extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private Controller controller;
    private SystemTray tray;
    private final ServersView serversView;
    private final ToolbarView toolbarView;
    private TrayIcon trayIcon;

    public MainView(Controller controller) throws HeadlessException {
        this.controller = controller;
        controller.setMainView(this);
        this.serversView = new ServersView(controller);
        this.toolbarView = new ToolbarView(this, controller);

        setUndecorated(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        try{
            setIconImage(ImageIO.read(new File("src/main/resources/img/taskbar.png")));
        } catch (IOException e) {
            logger.warn("Can't load taskbar icon " + e.getMessage());
        }

        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(77, 77, 77), 1));

        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);
        cr.setSnapSize(new Dimension(10, 10));
        cr.setMaximumSize(new Dimension(1600, 800));
        cr.setMinimumSize(new Dimension(1000, 700));

        try {
            createNewTray();
        } catch (IOException e) {
            logger.error("Can't create tray! " + e);
            System.exit(-1);
        }

        add(serversView, BorderLayout.CENTER);
        add(toolbarView, BorderLayout.NORTH);
        Dragger.dragWindow(toolbarView.getAppTitle(), this);

        setVisible(false);
        pack();
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        serversView.getPingBtn().doClick();
    }

    public void createNewTray() throws IOException {
        tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(ImageIO.read(new File("src/main/resources/img/tray/tray.png")), "pingme!", popup);
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
                controller.onClickExit();
                System.exit(0);
            }
        };
        Items.addActionListener(exitListener);
        popup.add(Items);
    }

    public void hideToTray(){
        setVisible(false);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("Can't create tray! " + e.getMessage());
        }
    }

    public void removeFromTray(){
        tray.remove(trayIcon);
        PingTask.pingProcess.destroy();
    }
}
