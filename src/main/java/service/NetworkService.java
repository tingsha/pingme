package main.java.service;

import main.java.model.PingTask;
import main.java.model.SpeedTestTask;
import main.java.utils.ThreadUtils;
import main.java.view.StatisticView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class NetworkService {
    private static final Logger logger = LoggerFactory.getLogger(NetworkService.class);
    /**
     * Выполняет тестирование задержки и скорости сети
     */
    private static ExecutorService executor;
    /**
     * Запущен ли мониторинг
     */
    private boolean monitoring = false;

    private SpeedTestTask speedTestTask;
    private PingTask pingTask;

    /**
     * Запустить задачи {@link PingTask} и {@link SpeedTestTask}
     */
    public void startMonitoring(String serverAddress) {
        executor = ThreadUtils.getDaemonExecutorService(2);
        speedTestTask = new SpeedTestTask(this);
        pingTask = new PingTask(serverAddress, this);
        executor.submit(pingTask);
        executor.submit(speedTestTask);
        monitoring = true;
    }

    /**
     * Остановить задачи {@link PingTask} и {@link SpeedTestTask}
     */
    public void stopMonitoring() {
        pingTask.stopMonitoring();
        speedTestTask.stopMonitoring();
        ThreadUtils.shutdownExecutor(executor);
        monitoring = false;
    }

    //TODO убрать из класса
    public void refreshView() {
        Runnable runnable = () -> {
            while (monitoring) {
                try {
                    StatisticView.getInstance().updateValues(
                            getPing(),
                            getUploadSpeed(),
                            getDownloadSpeed()
                    );
                    Thread.sleep(1000);
                    StatisticView.getInstance().getStatisticText().setText("");
                } catch (InterruptedException e) {
                    logger.warn("Thread was interrupted " + e);
                }
            }
        };
        ExecutorService executor = ThreadUtils.getDaemonExecutorService(1);
        executor.execute(runnable);
    }

    public String getPing() {
        return pingTask.getPing();
    }

    public int getDownloadSpeed() {
        return speedTestTask.getDownloadSpeed();
    }

    public int getUploadSpeed() {
        return speedTestTask.getUploadSpeed();
    }

    public boolean isMonitoring() {
        return monitoring;
    }
}
