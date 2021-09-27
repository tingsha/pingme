package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class SettingsView extends JWindow implements View {
    private Controller controller;
    private static final Logger logger = LoggerFactory.getLogger(SettingsView.class);
    private JFrame mainView;

    public SettingsView(JFrame mainView) {
        this.mainView = mainView;
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);
        mainView.setEnabled(false);
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBackground(Colors.TOOLBAR_BACKGROUND);
        rootPanel.setBorder(new TextBubbleBorder(Colors.TOOLBAR_BACKGROUND, 10, 10, 0));
        getContentPane().setBackground(Colors.TOOLBAR_BACKGROUND);
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 30, 0);
        rootPanel.add(createTitle(), constraints);

        constraints.gridx = 1;
        rootPanel.add(createCloseBtn(), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        rootPanel.add(getPreview(), constraints);

        constraints.insets = new Insets(5, 0, 30, 0);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        rootPanel.add(createColorTitle(), constraints);

        constraints.insets = new Insets(0, 0, 30, 0);
        constraints.gridx = 1;
        rootPanel.add(createColorsPanel(), constraints);

        rootPanel.setPreferredSize(new Dimension(350, 400));
        add(rootPanel);

        setLocation(mainView.getX() + mainView.getWidth() / 2 - 350 / 2,
                mainView.getY() + mainView.getHeight() / 2 - 400 / 2);
        setPreferredSize(new Dimension(350, 400));
        setVisible(true);
        pack();
    }

    private JPanel createColorsPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(10);
        panel.setLayout(flowLayout);
        String[] paths = {"src/main/resources/img/settings/redCircle.png",
                "src/main/resources/img/settings/yellowCircle.png",
                "src/main/resources/img/settings/greenCircle.png",
                "src/main/resources/img/settings/blackCircle.png",
                "src/main/resources/img/settings/purpleCircle.png"};
        String[] checkedPaths = {"src/main/resources/img/settings/redCircleChecked.png",
                "src/main/resources/img/settings/yellowCircleChecked.png",
                "src/main/resources/img/settings/greenCircleChecked.png",
                "src/main/resources/img/settings/blackCircleChecked.png",
                "src/main/resources/img/settings/purpleCircleChecked.png"};
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            JCheckBox btn = createColorBtn(Path.of(paths[i]));
            group.add(btn);
            panel.add(btn);
            try {
                btn.setSelectedIcon(new ImageIcon(ImageIO.read(new File(checkedPaths[i]))));
            } catch (IOException e) {
                logger.warn("Can't load selected color icon " + e.getMessage());
            }
        }
        return panel;
    }

    private JTextField createColorTitle() {
        JTextField color = new JTextField("Color");
        color.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        color.setForeground(new Color(155, 153, 165));
        color.setEditable(false);
        color.setBackground(Colors.TOOLBAR_BACKGROUND);
        color.setBorder(null);
        return color;
    }

    private JTextPane getPreview() {
        JTextPane preview = new JTextPane();
        preview.setText("ping: 43ms");
        StyledDocument doc = preview.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        preview.setBackground(Colors.TOOLBAR_BACKGROUND);
        preview.setEditable(false);
        preview.setBorder(null);
        preview.setFont(new Font("Helvetica Neue", Font.PLAIN, 18));
        preview.setForeground(Color.RED);
        return preview;
    }

    private JButton createCloseBtn() {
        JButton closeBtn = new JButton();
        try {
            Image close = ImageIO.read(new File("src/main/resources/img/settings/cross.png"));
            closeBtn.setIcon(new ImageIcon(close));
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainView.setEnabled(true);
                setVisible(false);
            }
        });
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBackground(Colors.TOOLBAR_BACKGROUND);
        closeBtn.setBorder(null);
        closeBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        return closeBtn;
    }

    private JTextField createTitle() {
        JTextField title = new JTextField("Preview");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
        title.setBackground(Colors.TOOLBAR_BACKGROUND);
        title.setEditable(false);
        title.setBorder(null);
        return title;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    private JCheckBox createColorBtn(Path pathToIcon) {
        JCheckBox btn = new JCheckBox();
        btn.setBackground(Colors.TOOLBAR_BACKGROUND);
        btn.setBorder(null);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        try {
            Image icon = ImageIO.read(pathToIcon.toFile());
            btn.setIcon(new ImageIcon(icon));
        } catch (IOException e) {
            logger.error("Can't create color circle " + e.getMessage());
        }
        return btn;
    }
}