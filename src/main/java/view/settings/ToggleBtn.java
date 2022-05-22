package main.java.view.settings;

import main.java.utils.Colors;
import main.java.utils.FileUtils;
import main.java.utils.ImageUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Базовый класс для кнопок-переключателей
 */
public abstract class ToggleBtn extends JCheckBox {
    protected final JTextPane preview;

    public abstract void doChecked();

    public abstract void doUnchecked();

    public void uncheck() {
        if (!isSelected())
            doUnchecked();
    }

    public ToggleBtn(JTextPane preview) {
        this.preview = preview;
        setPreferredSize(new Dimension(35, 40));
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(Colors.TOOLBAR_BACKGROUND);
        setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/settings/switch-off.png")));
        addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/settings/switch-on.png")));
                doChecked();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/settings/switch-off.png")));
                doUnchecked();
            }
        });
    }
}
