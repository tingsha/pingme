package main.java.model;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpeedTestTask extends Thread{
    private static BigDecimal downloadSpeed;
    private static volatile BigDecimal uploadSpeed;
    private SpeedTestSocket speedTestSocket;

    @Override
    public void run() {
        setDaemon(true);
        synchronized (this) {
            setupTestListener();
            setDownloadTask();
            //setUploadTask();
        }
    }

    private void setUploadTask(){
        speedTestSocket.startUploadRepeat(" http://speedtest.tele2.net/upload.php", 1000000,
                20000, 2000, new
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

    private void setDownloadTask(){
        speedTestSocket.startDownloadRepeat("http://speedtest.tele2.net/1GB.zip",
                20000, 2000, new
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

    private void setupTestListener(){
        speedTestSocket = new SpeedTestSocket();

        // add a listener to wait for speedtest completion and progress
        speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

            @Override
            public void onCompletion(SpeedTestReport report) {
                // called when download/upload is complete
                System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
            }

            @Override
            public void onError(SpeedTestError speedTestError, String errorMessage) {

            }

            @Override
            public void onProgress(float percent, SpeedTestReport report) {
                // called to notify download/upload progress
                System.out.println("[PROGRESS] progress : " + percent + "%");
                System.out.println("[PROGRESS] rate in Mb/s   : " + report.getTransferRateBit().divide(BigDecimal.valueOf(1_000_000)));
            }
        });
    }

    public static BigDecimal getDownloadSpeed() {
        return downloadSpeed;
    }

    public static BigDecimal getUploadSpeed() {
        return uploadSpeed;
    }
}
