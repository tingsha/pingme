package main.java.view;

import main.java.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.synth.SynthLookAndFeel;
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
    private JTextPane preview = getPreview();

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
        rootPanel.add(preview, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        rootPanel.add(createSeparatedLine(), constraints);

        constraints.insets = new Insets(10, 0, 30, 0);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        rootPanel.add(createJTextField("Color"), constraints);

        constraints.insets = new Insets(0, 0, 30, 0);
        constraints.gridx = 1;
        rootPanel.add(createColorsPanel(), constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        rootPanel.add(createJTextField("Alpha"), constraints);

        constraints.gridx = 1;
        rootPanel.add(createSlider(), constraints);

        rootPanel.setPreferredSize(new Dimension(350, 400));
        add(rootPanel);

        setLocation(mainView.getX() + mainView.getWidth() / 2 - 350 / 2,
                mainView.getY() + mainView.getHeight() / 2 - 400 / 2);
        setPreferredSize(new Dimension(350, 400));
        setVisible(true);
        pack();
    }

    private JSlider createSlider(){
        try {
            SynthLookAndFeel laf = new SynthLookAndFeel();
            laf.load(SettingsView.class.getResourceAsStream("slider/slider.xml"), SettingsView.class);
            UIManager.setLookAndFeel(laf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSlider slider = new JSlider(0, 255);
        slider.setBackground(Colors.TOOLBAR_BACKGROUND);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Color rgb = new Color(preview.getForeground().getRGB());
                preview.setForeground(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(),
                        ((JSlider) e.getSource()).getValue()));
            }
        });
        return slider;
    }

    private JPanel createSeparatedLine(){
        JPanel panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(Colors.DESELECTED_LINE);
                g2d.drawLine(0, 0,300, 0);
            }
        };
        panel.setPreferredSize(new Dimension(300, 3));
        panel.setBackground(Color.RED);
        return panel;
    }

    private JPanel createColorsPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(10);
        panel.setLayout(flowLayout);
        String[] uncheckedPaths = {"src/main/resources/img/settings/redCircle.png",
                "src/main/resources/img/settings/yellowCircle.png",
                "src/main/resources/img/settings/greenCircle.png",
                "src/main/resources/img/settings/blackCircle.png",
                "src/main/resources/img/settings/purpleCircle.png"};
        String[] checkedPaths = {"src/main/resources/img/settings/redCircleChecked.png",
                "src/main/resources/img/settings/yellowCircleChecked.png",
                "src/main/resources/img/settings/greenCircleChecked.png",
                "src/main/resources/img/settings/blackCircleChecked.png",
                "src/main/resources/img/settings/purpleCircleChecked.png"};
        Color[] colors = {new Color(255, 0, 0),
                new Color(250, 200, 28),
                new Color(64, 208, 182),
                new Color(0, 0, 0),
                new Color(100, 98, 252)};
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            ColorBtn btn = new ColorBtn(colors[i]);
            btn.setBackground(Colors.TOOLBAR_BACKGROUND);
            try {
                btn.setSelectedIcon(new ImageIcon(ImageIO.read(new File(checkedPaths[i]))));
                btn.setIcon(new ImageIcon(ImageIO.read(new File(uncheckedPaths[i]))));
            } catch (IOException e) {
                logger.warn("Can't load color icon " + e.getMessage());
            }
            btn.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED){
                        Color color = btn.getBtnColor();
                        preview.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                preview.getForeground().getAlpha()));
                    }
                }
            });
            group.add(btn);
            panel.add(btn);
        }
        return panel;
    }

    private JTextField createJTextField(String text) {
        JTextField color = new JTextField(text);
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

    private class ColorBtn extends JCheckBox{
        private final Color btnColor;

        public ColorBtn(Color btnColor) {
            this.btnColor = btnColor;
        }

        public Color getBtnColor() {
            return btnColor;
        }
    }
}