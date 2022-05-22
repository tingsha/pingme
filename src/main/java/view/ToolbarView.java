package main.java.view;

import main.java.controller.Controller;
import main.java.utils.Colors;
import main.java.utils.FileUtils;
import main.java.utils.ImageUtils;
import main.java.view.settings.SettingsView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Представление панели инструментов
 */
public class ToolbarView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ToolbarView.class);
    private final JTextPane appTitle = createAppTitle();
    private final JFrame mainView;
    private final Controller controller;

    public ToolbarView(JFrame mainView, Controller controller) {
        this.controller = controller;
        this.mainView = mainView;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Colors.DESELECTED_LINE));
        setBackground(Colors.TOOLBAR_BACKGROUND);
        JLabel icon = new JLabel(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/toolbar/ping_ico.png")));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        add(icon, BorderLayout.WEST);
        add(appTitle, BorderLayout.CENTER);
        add(createServiceButtons(), BorderLayout.EAST);
    }

    /**
     * Создать заголовок для окна
     */
    private JTextPane createAppTitle() {
        JTextPane title = new JTextPane();
        title.setText("ping");
        title.setForeground(Color.WHITE);
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/main/resources/fonts/Bauhaus_ITC.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            title.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            logger.warn("Exception during font loading 'Bauhaus_ITC.ttf'. " + e);
            title.setFont(new Font("Consolas", Font.PLAIN, 20));
        }
        if (!insertMeInTitle(title)) {
            title.setText("pingme!");
        }
        title.setEditable(false);
        title.setBorder(BorderFactory.createEmptyBorder(5, 100, 7, 0));
        title.setBackground(Colors.TOOLBAR_BACKGROUND);
        return title;
    }

    /**
     * Вставить 'me!' другого цвета в заголовке
     *
     * @param title заголовок приложения
     * @return true, если вставка прошла успешно, иначе false
     */
    private boolean insertMeInTitle(JTextPane title) {
        Style style = title.addStyle("Style", null);
        StyleConstants.setForeground(style, new Color(255, 192, 203));

        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);

        StyledDocument doc = title.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "me!", style);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            title.setDocument(doc);
        } catch (BadLocationException e) {
            logger.warn("Exception during 'me!' insertion in application title. " + e);
            return false;
        }
        return true;
    }

    /**
     * Создать кнопки управления окном
     */
    private JPanel createServiceButtons() {
        JPanel serviceButtonsPanel = new JPanel();
        serviceButtonsPanel.setBackground(Colors.TOOLBAR_BACKGROUND);
        serviceButtonsPanel.setLayout(new FlowLayout());

        serviceButtonsPanel.add(createMinimizeButton());
        serviceButtonsPanel.add(createSettingsButton());
        serviceButtonsPanel.add(createCloseButton());

        return serviceButtonsPanel;
    }

    /**
     * Создать кнопку для сворачивания окна
     */
    private JButton createMinimizeButton() {
        JButton btn = new JButton();
        btn.setBackground(Colors.TOOLBAR_BACKGROUND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
        btn.setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/toolbar/minimize.png")));
        btn.addActionListener(e -> mainView.setState(Frame.ICONIFIED));
        return btn;
    }

    /**
     * Создать кнопку для отображения окна с настройками
     */
    private JButton createSettingsButton() {
        JButton btn = new JButton();
        btn.setBackground(Colors.TOOLBAR_BACKGROUND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 8, 0, 8));
        btn.setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/toolbar/settings.png")));
        btn.addActionListener(e -> SettingsView.showSettings(mainView));
        return btn;
    }

    /**
     * Создать кнопку для закрытия окна
     */
    private JButton createCloseButton() {
        JButton btn = new JButton();
        btn.setBackground(Colors.TOOLBAR_BACKGROUND);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 12, 0, 12));
        btn.setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/toolbar/close.png")));
        btn.addActionListener(e -> {
            controller.onClickExit();
            System.exit(0);
        });
        return btn;
    }


    public JTextPane getAppTitle() {
        return appTitle;
    }
}
