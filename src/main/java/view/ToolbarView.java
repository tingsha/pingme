package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ToolbarView extends JPanel implements View{
    private static final Logger logger = LoggerFactory.getLogger(ToolbarView.class);
    private static final Color bgColor = new Color(60, 63, 65);
    private static final Color lineColor = new Color(77, 77, 77);
    private Controller controller;
    private final JTextPane appTitle = createAppTitle();

    public ToolbarView(JFrame mainView){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0 ,0, 1, 0, lineColor));
        setBackground(bgColor);
        add(createAppIcon(), BorderLayout.WEST);
        add(appTitle, BorderLayout.CENTER);
        Dragger.dragWindow(appTitle, mainView);
        add(createAppServiceButtons(), BorderLayout.EAST);
    }

    private JButton createAppIcon(){
        JButton icon = new JButton();
        icon.setBackground(bgColor);
        icon.setFocusPainted(false);
        icon.setBorderPainted(false);
        icon.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        try {
            Image img = ImageIO.read(new File("src/main/resources/img/toolbar/ping_ico.png"));
            icon.setIcon(new ImageIcon(img));
        } catch (IOException e) {
            logger.error("Cannot load image src/main/resources/img/toolbar/ping_ico.png\n" + e);
        }
        return icon;
    }

    private JTextPane createAppTitle(){
        // insert "me!" after "ping" and centralize text
        JTextPane title = new JTextPane();
        title.setText("ping");
        title.setForeground(Color.WHITE);
        try {
            //create custom font
            Font customFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/Bauhaus_ITC.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
            title.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            logger.warn("Cannot load font src/main/resources/fonts/Bauhaus_ITC.ttf" + e);
            title.setFont(new Font("Consolas", Font.PLAIN, 20));
        }
        if (!insertMeInTitle(title)){
            title.setText("pingme!");
        }
        title.setEditable(false);
        title.setBorder(BorderFactory.createEmptyBorder(5, 100, 7, 0));
        title.setBackground(bgColor);
        return title;
    }

    private boolean insertMeInTitle(JTextPane title){
        Style style = title.addStyle("Style", null);
        StyleConstants.setForeground(style, new Color(255, 192, 203));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument doc = title.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "me!",style);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            title.setDocument(doc);
        }
        catch (BadLocationException e){
            logger.warn("Cannot insert 'me!' in title\n" + e);
            return false;
        }
        return true;
    }

    private JPanel createAppServiceButtons(){
        JPanel serviceButtonsPanel = new JPanel();
        serviceButtonsPanel.setBackground(bgColor);
        serviceButtonsPanel.setLayout(new FlowLayout());

        ServiceButtons serviceButtons = new ServiceButtons();
        serviceButtonsPanel.add(serviceButtons.MINIMIZE_BTN);
        serviceButtonsPanel.add(serviceButtons.SETTINGS_BTN);
        serviceButtonsPanel.add(serviceButtons.CLOSE_BTN);

        return serviceButtonsPanel;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public class ServiceButtons{
        public final JButton MINIMIZE_BTN = createMinimizeButton();
        public final JCheckBox SETTINGS_BTN = createSettingsButton();
        public final JButton CLOSE_BTN = createCloseButton();

        private JButton createMinimizeButton(){
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
            try {
                Image minimize = ImageIO.read(new File("src/main/resources/img/toolbar/minimize.png"));
                btn.setIcon(new ImageIcon(minimize));
            } catch (IOException e) {
                e.printStackTrace();
            }
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //mainFrame.setState(Frame.ICONIFIED);
                }
            });
            return btn;
        }

        private JCheckBox createSettingsButton(){
            JCheckBox checkBox = new JCheckBox();
            checkBox.setBackground(bgColor);
            checkBox.setFocusPainted(false);
            checkBox.setBorderPainted(false);
            checkBox.setBorder(BorderFactory.createEmptyBorder(5, 8, 0, 8));
            try {
                Image minimize = ImageIO.read(new File("src/main/resources/img/toolbar/settings.png"));
                checkBox.setIcon(new ImageIcon(minimize));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return checkBox;
        }

        private JButton createCloseButton(){
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
            try {
                Image cross = ImageIO.read(new File("src/main/resources/img/toolbar/close.png"));
                btn.setIcon(new ImageIcon(cross));
            } catch (IOException e) {
                e.printStackTrace();
            }
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            return btn;
        }
    }

    public JTextPane getAppTitle() {
        return appTitle;
    }
}
