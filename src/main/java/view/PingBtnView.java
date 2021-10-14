package main.java.view;

import main.java.controller.Controller;
import main.java.model.PingTask;
import main.java.model.SpeedTestTask;
import main.java.view.utils.Colors;
import main.java.view.utils.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PingBtnView extends JCheckBox {
    private Controller controller;
    private final Color bgColor = Colors.SERVERS_BACKGROUND;
    private final static Logger logger = LoggerFactory.getLogger(PingBtnView.class);

    public PingBtnView(Controller controller){
        this.controller = controller;
        setHorizontalAlignment((int) CENTER_ALIGNMENT);
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(0, 17, 0, 7));
        setBorderPaintedFlat(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setImageIcon(Path.of("src/main/resources/img/charger/charger_grey.png"));
        addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ex) {
                if (ex.getStateChange() == ItemEvent.SELECTED) {
                    new AnimationThread(false).start();
                    Timer timer = new Timer(500, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            controller.onSelectPingBtn();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    while (ex.getStateChange() == ItemEvent.SELECTED){
                                        StatisticView.getInstance().setVisible(true);
                                        StatisticView.getInstance().refresh(PingTask.getPing(),
                                                SpeedTestTask.getUploadSpeed(),
                                                SpeedTestTask.getDownloadSpeed());
                                    }
                                }
                            };
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(runnable);
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                } else if (ex.getStateChange() == ItemEvent.DESELECTED){
                    new AnimationThread(true).start();
                    StatisticView.getInstance().setVisible(false);
                    controller.onDeselectPingBtn();
                }
            }
        });
    }

    public void setImageIcon(Path pathToIcon){
        try {
            Image cbIcon = ImageIO.read(pathToIcon.toFile());
            setIcon(new ImageIcon(cbIcon));
        } catch (IOException e) {
            logger.warn("Can't load ping btn image! " + e.getMessage() + " " + pathToIcon);
        }
    }

    private class AnimationThread extends Thread{
        final boolean reversedOrder;
        final Path[] paths = new Path[]{
                Path.of("src/main/resources/img/charger/anim0.png"),
                Path.of("src/main/resources/img/charger/anim1.png"),
                Path.of("src/main/resources/img/charger/anim2.png"),
                Path.of("src/main/resources/img/charger/anim3.png"),
                Path.of("src/main/resources/img/charger/anim4.png")};

        AnimationThread(boolean reversedOrder){
            this.reversedOrder = reversedOrder;
        }

        @Override
        public void run(){
            if (!reversedOrder){
                for (Path path : paths){
                    setImageIcon(path);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } else{
                for (int i = paths.length-1; i > 0; i--) {
                    setImageIcon(paths[i]);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            interrupt();
        }
    }
}
