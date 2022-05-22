package main.java.view;

import main.java.utils.FileUtils;
import main.java.utils.PropertiesUtils;
import main.java.utils.WindowUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Properties;

/**
 * Окно со статистикой (пинг, скорость сети)
 */
public class StatisticView extends JWindow {
    private final JTextPane statisticText = new JTextPane();
    private static StatisticView instance;
    private final Properties properties = PropertiesUtils.loadProperties(FileUtils.getFileFromResources("/config.properties"));

    public static StatisticView getInstance() {
        if (instance == null)
            instance = new StatisticView();
        return instance;
    }

    private StatisticView() {
        initialize();
    }

    private void initialize() {
        WindowUtil.dragWindow(statisticText, this);
        Map<String, Integer> windowProp = PropertiesUtils.getStatisticWindowProperties(FileUtils.getFileFromResources("/config.properties"));

        setLocation(windowProp.get("locationX"), windowProp.get("locationY"));
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);

        initStatisticText(windowProp);

        setVisible(true);
        pack();
    }

    private void initStatisticText(Map<String, Integer> windowProp) {
        statisticText.setText("connecting...");
        statisticText.setForeground(new Color(windowProp.get("red"), windowProp.get("green"), windowProp.get("blue"), windowProp.get("alpha")));
        statisticText.setFont(new Font("Consolas", Font.PLAIN, windowProp.get("fontSize")));
        statisticText.setOpaque(false);
        statisticText.setEditable(false);
        statisticText.setBackground(new Color(0, 0, 0, 0));
        statisticText.setBorder(null);
        statisticText.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, 200));
        add(statisticText, BorderLayout.WEST);
    }

    /**
     * Установить новые значения задержки и скорости
     *
     * @param ping          новый пинг
     * @param uploadSpeed   новая скорость загрузки
     * @param downloadSpeed новая скорость закачки
     */
    public void updateValues(String ping, long uploadSpeed, long downloadSpeed) {
        if (ping == null || ping.isEmpty())
            return;

        boolean isUploadSelected = Boolean.parseBoolean(properties.getProperty("upload"));
        boolean isDownloadSelected = Boolean.parseBoolean(properties.getProperty("download"));
        boolean isUnitsSelected = Boolean.parseBoolean(properties.getProperty("units"));
        boolean isLabelsSelected = Boolean.parseBoolean(properties.getProperty("labels"));

        statisticText.setText(getPing(ping, isUnitsSelected, isLabelsSelected) +
                getNetworkSpeed(isUploadSelected, isLabelsSelected, "upload: ", uploadSpeed, isUnitsSelected) +
                getNetworkSpeed(isDownloadSelected, isLabelsSelected, "download: ", downloadSpeed, isUnitsSelected));
    }

    /**
     * Получить строку с информацией о задержке сети
     *
     * @param ping             значение задержки
     * @param isUnitsSelected  нужна ли единицы измерения
     * @param isLabelsSelected нужна ли подпись перед значением
     */
    private String getPing(String ping, boolean isUnitsSelected, boolean isLabelsSelected) {
        StringBuilder builder = new StringBuilder();
        if (ping.equals("loss"))
            builder.append("loss\n");
        else {
            if (isLabelsSelected)
                builder.append("ping: ");
            builder.append(ping);
            if (isUnitsSelected)
                builder.append("ms");
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Получить строку с информацией о скорости сети (загрузка/закачка)
     * TODO убрать isSelected во внешний метод
     *
     * @param isSelected       нужна ли скорость
     * @param isLabelsSelected нужна ли подпись перед значением
     * @param label            подпись перед значением
     * @param speedValue       скорость загрузки или закачки
     * @param isUnitsSelected  нужны ли единицы измерения
     */
    private String getNetworkSpeed(boolean isSelected, boolean isLabelsSelected, String label, long speedValue, boolean isUnitsSelected) {
        if (!isSelected) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        if (isLabelsSelected) {
            builder.append(label);
        }
        builder.append(speedValue);
        if (isUnitsSelected) {
            builder.append("Mb/s");
        }
        builder.append("\n");
        return builder.toString();
    }

    public JTextPane getStatisticText() {
        return statisticText;
    }
}
