package main.java.view.settings;

import main.java.view.utils.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

public abstract class ToggleBtn extends JCheckBox{
    private static final Logger logger = LoggerFactory.getLogger(ToggleBtn.class);
    protected final JTextPane preview;

    public abstract void doChecked();

    public abstract void doUnchecked();

    public void checkCollision(){
        if (!isSelected())
            doUnchecked();
    }

    public ToggleBtn(JTextPane preview) {
        this.preview = preview;
        setPreferredSize(new Dimension(35, 40));
        setFocusPainted(false);
        setBorderPainted(false);
        setBackground(Colors.TOOLBAR_BACKGROUND);
        try {
            setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-off.png"))));
        } catch (IOException ex) {
            logger.warn("Can't load toggle resources " + ex.getMessage());
        }
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-on.png"))));
                    } catch (IOException ex) {
                        logger.warn("Can't load checked toggle resources " + ex.getMessage());
                    }
                    doChecked();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    try {
                        setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/img/settings/switch-off.png"))));
                    } catch (IOException ex) {
                        logger.warn("Can't load toggle resources " + ex.getMessage());
                    }
                    doUnchecked();
                }
            }
        });
    }
}
