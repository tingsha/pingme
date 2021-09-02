package main.java.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ToolbarView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ToolbarView.class);
    private static final Color bgColor = new Color(60, 63, 65);
    private static final Color lineColor = new Color(77, 77, 77);

    public ToolbarView(){
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0 ,0, 1, 0, lineColor));
        setBackground(bgColor);
        add(createAppIcon(), BorderLayout.WEST);
        add(createAppTitle(), BorderLayout.CENTER);
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
            logger.warn("Cannot load font src/main/resources/fonts/Bauhaus_ITC.ttf\n" + e);
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
        JPanel serviceButtons = new JPanel();
        serviceButtons.setBackground(bgColor);
        serviceButtons.setLayout(new FlowLayout());
        serviceButtons.add(ServiceButtons.MINIMIZE_BTN);
        serviceButtons.add(ServiceButtons.SETTINGS_BTN);
        serviceButtons.add(ServiceButtons.CLOSE_BTN);
        return serviceButtons;
    }

    public static class ServiceButtons{
        public static final JButton MINIMIZE_BTN = createMinimizeButton();
        public static final JCheckBox SETTINGS_BTN = createSettingsButton();
        public static final JButton CLOSE_BTN = createCloseButton();

        private static JButton createMinimizeButton(){
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
            try {
                Image minimize = ImageIO.read(new File("src/main/resources/img/toolbar/minimize.png"));
                btn.setIcon(new ImageIcon(minimize));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return btn;
        }

        private static JCheckBox createSettingsButton(){
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

        private static JButton createCloseButton(){
            JButton btn = new JButton();
            btn.setBackground(bgColor);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
            try {
                Image cross = ImageIO.read(new File("src/main/resources/img/toolbar/close.png"));
                btn.setIcon(new ImageIcon(cross));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return btn;
        }
    }
}
