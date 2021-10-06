package main.java.view;

import main.java.controller.Controller;
import main.java.view.slider.CustomSliderUI;
import main.java.view.utils.Colors;
import main.java.view.utils.TextBubbleBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.PanelUI;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;

public class SettingsView extends JDialog implements View {
    private Controller controller;
    private static final Logger logger = LoggerFactory.getLogger(SettingsView.class);
    private final JFrame mainView;
    private final JTextPane preview = getPreview();

    public SettingsView(JFrame mainView) {
        this.mainView = mainView;
        mainView.setEnabled(false);

        setUndecorated(true);
        setAlwaysOnTop(true);

        JPanel rootPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rootPanel.setBackground(Colors.TOOLBAR_BACKGROUND);
        rootPanel.setBorder(new TextBubbleBorder(new Color(77, 77, 77), 1, 16, 0,
                new Insets(20, 0, 0, 0), Colors.SERVERS_BACKGROUND));
        rootPanel.setPreferredSize(new Dimension(400, 480));

        rootPanel.add(createTitle("Preview"));
        rootPanel.add(createCloseBtn());
        rootPanel.add(preview);
        rootPanel.add(createSeparatedLine());
        rootPanel.add(createTitle("Settings"));
        rootPanel.add(createJTextField("Color"));
        rootPanel.add(createColorsPanel());
        rootPanel.add(createJTextField("Size", BorderFactory.createEmptyBorder(0, 12, 0, 10)));
        rootPanel.add(createSizeChooser());
        rootPanel.add(createJTextField("Alpha", BorderFactory.createEmptyBorder(0, 20, 0, 8)));
        rootPanel.add(createSlider());
        rootPanel.add(createJTextField("Upload", BorderFactory.createEmptyBorder(0, 20, 0, 12)));
        rootPanel.add(new ToggleBtn("upload: 60Mb/s\n"));
        rootPanel.add(createJTextField("Download", BorderFactory.createEmptyBorder(0, 145, 0, 12)));
        rootPanel.add(new ToggleBtn("download: 59Mb/s"));
        rootPanel.add(createJTextField("Labels", BorderFactory.createEmptyBorder(0, 20, 0, 14)));
        rootPanel.add(new ToggleBtn("ping:", "upload:", "download:"));
        rootPanel.add(createJTextField("Units", BorderFactory.createEmptyBorder(0, 146, 0, 41)));
        rootPanel.add(new ToggleBtn("ms", "Mb/s"));
        rootPanel.add(createVerificationPanel(), BorderLayout.SOUTH);

        add(rootPanel);

        setLocation(mainView.getX() + mainView.getWidth() / 2 - 400 / 2,
                mainView.getY() + mainView.getHeight() / 2 - 490 / 2);
        setPreferredSize(new Dimension(400, 480));
        setVisible(true);
        pack();
    }

    //TODO remove focus color
    private JPanel createVerificationPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        //panel.setBackground(new Color(49, 49, 51));
        panel.setPreferredSize(new Dimension(390, 70));

        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        JButton save = new JButton("Save");
        save.setFocusPainted(false);
        save.setContentAreaFilled(false);
        save.setBorder(new TextBubbleBorder(new Color(100, 98, 252), 1, 10,
                0, new Insets(0, 0, 0, 0), Colors.TOOLBAR_BACKGROUND));
        save.setOpaque(true);
        save.setBackground(new Color(100, 98, 252));
        save.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        save.setPreferredSize(new Dimension(80, 39));
        save.setForeground(new Color(253, 252, 255));

        JButton cancel = new JButton("Cancel");
        cancel.setBorder(new TextBubbleBorder(new Color(46, 45, 48), 1, 10, 0,
                new Insets(0, 0, 0,0), Colors.TOOLBAR_BACKGROUND));
        cancel.setFocusPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setPreferredSize(new Dimension(100, 40));
        cancel.setOpaque(true);
        cancel.setFocusPainted(false);
        cancel.setBackground(new Color(62, 61, 64));
        cancel.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        cancel.setForeground(new Color(149, 147, 158));

        panel.add(cancel);
        panel.add(save);

