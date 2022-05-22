package main.java.view;

import main.java.controller.Controller;
import main.java.utils.ComponentResizer;
import main.java.utils.FileUtils;
import main.java.utils.ImageUtils;
import main.java.utils.WindowUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Основное окно приложения
 */
public class MainView extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 750;
    private final Controller controller;
    private SystemTray tray;
    private final ServersView serversView;
    private final ToolbarView toolbarView;
    private TrayIcon trayIcon;
    private boolean isHidden;

    public MainView(Controller controller) throws HeadlessException {
        this.controller = controller;
        controller.setMainView(this);
        this.serversView = new ServersView(controller);
        this.toolbarView = new ToolbarView(this, controller);

        setUndecorated(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setIconImage(ImageUtils.getImage(FileUtils.getFileFromResources("/img/taskbar.png")));

        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(77, 77, 77), 1));

        ComponentResizer cr = new ComponentResizer();
        cr.registerComponent(this);
        cr.setSnapSize(new Dimension(10, 10));
        cr.setMinimumSize(new Dimension(WIDTH, HEIGHT));

        createTray();

        add(serversView, BorderLayout.CENTER);
        add(toolbarView, BorderLayout.NORTH);
        WindowUtil.dragWindow(toolbarView.getAppTitle(), this);

        hideToTray();
        pack();
        setBounds(0, 0, WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                serversView.selectServer();
            }
        });

        serversView.getStartBtn().doClick();
    }

    /**
     * Создать трей с кнопками "Open" и "Exit"
     */
    public void createTray() {
        tray = SystemTray.getSystemTray();
        PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(ImageUtils.getImage(FileUtils.getFileFromResources("/img/tray/tray.png")), "pingme!", popup);
        trayIcon.setImageAutoSize(true);
        MenuItem items = new MenuItem("Open");
        items.addActionListener(e -> {
            setVisible(true);
            setExtendedState(JFrame.NORMAL);
            tray.remove(trayIcon);
            serversView.selectServer();
        });
        popup.add(items);
        items = new MenuItem("Exit");
        ActionListener exitListener = e -> {
            controller.onClickExit();
            System.exit(0);
        };
        items.addActionListener(exitListener);
        popup.add(items);
    }

    /**
     * Свернуть приложение в трей
     */
    public void hideToTray() {
        if (isHidden) {
            return;
        }
        try {
            tray.add(trayIcon);
            isHidden = true;
        } catch (AWTException e) {
            logger.error("Exception during hiding tray. " + e.getMessage());
            throw new RuntimeException(e);
        }
        setVisible(false);
    }

    /**
     * Удалить иконку приложения из трея
     */
    public void removeFromTray() {
        tray.remove(trayIcon);
        isHidden = false;
    }
}
