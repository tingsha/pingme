package main.java.view;

import main.java.controller.Controller;
import main.java.view.utils.Colors;
import main.java.view.utils.TextBubbleBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ServersView extends JPanel implements View{
    private static final Logger logger = LoggerFactory.getLogger(ServersView.class);
    private Controller controller;
    private final PingBtnView pingBtn;
    private final List<ServerButton> serverButtons;
    private ServerButton selectedServer;

    public ServersView(){
        pingBtn = new PingBtnView();
        setBackground(Colors.SERVERS_BACKGROUND);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel horizontalLinePanel = getHorizontalLinePanel();
        JPanel verticalLinePanel = getVerticalLinePanel();

        serverButtons = createServerBtns(horizontalLinePanel, verticalLinePanel);

        ButtonGroup serversGroup = new ButtonGroup();
        for (ServerButton serverButton : serverButtons)
            serversGroup.add(serverButton.getServerBtn());

        constraints.insets = new Insets(0, 30, 0, 30);

        constraints.gridx = 0;
        add(serverButtons.get(0), constraints);

        constraints.gridx = 1;
        add(serverButtons.get(1), constraints);

        constraints.gridx = 2;
        add(serverButtons.get(2), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        add(horizontalLinePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(serverButtons.get(3), constraints);

        constraints.gridx = 1;
        add(verticalLinePanel, constraints);

        constraints.gridx = 2;
        add(serverButtons.get(4), constraints);

        constraints.gridx = 1;
        constraints.gridy= 3;
        add(pingBtn, constraints);

    }

    private List<ServerButton> createServerBtns(JPanel horizontalLinePanel, JPanel verticalLinePanel){
        List<ServerButton> btns = new ArrayList<>();
        btns.add(new ServerButton(Path.of("src/main/resources/img/servers/google.png"), "google.com",
                Arrays.asList(
                        new Line(new Point(10, 0), new Point(10, 30), horizontalLinePanel),
                        new Line(new Point(10, 30), new Point(180, 30), horizontalLinePanel),
                        new Line(new Point(180, 30), new Point(405, 30), horizontalLinePanel),
                        new Line(new Point(405, 30), new Point(405, 60), horizontalLinePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                )));
        btns.add(new ServerButton(Path.of("src/main/resources/img/servers/yandex.png"), "ya.ru",
                Arrays.asList(
                        new Line(new Point(405, 0), new Point(405, 30), horizontalLinePanel),
                        new Line(new Point(405, 30), new Point(405, 60), horizontalLinePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                )));
        btns.add(new ServerButton(Path.of("src/main/resources/img/servers/csgo.png"), "CS:GO",
                Arrays.asList(
                        new Line(new Point(405, 30), new Point(630, 30), horizontalLinePanel),
                        new Line(new Point(630, 30), new Point(800, 30), horizontalLinePanel),
                        new Line(new Point(800, 0), new Point(800, 30), horizontalLinePanel),
                        new Line(new Point(405, 30), new Point(405, 60), horizontalLinePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                )));
        btns.add(new ServerButton(Path.of("src/main/resources/img/servers/dota2.png"), "Dota 2",
                Arrays.asList(
                        new Line(new Point(180, 30), new Point(180, 60), horizontalLinePanel),
                        new Line(new Point(180, 30), new Point(405, 30), horizontalLinePanel),
                        new Line(new Point(405, 30), new Point(405, 60), horizontalLinePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                )));
        ServerButton btn = new ServerButton(Path.of("src/main/resources/img/servers/add.png"), "Add",
                Arrays.asList(
                        new Line(new Point(405, 30), new Point(630, 30), horizontalLinePanel),
                        new Line(new Point(630, 30), new Point(630, 60), horizontalLinePanel),
                        new Line(new Point(405, 30), new Point(405, 60), horizontalLinePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));
        Properties properties = loadProperties();
        btn.setDomain(properties.getProperty("domain"));
        btn.getServerBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DomainDialog(btn);
            }
        });
        btns.add(btn);
        return btns;
    }

    private JPanel getVerticalLinePanel(){
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Colors.DESELECTED_LINE);
                g2d.setStroke(new BasicStroke(2f));

                g2d.drawLine(125, 0, 125, 220);
                g2d.drawLine(125, 219, 88, 219);
                g2d.drawLine(125, 219, 161, 219);
            }
        };
        panel.setPreferredSize(new Dimension(250, 220));
        panel.setBackground(Colors.SERVERS_BACKGROUND);
        return panel;
    }

    private JPanel getHorizontalLinePanel(){
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Colors.DESELECTED_LINE);
                g2d.setStroke(new BasicStroke(2f));

                g2d.drawLine(10, 0, 10, 30);
                g2d.drawLine(800, 0, 800, 30);

                g2d.drawLine(10, 30, 180, 30);
                g2d.drawLine(180, 30, 405, 30);
                g2d.drawLine(405, 30, 630, 30);
                g2d.drawLine(630, 30, 800, 30);

                g2d.drawLine(180, 30, 180, 60);
                g2d.drawLine(630, 30, 630, 60);

                g2d.drawLine(405, 0, 405, 30);
                g2d.drawLine(405, 30, 405, 60);
            }
        };
        panel.setPreferredSize(new Dimension(810, 60));
        panel.setBackground(Colors.SERVERS_BACKGROUND);
        return panel;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        pingBtn.setController(controller);
    }

    public List<ServerButton> getServerButtons() {
        return serverButtons;
    }

    public ServerButton getSelectedServer() {
        return selectedServer;
    }

    public class ServerButton extends JPanel{
        private static final Logger logger = LoggerFactory.getLogger(ServerButton.class);
        private final Path pathToIcon;
        private String domain;
        private final List<Line> linkedLines;
        private final JRadioButton serverBtn;
        JTextField field = new JTextField();

        public ServerButton(Path pathToIcon, String domain, List<Line> linkedLines) {
            this.pathToIcon = pathToIcon;
            this.domain = domain;
            this.linkedLines = linkedLines;

            setBackground(Colors.SERVERS_BACKGROUND);
            setPreferredSize(new Dimension(250, 220));
            setLayout(new BorderLayout());
            setBorder(new TextBubbleBorder(Colors.DESELECTED_LINE, 2, 16, 0,
                    new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));

            serverBtn = new JRadioButton();
            serverBtn.setBorderPainted(false);
            serverBtn.setBackground(Colors.SERVERS_BACKGROUND);
            serverBtn.setIcon(getIcon());
            serverBtn.setHorizontalAlignment(SwingConstants.CENTER);
            serverBtn.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        selectedServer = ServerButton.this;
                        setBorder(new TextBubbleBorder(Colors.SELECTED_LINE, 2, 16, 0,
                                new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));
                        changeLinesColor(Colors.SELECTED_LINE);
                        if (!pingBtn.isSelected())
                            pingBtn.setImageIcon(Path.of("src/main/resources/img/charger/charger_pink.png"));
                        controller.onClickServerBtn(ServersView.ServerButton.this);
                    } else if (e.getStateChange() == ItemEvent.DESELECTED){
                        setBorder(new TextBubbleBorder(Colors.DESELECTED_LINE, 2, 16, 0,
                                new Insets(4, 4, 4, 4), Colors.SERVERS_BACKGROUND));
                        changeLinesColor(Colors.DESELECTED_LINE);
                        if (!pingBtn.isSelected())
                            pingBtn.setImageIcon(Path.of("src/main/resources/img/charger/charger_grey.png"));
                    }
                }
            });
            field.setText(domain);
            field.setBackground(Colors.SERVERS_BACKGROUND);
            field.setEditable(false);
            field.setForeground(new Color(187, 187, 187));
            field.setFont(new Font("Dialog", Font.PLAIN, 16));
            field.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            field.setHorizontalAlignment((int) CENTER_ALIGNMENT);
            add(serverBtn, BorderLayout.CENTER);
            add(field, BorderLayout.SOUTH);
        }

        public ImageIcon getIcon() {
            try {
                return new ImageIcon(ImageIO.read(pathToIcon.toFile()));
            } catch (IOException e) {
                logger.error("Can't load server icon! " + e.getMessage());
                return null;
            }
        }

        public JRadioButton getServerBtn() {
            return serverBtn;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            field.setText(domain);
            Properties properties = loadProperties();
            try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/config.properties")) {
                Properties propertiesToSave = new Properties();
                propertiesToSave.setProperty("red", properties.getProperty("red"));
                propertiesToSave.setProperty("green", properties.getProperty("green"));
                propertiesToSave.setProperty("blue", properties.getProperty("blue"));
                propertiesToSave.setProperty("alpha", properties.getProperty("alpha"));
                propertiesToSave.setProperty("download", properties.getProperty("download"));
                propertiesToSave.setProperty("upload", properties.getProperty("upload"));
                propertiesToSave.setProperty("size", properties.getProperty("size"));
                propertiesToSave.setProperty("units", properties.getProperty("units"));
                propertiesToSave.setProperty("labels", properties.getProperty("labels"));
                propertiesToSave.setProperty("domain", domain);
                propertiesToSave.store(outputStream, null);
            } catch (IOException e) {
                logger.warn("Can't rewrite domain in property " + e.getMessage());
            }
            this.domain = domain;
        }

        public void changeLinesColor(Color color){
            for (Line line : linkedLines){
                Graphics2D g2d = (Graphics2D) line.linkedPanel.getGraphics();
                g2d.setStroke(new BasicStroke(2f));
                g2d.setColor(color);
                g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
        }

        @Override
        public String toString(){
            return getDomain();
        }
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Can't load properties " + e.getMessage());
        }
        return properties;
    }

    public static class Line{
        private final Point start;
        private final Point end;
        private final JPanel linkedPanel;

        public Line(Point start, Point end, JPanel linkedPanel) {
            this.start = start;
            this.end = end;
            this.linkedPanel = linkedPanel;
        }
    }
}
