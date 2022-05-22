package main.java.view;

import main.java.controller.Controller;
import main.java.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Представление списка серверов для выбора
 */
public class ServersView extends JPanel {
    private final Controller controller;
    private final StartBtnView startBtn;
    private final Properties properties = PropertiesUtils.loadProperties(FileUtils.getFileFromResources("/config.properties"));
    private final List<ServerButton> serverButtons;
    private ServerButton selectedServer;

    public ServersView(Controller controller) {
        this.controller = controller;
        startBtn = new StartBtnView(controller);
        setBackground(Colors.SERVERS_BACKGROUND);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        serverButtons = createServerBtns();

        ButtonGroup serversGroup = new ButtonGroup();
        for (ServerButton serverButton : serverButtons)
            serversGroup.add(serverButton.getServerBtn());

        constraints.insets = new Insets(30, 30, 30, 30);

        constraints.gridx = 0;
        add(serverButtons.get(0), constraints);

        constraints.gridx = 1;
        add(serverButtons.get(1), constraints);

        constraints.gridx = 2;
        add(serverButtons.get(2), constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(serverButtons.get(3), constraints);

        constraints.gridx = 2;
        add(serverButtons.get(4), constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        add(startBtn, constraints);
    }

    /**
     * Создать кнопки для выбора серверов
     *
     * @return список кнопок
     */
    private List<ServerButton> createServerBtns() {
        List<ServerButton> btns = new ArrayList<>();
        btns.add(new ServerButton(this, FileUtils.getFileFromResources("/img/servers/google.png"), "google.com",
                LineBuilder.getGoogleServerLines()));
        btns.add(new ServerButton(this, FileUtils.getFileFromResources("/img/servers/yandex.png"), "ya.ru",
                LineBuilder.getYandexServerLines()));
        btns.add(new ServerButton(this, FileUtils.getFileFromResources("/img/servers/steam.png"), "store.steampowered.com",
                LineBuilder.getSteamServerLines()));
        btns.add(new ServerButton(this, FileUtils.getFileFromResources("/img/servers/battlenet.png"), "blizzard.com",
                LineBuilder.getBattlenetLines()));
        ServerButton userServerBtn = new ServerButton(this, FileUtils.getFileFromResources("/img/servers/add.png"), "Add",
                LineBuilder.getCustomLines());
        userServerBtn.setServerAddress(properties.getProperty("userServerAddress"));
        userServerBtn.getServerBtn().addActionListener(e -> new ServerDialogView(userServerBtn));
        btns.add(userServerBtn);
        return btns;
    }

    /**
     * При сворачивании и разворачивании окна линии исчезают, поэтому перевыбираем сервер, чтобы перерисовать линии
     */
    public void selectServer() {
        Timer timer = new Timer(100, e -> {
            for (ServerButton serverButton : serverButtons) {
                serverButton.changeLinesColor(Colors.DESELECTED_LINE);
            }
            if (selectedServer != null) {
                selectedServer.changeLinesColor(Colors.SELECTED_LINE);
                selectedServer.getServerBtn().setSelected(true);
            } else if (startBtn.isSelected()) {
                String selectedServerAddress = properties.get("serverAddress").toString();
                serverButtons
                        .stream()
                        .filter(btn -> btn.getServerAddress().equals(selectedServerAddress))
                        .findFirst()
                        .ifPresent(serverButton -> {
                            serverButton.getServerBtn().setSelected(true);
                            serverButton.getServerBtn().setSelected(true);
                        });
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Кнопка для выбора сервера
     */
    public StartBtnView getStartBtn() {
        return startBtn;
    }

    public class ServerButton extends JPanel {

        private String serverAddress;
        private JRadioButton serverBtn;
        private final List<Line> linkedLines;
        private final JComponent parentView;
        JTextField field = new JTextField();

        public ServerButton(JComponent parentView, File buttonIconFile, String serverAddress, List<Line> linkedLines) {
            this.parentView = parentView;
            this.serverAddress = serverAddress;
            this.linkedLines = linkedLines;

            setBackground(Colors.SERVERS_BACKGROUND);
            setPreferredSize(new Dimension(250, 220));
            setLayout(new BorderLayout());
            setBorder(new TextBubbleBorder(Colors.DESELECTED_LINE, 2, 16, 0,
                    new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));

            initializeButton(buttonIconFile, serverAddress);
            initializeTitle(serverAddress);
            add(serverBtn, BorderLayout.CENTER);
            add(field, BorderLayout.SOUTH);
        }

        private void initializeButton(File buttonIconFile, String serverAddress) {
            serverBtn = new JRadioButton();
            serverBtn.setBorderPainted(false);
            serverBtn.setBackground(Colors.SERVERS_BACKGROUND);
            serverBtn.setIcon(ImageUtils.getImageIcon(buttonIconFile));
            serverBtn.setHorizontalAlignment(SwingConstants.CENTER);
            serverBtn.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    selectedServer = this;
                    setBorder(new TextBubbleBorder(Colors.SELECTED_LINE, 2, 16, 0,
                            new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));
                    changeLinesColor(Colors.SELECTED_LINE);
                    if (!startBtn.isSelected())
                        startBtn.setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/charger/charger_pink.png")));
                    controller.onClickServerBtn(serverAddress);
                    PropertiesUtils.rewriteProperties(FileUtils.getFileFromResources("/config.properties"), Map.of("lastServer", serverAddress));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    setBorder(new TextBubbleBorder(Colors.DESELECTED_LINE, 2, 16, 0,
                            new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));
                    changeLinesColor(Colors.DESELECTED_LINE);
                    if (!startBtn.isSelected())
                        startBtn.setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/charger/charger_grey.png")));
                }
            });
        }

        private void initializeTitle(String serverAddress) {
            field.setText(serverAddress);
            field.setBackground(Colors.SERVERS_BACKGROUND);
            field.setEditable(false);
            field.setForeground(new Color(187, 187, 187));
            field.setFont(new Font("Dialog", Font.PLAIN, 16));
            field.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            field.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        }

        public JRadioButton getServerBtn() {
            return serverBtn;
        }

        public String getServerAddress() {
            return serverAddress;
        }

        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
            field.setText(serverAddress);
            PropertiesUtils.rewriteProperties(FileUtils.getFileFromResources("/config.properties"), Map.of("serverAddress", serverAddress));
        }

        public void changeLinesColor(Color color) {
            Graphics2D g2d = (Graphics2D) parentView.getGraphics();
            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(color);
            for (Line line : linkedLines) {
                line.paint(g2d);
            }
        }

        @Override
        public String toString() {
            return getServerAddress();
        }
    }
}
