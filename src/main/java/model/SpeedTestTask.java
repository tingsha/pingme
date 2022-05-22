package main.java.model;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import main.java.service.NetworkService;
import main.java.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * Задача для мониторинга скорости сети
 */
public class SpeedTestTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SpeedTestTask.class);
    /**
     * Скорость закачки в Мб/с
     */
    private volatile int downloadSpeed;
    /**
     * Скорость загрузки в Мб/с
     */
    private volatile int uploadSpeed;

    private final SpeedTestSocket downloadSocket;
    private final SpeedTestSocket uploadSocket;
    private final ExecutorService executor;
    private CountDownLatch latch;
    private final NetworkService networkService;

    public SpeedTestTask(NetworkService networkService) {
        this.networkService = networkService;
        executor = ThreadUtils.getDaemonExecutorService(1);
        uploadSocket = new SpeedTestSocket();
        downloadSocket = new SpeedTestSocket();
        downloadSocket.addSpeedTestListener(new SpeedTestListener(SpeedType.DOWNLOAD));
        uploadSocket.addSpeedTestListener(new SpeedTestListener(SpeedType.UPLOAD));
    }

    @Override
    public void run() {
        //TODO убрать нахуй эти костыли с latch
        while (networkService.isMonitoring()) {
            try {
                latch = new CountDownLatch(1);
                uploadSocket.startUpload("http://ipv4.ikoula.testdebit.info/", 10_000_000);
                latch.await();
                latch = new CountDownLatch(1);
                downloadSocket.startDownload("https://scaleway.testdebit.info:443/100M/100M.zip");
                latch.await();
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Слушатель, который в зависимости от {@code type} обновляет {@link SpeedTestTask#downloadSpeed} или {@link SpeedTestTask#uploadSpeed
     */
    private class SpeedTestListener implements ISpeedTestListener {
        /**
         * Загрузка или закачка
         */
        private final SpeedType type;

        public SpeedTestListener(SpeedType type) {
            this.type = type;
        }

        @Override
        public void onCompletion(SpeedTestReport report) {
            latch.countDown();
        }

        @Override
        public void onError(SpeedTestError speedTestError, String errorMessage) {
            logger.warn(String.format("Exception during upload speed test. Exception type %s. Message: %s", speedTestError, errorMessage));
            ThreadUtils.shutdownExecutor(executor);
        }

        @Override
        public void onProgress(float percent, SpeedTestReport report) {
            switch (type) {
                case DOWNLOAD:
                    downloadSpeed = report.getTransferRateBit().divide(BigDecimal.valueOf(100_000)).intValue();
                case UPLOAD:
                    uploadSpeed = report.getTransferRateBit().divide(BigDecimal.valueOf(100_000)).intValue();
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private enum SpeedType {
        DOWNLOAD,
        UPLOAD
    }

    /**
     * Остановить мониторинг
     */
    public void stopMonitoring() {
        uploadSpeed = 0;
        downloadSpeed = 0;
        downloadSocket.forceStopTask();
        uploadSocket.forceStopTask();
        ThreadUtils.shutdownExecutor(executor);
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public int getUploadSpeed() {
        return uploadSpeed;
    }
}