package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ServersView extends JPanel implements View{
    private Controller controller;
    private final Color bgColor = new Color(43, 43, 43);
    private final Color lineColor = new Color(77, 77, 77);
    private final Color selectedLineColor = new Color(255, 192, 203);
    private final PingBtnView pingBtn;

    public ServersView(MainView mainView){
        pingBtn = new PingBtnView(mainView);
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JPanel linePanel = getHorizontalLinePanel();
        JPanel verticalLinePanel = getVerticalLinePanel();

        JRadioButton googleServerBtn = new ServerButton(Path.of("src/main/resources/img/servers/google.png"), "google.com",
                Arrays.asList(
                        new Line(new Point(10, 0), new Point(10, 30), linePanel),
                        new Line(new Point(10, 30), new Point(180, 30), linePanel),
                        new Line(new Point(180, 30), new Point(405, 30), linePanel),
                        new Line(new Point(405, 30), new Point(405, 60), linePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));
        JRadioButton yandexServerBtn = new ServerButton(Path.of("src/main/resources/img/servers/yandex.png"), "ya.ru",
                Arrays.asList(
                        new Line(new Point(405, 0), new Point(405, 30), linePanel),
                        new Line(new Point(405, 30), new Point(405, 60), linePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));
        JRadioButton csgoServerBtn = new ServerButton(Path.of("src/main/resources/img/servers/csgo.png"), "CS:GO",
                Arrays.asList(
                        new Line(new Point(405, 30), new Point(630, 30), linePanel),
                        new Line(new Point(630, 30), new Point(800, 30), linePanel),
                        new Line(new Point(800, 0), new Point(800, 30), linePanel),
                        new Line(new Point(405, 30), new Point(405, 60), linePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));
        JRadioButton dotaServerBtn = new ServerButton(Path.of("src/main/resources/img/servers/dota2.png"), "Dota 2",
                Arrays.asList(
                        new Line(new Point(180, 30), new Point(180, 60), linePanel),
                        new Line(new Point(180, 30), new Point(405, 30), linePanel),
                        new Line(new Point(405, 30), new Point(405, 60), linePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));
        JRadioButton userServerBtn = new ServerButton(Path.of("src/main/resources/img/servers/add.png"), "Add",
                Arrays.asList(
                        new Line(new Point(405, 30), new Point(630, 30), linePanel),
                        new Line(new Point(630, 30), new Point(630, 60), linePanel),
                        new Line(new Point(405, 30), new Point(405, 60), linePanel),
                        new Line(new Point(125, 0), new Point(125, 220), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(88, 219), verticalLinePanel),
                        new Line(new Point(125, 219), new Point(161, 219), verticalLinePanel)
                ));

        ButtonGroup servers = new ButtonGroup();
        servers.add(googleServerBtn);
        servers.add(yandexServerBtn);
        servers.add(csgoServerBtn);
        servers.add(dotaServerBtn);
        servers.add(userServerBtn);

        constraints.insets = new Insets(0, 30, 0, 30);
        constraints.gridx = 0;
        add(googleServerBtn, constraints);

        constraints.gridx = 1;
        add(yandexServerBtn, constraints);

        constraints.gridx = 2;
        add(csgoServerBtn, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        add(linePanel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        add(dotaServerBtn, constraints);

        constraints.gridx = 1;
        add(verticalLinePanel, constraints);

        constraints.gridx = 2;
        add(userServerBtn, constraints);

        constraints.gridx = 1;
        constraints.gridy= 3;
        add(pingBtn, constraints);
    }

    private JPanel getVerticalLinePanel(){
        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(lineColor);
                g2d.setStroke(new BasicStroke(2f));

                g2d.drawLine(125, 0, 125, 220);
                g2d.drawLine(125, 219, 88, 219);
                g2d.drawLine(125, 219, 161, 219);
            }
        };
        panel.setPreferredSize(new Dimension(250, 220));
        panel.setBackground(bgColor);
        return panel;
    }

    private JPanel getHorizontalLinePanel(){
        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(lineColor);
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
        panel.setBackground(bgColor);
        return panel;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
        pingBtn.setController(controller);
    }

    public class ServerButton extends JRadioButton{
        private static final Logger logger = LoggerFactory.getLogger(ServerButton.class);
        private final Path pathToIcon;
        private final String domain;
        private final List<Line> linkedLines;

        public ServerButton(Path pathToIcon, String domain, List<Line> linkedLines) {
            this.pathToIcon = pathToIcon;
            this.domain = domain;
            this.linkedLines = linkedLines;
            setBorder(new TextBubbleBorder(lineColor, 2, 16, 0));
            setBorderPainted(true);
            setIcon(getIcon());
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(bgColor);
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(250, 220));
            addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        setBorder(new TextBubbleBorder(selectedLineColor, 2, 16, 0));
                        changeLinesColor(selectedLineColor);
                        pingBtn.setImageIcon(Path.of("src/main/resources/img/charger/charger_pink.png"));
                        controller.onClickServerBtn(ServersView.ServerButton.this);
                    } else if (e.getStateChange() == ItemEvent.DESELECTED){
                        setBorder(new TextBubbleBorder(lineColor, 2, 16, 0));
                        changeLinesColor(lineColor);
                        pingBtn.setImageIcon(Path.of("src/main/resources/img/charger/charger_grey.png"));
                    }
                }
            });
        }

        public ImageIcon getIcon() {
            try {
                return new ImageIcon(ImageIO.read(pathToIcon.toFile()));
            } catch (IOException e) {
                logger.error("Can't load server icon! " + e.getMessage());
                return null;
            }
        }

        public String getDomain() {
            return domain;
        }

        public void changeLinesColor(Color color){
            for (Line line : linkedLines){
                Graphics2D g2d = (Graphics2D) line.linkedPanel.getGraphics();
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(color);
                g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
            repaint();
        }
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
