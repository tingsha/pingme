package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ServersView extends JPanel implements View{
    private Controller controller;
    private final Color bgColor = new Color(43, 43, 43);
    private final Color lineColor = new Color(77, 77, 77);

    public ServersView(){
        setBackground(bgColor);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(0, 30, 0, 30);
        constraints.gridx = 0;
        add(new ServerButton(Path.of("src/main/resources/img/servers/google.png"), "google.com"), constraints);
        constraints.gridx = 1;
        add(new ServerButton(Path.of("src/main/resources/img/servers/yandex.png"), "yandex.ru"), constraints);
        constraints.gridx = 2;
        add(new ServerButton(Path.of("src/main/resources/img/servers/csgo.png"), "CS:GO"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(createLines(), constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new ServerButton(Path.of("src/main/resources/img/servers/dota2.png"), "Dota 2"), constraints);
        constraints.gridx = 2;
        add(new ServerButton(Path.of("src/main/resources/img/servers/add.png"), "Add"), constraints);
    }

    private JPanel createLines(){
        JPanel panel = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(lineColor);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawLine(10, 0, 10, 30);
                g2d.drawLine(10, 30, 200, 30);
                g2d.drawLine(200, 30, 200, 60);
            }
        };
        panel.setPreferredSize(new Dimension(210, 60));
        panel.setBackground(bgColor);
        return panel;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public class ServerButton extends JRadioButton{
        private static final Logger logger = LoggerFactory.getLogger(ServerButton.class);
        private final Path pathToIcon;
        private final String domain;

        public ServerButton(Path pathToIcon, String domain) {
            this.pathToIcon = pathToIcon;
            this.domain = domain;
            setBorder(new TextBubbleBorder(new Color(77, 77, 77), 2, 16, 0));
            setBorderPainted(true);
            setIcon(getIcon());
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(bgColor);
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(250, 220));
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
    }
}
