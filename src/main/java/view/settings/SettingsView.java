package main.java.view.settings;

import main.java.controller.Controller;
import main.java.view.View;
import main.java.view.utils.Colors;
import main.java.view.utils.TextBubbleBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Properties;

public class SettingsView extends JDialog implements View {
    private Controller controller;
    private static final Logger logger = LoggerFactory.getLogger(SettingsView.class);
    private final JFrame mainView;
    private final JTextPane preview = getPreview();

    private final ToggleBtn labelsBtn = new LabelsBtn(preview);
    private final ToggleBtn unitsBtn = new UnitsBtn(preview);
    private final ToggleBtn uploadBtn = new UploadBtn(preview);
    private final ToggleBtn downloadBtn = new DownloadBtn(preview);

    private final JSlider slider = new CustomSlider();
    private final JTextField sizeChooser = createSizeChooser();

    private Color colorToSave;
    private int sizeToSave = 0;
    private int alphaToSave = 0;

    public SettingsView(JFrame mainView) {
        this.mainView = mainView;
        mainView.setEnabled(false);

        setUndecorated(true);
        setAlwaysOnTop(true);

        JPanel rootPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rootPanel.setOpaque(true);
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
        rootPanel.add(sizeChooser);
        rootPanel.add(createJTextField("Alpha", BorderFactory.createEmptyBorder(0, 20, 0, 8)));
        rootPanel.add(slider);
        rootPanel.add(createJTextField("Upload", BorderFactory.createEmptyBorder(0, 20, 0, 12)));
        rootPanel.add(uploadBtn);
        rootPanel.add(createJTextField("Download", BorderFactory.createEmptyBorder(0, 145, 0, 12)));
        rootPanel.add(downloadBtn);
        rootPanel.add(createJTextField("Labels", BorderFactory.createEmptyBorder(0, 20, 0, 14)));
        rootPanel.add(labelsBtn);
        rootPanel.add(createJTextField("Units", BorderFactory.createEmptyBorder(0, 146, 0, 41)));
        rootPanel.add(unitsBtn);
        rootPanel.add(createVerificationPanel(), BorderLayout.SOUTH);

        add(rootPanel);

        setLocation(mainView.getX() + mainView.getWidth() / 2 - 400 / 2,
                mainView.getY() + mainView.getHeight() / 2 - 490 / 2);
        setPreferredSize(new Dimension(400, 480));
        setVisible(true);
        pack();

        loadSettings();

        labelsBtn.checkCollision();
        unitsBtn.checkCollision();
    }

    private void loadSettings() {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);
            int red = Integer.parseInt(properties.getProperty("red"));
            int green = Integer.parseInt(properties.getProperty("green"));
            int blue = Integer.parseInt(properties.getProperty("blue"));
            int alpha = Integer.parseInt(properties.getProperty("alpha"));

            preview.setForeground(new Color(red, green, blue, alpha));
            slider.setValue(alpha);
            sizeChooser.setText(properties.getProperty("size"));
            labelsBtn.setSelected(Boolean.parseBoolean(properties.getProperty("labels")));
            unitsBtn.setSelected(Boolean.parseBoolean(properties.getProperty("units")));
            uploadBtn.setSelected(Boolean.parseBoolean(properties.getProperty("upload")));
            downloadBtn.setSelected(Boolean.parseBoolean(properties.getProperty("download")));

            ToggleBtn[] btns = {uploadBtn, downloadBtn, labelsBtn, unitsBtn};
        } catch (IOException e) {
            logger.error("Can't load settings " + e.getMessage());
        }
    }

    private void saveSettings() {
        Properties properties = new Properties();
        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/config.properties")) {
            if (colorToSave != null) {
                properties.setProperty("red", String.valueOf(colorToSave.getRed()));
                properties.setProperty("green", String.valueOf(colorToSave.getGreen()));
                properties.setProperty("blue", String.valueOf(colorToSave.getBlue()));
            } else {
                properties.setProperty("red", "255");
                properties.setProperty("green", "0");
                properties.setProperty("blue", "0");
            }
            properties.setProperty("alpha", String.valueOf(alphaToSave));
            properties.setProperty("size", String.valueOf(sizeToSave));
            properties.setProperty("upload", String.valueOf(uploadBtn.isSelected()));
            properties.setProperty("download", String.valueOf(downloadBtn.isSelected()));
            properties.setProperty("labels", String.valueOf(labelsBtn.isSelected()));
            properties.setProperty("units", String.valueOf(unitsBtn.isSelected()));
            properties.store(outputStream, null);
        } catch (IOException e) {
            logger.error("Can't save settings " + e.getMessage());
        }
    }

    //TODO remove focus color
    private JPanel createVerificationPanel() {
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
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
            }
        });

        JButton cancel = new JButton("Cancel");
        cancel.setBorder(new TextBubbleBorder(new Color(46, 45, 48), 1, 10, 0,
                new Insets(0, 0, 0, 0), Colors.TOOLBAR_BACKGROUND));
        cancel.setFocusPainted(false);
        cancel.setContentAreaFilled(false);
        cancel.setPreferredSize(new Dimension(100, 40));
        cancel.setOpaque(true);
        cancel.setFocusPainted(false);
        cancel.setBackground(new Color(62, 61, 64));
        cancel.setFont(new Font("Helvetica Neue", Font.PLAIN, 16));
        cancel.setForeground(new Color(149, 147, 158));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                mainView.setEnabled(true);
            }
        });

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
                        sizeToSave = Integer.parseInt(field.getText());
                    }
                } catch (NumberFormatException ignored) {
                } catch (Exception e) {
                    logger.warn("Can't set font size " + e.getMessage());
                    preview.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
                }
            }
        });
        return field;
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
                        colorToSave = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                preview.getForeground().getAlpha());
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
        preview.setText("ping: 43ms\n");
                /*"upload: 60Mb/s\n" +
                "download: 59Mb/s");*/
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

    private class CustomSlider extends JSlider {
        public CustomSlider(){
            setPreferredSize(new Dimension(320, 40));
            setBackground(Colors.TOOLBAR_BACKGROUND);
            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    Color rgb = new Color(preview.getForeground().getRGB());
                    preview.setForeground(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(),
                            ((JSlider) e.getSource()).getValue()));
                    alphaToSave = ((JSlider) e.getSource()).getValue();
                }
            });
        }

        @Override
        public void updateUI(){
            setUI(new CustomSliderUI(this));
        }
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
}
