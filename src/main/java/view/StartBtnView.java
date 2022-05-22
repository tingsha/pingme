package main.java.view;

import main.java.controller.Controller;
import main.java.utils.Colors;
import main.java.utils.FileUtils;
import main.java.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Представление кнопки старта
 */
public class StartBtnView extends JCheckBox {
    private final Controller controller;
    private final Logger logger = LoggerFactory.getLogger(StartBtnView.class);
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public StartBtnView(Controller controller) {
        this.controller = controller;
        setHorizontalAlignment((int) CENTER_ALIGNMENT);
        setBackground(Colors.SERVERS_BACKGROUND);
        setBorder(BorderFactory.createEmptyBorder(0, 17, 0, 7));
        setBorderPaintedFlat(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setIcon(ImageUtils.getImageIcon(FileUtils.getFileFromResources("/img/charger/charger_grey.png")));
        addItemListener(new StartBtnListener());
    }

    /**
     * Если кнопка выбрана, то запускает анимацию и вызывает {@link Controller#onSelectStartBtn}
     */
    private class StartBtnListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setEnabled(false);
                executorService.submit(new AnimationTask(false));
                Timer timer = new Timer(500, event -> {
                    controller.onSelectStartBtn();
                    setEnabled(true);
                });
                timer.setRepeats(false);
                timer.start();
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                executorService.submit(new AnimationTask(true));
                controller.onDeselectPingBtn();
                StatisticView.getInstance().setVisible(false);
            }
        }
    }

    /**
     * Задача анимации. Анимация реализована в виде сменяющихся изображений
     */
    private class AnimationTask implements Runnable {
        /**
         * Нужно ли воспроизводить анимацию в обратном порядке
         */
        final boolean reversedOrder;
        /**
         * Задержка смены кадров
         */
        static final int DELAY_IN_MILLIS = 100;
        /**
         * Кадры для анимации
         */
        final List<File> animationFrames = new ArrayList<>(List.of(
                FileUtils.getFileFromResources("/img/charger/anim0.png"),
                FileUtils.getFileFromResources("/img/charger/anim1.png"),
                FileUtils.getFileFromResources("/img/charger/anim2.png"),
                FileUtils.getFileFromResources("/img/charger/anim3.png"),
                FileUtils.getFileFromResources("/img/charger/anim4.png")));

        AnimationTask(boolean reversedOrder) {
            this.reversedOrder = reversedOrder;
        }

        /**
         * Раз в {@link AnimationTask#DELAY_IN_MILLIS} устанавливает кнопке изображение из {@link AnimationTask#animationFrames}
         */
        @Override
        public void run() {
            try {
                if (reversedOrder) {
                    Collections.reverse(animationFrames);
                }
                for (File file : animationFrames) {
                    setIcon(ImageUtils.getImageIcon(file));
                    Thread.sleep(DELAY_IN_MILLIS);
                }
            } catch (InterruptedException e) {
                logger.error("Animation thread was interrupted. " + e);
                throw new RuntimeException(e);
            }
        }
    }
}
