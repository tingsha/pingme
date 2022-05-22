package main.java.view.settings;

import main.java.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Представление окна с настройками
 */
public class SettingsView extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger(SettingsView.class);
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

    private static SettingsView instance;

    /**
     * Отобразить окно по центру {@code mainFrame}
     */
    public static void showSettings(JFrame mainFrame) {
        if (instance == null) {
            instance = new SettingsView(mainFrame);
        } else {
            instance.setVisible(true);
        }
    }

    private SettingsView(JFrame mainFrame) {
        setUndecorated(true);
        setAlwaysOnTop(true);

        add(createContent());

        setPreferredSize(new Dimension(400, 480));
        setVisible(true);
        pack();
        setLocationRelativeTo(mainFrame);

        loadSettings();

        labelsBtn.uncheck();
        unitsBtn.uncheck();
    }

    /**
     * Создать компоненты
     */
    private JPanel createContent() {
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
        rootPanel.add(createControlBtns(), BorderLayout.SOUTH);
        return rootPanel;
    }

    /**
     * Установить полям настройки из {@code config.properties}
     */
    private void loadSettings() {
        Properties properties = PropertiesUtils.loadProperties(FileUtils.getFileFromResources("/config.properties"));
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
    }

    /**
     * Сохранить настройки в {@code config.properties}
     */
    private void saveSettings() {
        Map<String, String> newProperties = new HashMap<>();
        if (colorToSave != null) {
            newProperties.put("red", String.valueOf(colorToSave.getRed()));
            newProperties.put("green", String.valueOf(colorToSave.getGreen()));
            newProperties.put("blue", String.valueOf(colorToSave.getBlue()));
        } else {
            newProperties.put("red", "255");
            newProperties.put("green", "0");
            newProperties.put("blue", "0");
        }
        newProperties.put("alpha", String.valueOf(alphaToSave));
        newProperties.put("size", String.valueOf(sizeToSave));
        newProperties.put("upload", String.valueOf(uploadBtn.isSelected()));
        newProperties.put("download", String.valueOf(downloadBtn.isSelected()));
        newProperties.put("labels", String.valueOf(labelsBtn.isSelected()));
        newProperties.put("units", String.valueOf(unitsBtn.isSelected()));
        PropertiesUtils.rewriteProperties(FileUtils.getFileFromResources("/config.properties"), newProperties);
    }

    //TODO remove focus color

    /**
     * Панель с кнопками "Сохранить" и "Отмена"
     */
    private JPanel createControlBtns() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panel.setOpaque(true);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        panel.setPreferredSize(new Dimension(390, 70));

        UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
        JButton save = createSaveBtn();

        JButton cancel = createCancelBtn();

        panel.add(cancel);
        panel.add(save);

        return panel;
    }

    /**
     * Создать кнопку отмены
     */
    private JButton createCancelBtn() {
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
        cancel.addActionListener(e -> setVisible(false));
        return cancel;
    }

    /**
     * Создать кнопку сохранения
     */
    private JButton createSaveBtn() {
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
        save.addActionListener(e -> saveSettings());
        return save;
    }

    /**
     * Создать поле для выбора размера текста
     */
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
                    logger.warn("Exception during text size changing in settings window. " + e.getMessage());
                    preview.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
                }
            }
        });
        return field;
    }

    /**
     * Создать линию-разделитель
     */
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

    /**
     * Создать панель для выбора цвета текста
     */
    private JPanel createColorsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Colors.TOOLBAR_BACKGROUND);
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(0);
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel.setLayout(flowLayout);
        File[] uncheckedBtnImages = {FileUtils.getFileFromResources("/img/settings/redCircle.png"),
                FileUtils.getFileFromResources("/img/settings/yellowCircle.png"),
                FileUtils.getFileFromResources("/img/settings/greenCircle.png"),
                FileUtils.getFileFromResources("/img/settings/blackCircle.png"),
                FileUtils.getFileFromResources("/img/settings/purpleCircle.png")};
        File[] checkedBtnImages = {FileUtils.getFileFromResources("/img/settings/redCircleChecked.png"),
                FileUtils.getFileFromResources("/img/settings/yellowCircleChecked.png"),
                FileUtils.getFileFromResources("/img/settings/greenCircleChecked.png"),
                FileUtils.getFileFromResources("/img/settings/blackCircleChecked.png"),
                FileUtils.getFileFromResources("/img/settings/purpleCircleChecked.png")};
        Color[] colors = {new Color(255, 0, 0),
                new Color(250, 200, 28),
                new Color(64, 208, 182),
                new Color(0, 0, 0),
                new Color(100, 98, 252)};
        createColorButtons(panel, uncheckedBtnImages, checkedBtnImages, colors);
        return panel;
    }

    /**
     * Создать кнопки для выбора цвета текста
     *
     * @param uncheckedBtnImages изображения, если кнопки не выбраны
     * @param checkedBtnImages   изображения, если кнопки выбраны
     * @param colors             цвета
     */
    private void createColorButtons(JPanel panel, File[] uncheckedBtnImages, File[] checkedBtnImages, Color[] colors) {
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            ColorBtn btn = new ColorBtn(colors[i]);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            btn.setBackground(Colors.TOOLBAR_BACKGROUND);
            btn.setSelectedIcon(ImageUtils.getImageIcon(checkedBtnImages[i]));
            btn.setIcon(ImageUtils.getImageIcon(uncheckedBtnImages[i]));
            btn.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Color color = btn.getBtnColor();
                    preview.setForeground(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            preview.getForeground().getAlpha()));
                    colorToSave = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            preview.getForeground().getAlpha());
                }
            });
            group.add(btn);
            panel.add(btn);
        }
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

    /**
     * Создать превью для выбранных настроек
     */
    private JTextPane getPreview() {
        JTextPane preview = new JTextPane();
        preview.setText("ping: 43ms\n");
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

    /**
     * Создать кнопку для закрытия окна настроек
     */
    private JButton createCloseBtn() {
        JButton closeBtn = new JButton();
        try {
            Image close = ImageIO.read(new File("src/main/resources/img/settings/cross.png"));
            closeBtn.setIcon(new ImageIcon(close));
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeBtn.addActionListener(e -> setVisible(false));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setBorder(null);
        closeBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        return closeBtn;
    }

    /**
     * Создать поле с названием меню настроек
     */
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

    /**
     * Слайдер для изменения яркости текста
     */
    private class CustomSlider extends JSlider {
        public CustomSlider() {
            setPreferredSize(new Dimension(320, 40));
            setBackground(Colors.TOOLBAR_BACKGROUND);
            addChangeListener(e -> {
                Color rgb = new Color(preview.getForeground().getRGB());
                preview.setForeground(new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(),
                        ((JSlider) e.getSource()).getValue()));
                alphaToSave = ((JSlider) e.getSource()).getValue();
            });
        }

        @Override
        public void updateUI() {
            setUI(new main.java.view.settings.CustomSlider(this));
        }
    }

    /**
     * Кнопка для выбора цвета
     */
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
