package main.java.model;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.utils.SpeedTestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpeedTestTask extends Thread {
    private static volatile int downloadSpeed = 0;
    private static volatile int uploadSpeed = 0;
    private SpeedTestSocket uploadSocket;
    private SpeedTestSocket downloadSocket;
    private final Executor executor = Executors.newFixedThreadPool(2);

    @Override
    public void run() {
        setDaemon(true);
        synchronized (this) {
            setupTestListener();
            executor.execute(new UploadTask());
            executor.execute(new DownloadTask());
        }
    }

    private void setupTestListener() {
        uploadSocket = new SpeedTestSocket();
        downloadSocket = new SpeedTestSocket();

        // add a listener to wait for speedtest completion and progress
        uploadSocket.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                //System.out.println("[COMPLETED] upload rate in Mb/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {

            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
//                called to notify download/upload progress
//                System.out.println("[PROGRESS] upload progress : " + percent + "%");
//                System.out.println("[PROGRESS] upload rate in Mb/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
                uploadSpeed = report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)).intValue();
            }
        });
        downloadSocket.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                //System.out.println("[COMPLETED] download rate in Mb/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {

            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
//                System.out.println("[PROGRESS] download progress : " + percent + "%");
//                System.out.println("[PROGRESS] download rate in Mb/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
                downloadSpeed = report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)).intValue();
            }
        });
    }

    public static String getDownloadSpeed() {
        return String.valueOf(downloadSpeed);
    }

    public static String getUploadSpeed() {
        return String.valueOf(uploadSpeed);
    }

    private class UploadTask implements Runnable {

        @Override
        public void run() {
            uploadSocket.startUploadRepeat("http://ipv4.ikoula.testdebit.info/", 3_600_000,
                    2000, 10_000_000, new
                            IRepeatListener() {
                                @Override
                                public void onCompletion(final SpeedTestReport report) {
                                    // called when repeat task is finished
                                }

                                @Override
                                public void onReport(final SpeedTestReport report) {
                                    // called when an upload report is dispatched
                                }
                            });
        }
    }

    private class DownloadTask implements Runnable {

        @Override
        public void run() {
            downloadSocket.startDownloadRepeat("http://speedtest.tele2.net/1GB.zip",
                    3_600_000, 2000, new
                            IRepeatListener() {
                                @Override
                                public void onCompletion(final SpeedTestReport report) {
                                    // called when repeat task is finished
                                }

                                @Override
                                public void onReport(final SpeedTestReport report) {
                                    // called when a download report is dispatched
                                }
                            });
        }
    }
}