        return panel;
    }

    private JTextField createSizeChooser() {
        JTextField field = new JTextField("14");
        field.setForeground(Color.WHITE);
        field.setEditable(true);
        field.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
        field.setCaretColor(new Color(220, 220, 220));
        field.setBackground(Colors.TOOLBAR_BACKGROUND);
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setFontSize();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setFontSize();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                setFontSize();
            }

            public void setFontSize() {
                try {
                    if (!field.getText().equals("") && Integer.parseInt(field.getText()) > 0) {
                        preview.setFont(new Font("Helvetica Neue", Font.PLAIN, Integer.parseInt(field.getText())));
                    }
                }
                catch (NumberFormatException ignored){}
                catch (Exception e) {
                    logger.warn("Can't set font size " + e.getMessage());
                    preview.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
                }
            }
        });
        return field;
    }

    private JSlider createSlider() {
        try {
            SynthLookAndFeel laf = new SynthLookAndFeel();
            laf.load(SettingsView.class.getResourceAsStream("slider/slider.xml"), SettingsView.class);
            UIManager.setLookAndFeel(laf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSlider slider = new JSlider(0, 255) {
            @Override
            public void updateUI() {
                setUI(new CustomSliderUI(this));
            }
        };
        slider.setPreferredSize(new Dimension(320, 40));
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

    private JPanel createSeparatedLine() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(new Color(49, 49, 51));
                g2d.drawLine(20, 0, 400, 0);
            }
        };
        panel.setPreferredSize(new Dimension(380, 3));
        panel.setBackground(Color.RED);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        return panel;
    }

    private JPanel createColorsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(0);
        flowLayout.setAlignment(FlowLayout.LEFT);
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
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
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
                    if (e.getStateChange() == ItemEvent.SELECTED) {
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

    private JTextField createJTextField(String text, Border border) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        textField.setForeground(new Color(155, 153, 165));
        textField.setEditable(false);
        textField.setBackground(Colors.TOOLBAR_BACKGROUND);
        textField.setBorder(border);
        return textField;
    }

    private JTextField createJTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        textField.setForeground(new Color(155, 153, 165));
        textField.setEditable(false);
        textField.setBackground(Colors.TOOLBAR_BACKGROUND);
        textField.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        return textField;
    }

    private JTextPane getPreview() {
        JTextPane preview = new JTextPane();
        preview.setText("ping: 43ms\n" +
                "upload: 60Mb/s\n" +
                "download: 59Mb/s");
        StyledDocument doc = preview.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        preview.setBackground(Colors.TOOLBAR_BACKGROUND);
        preview.setEditable(false);
        preview.setBorder(null);
        preview.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        preview.setPreferredSize(new Dimension(390, 100));
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

    private JTextField createTitle(String text) {
        JTextField title = new JTextField(text);
        title.setPreferredSize(new Dimension(340, 40));
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Helvetica Neue", Font.PLAIN, 24));
        title.setBackground(Colors.TOOLBAR_BACKGROUND);
        title.setEditable(false);
        title.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        return title;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    private static class ColorBtn extends JCheckBox {
        private final Color btnColor;

        public ColorBtn(Color btnColor) {
            this.btnColor = btnColor;
        }

        public Color getBtnColor() {
            return btnColor;
        }
    }

    private class ToggleBtn extends JCheckBox {
        private String sourcePreviewText;

        public ToggleBtn(String... stringsToRemove) {
            sourcePreviewText = preview.getText();
            setPreferredSize(new Dimension(35, 40));
            setFocusPainted(false);
            setBorderPainted(false);
            try {
                setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-off.png"))));
            } catch (IOException ex) {
                logger.warn("Can't load toggle resources " + ex.getMessage());
            }
            addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        preview.setText(sourcePreviewText);
                        try {
                            setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-on.png"))));
                        } catch (IOException ex) {
                            logger.warn("Can't load checked toggle resources " + ex.getMessage());
                        }
                    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                        sourcePreviewText = preview.getText();
                        String newPreviewText = preview.getText();
                        for (String s : stringsToRemove) {
                            newPreviewText = newPreviewText.replaceAll(s, "");
                        }
                        preview.setText(newPreviewText);
                        try {
                            setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-off.png"))));
                        } catch (IOException ex) {
                            logger.warn("Can't load toggle resources " + ex.getMessage());
                        }
                    }
                }
            });
        }
    }
}
