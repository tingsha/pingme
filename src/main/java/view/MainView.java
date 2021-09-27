package main.java.view;

import main.java.controller.Controller;
import main.java.model.PingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MainView extends JFrame implements View {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 700;
    private Controller controller;
    private SystemTray tray;
    private final ServersView serversView = new ServersView();
    private final ToolbarView toolbarView = new ToolbarView(this);
    private TrayIcon trayIcon;

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

        add(serversView, BorderLayout.CENTER);
        add(toolbarView, BorderLayout.NORTH);
        Dragger.dragWindow(toolbarView.getAppTitle(), this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeiconified(WindowEvent e) {
                try{
                    //TODO fix bug
                    serversView.getSelectedServer().changeLinesColor(new Color(255, 192, 203));
                } catch (Exception ignored){
                }
            }
        });

        pack();
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        controller.setMainView(this);
        serversView.setController(controller);
        toolbarView.setController(controller);
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
                controller.onClickTrayExit();
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
