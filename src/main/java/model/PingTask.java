package main.java.model;

import main.java.service.NetworkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Задача для мониторинга задержки сети
 */
public class PingTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(PingTask.class);
    private volatile String ping;
    private final String serverAddress;
    private Process pingProcess;
    private final NetworkService networkService;

    public PingTask(String serverAddress, NetworkService networkService) {
        this.networkService = networkService;
        this.serverAddress = serverAddress;
    }

    @Override
    public void run() {
        createPingProcess();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(pingProcess.getInputStream(), "cp866"))) {
            while (networkService.isMonitoring()) {
                String response = reader.readLine();
                if (response == null || !response.contains("=")) {
                    ping = "loss";
                    continue;
                }
                //Ответ от 217.69.139.200: число байт=32 время=131мс TTL=50
                ping = response.split(" ")[5].replaceAll("\\D", "");
            }
        } catch (Exception e) {
            logger.error("Exception during pinging: {0} ", e);
            throw new RuntimeException(e);
        } finally {
            pingProcess.destroy();
        }
    }

    /**
     * Создать процесс для получения задержки сети
     */
    private void createPingProcess() {
        try {
            pingProcess = Runtime.getRuntime().exec("ping " + serverAddress + " -t");
        } catch (IOException e) {
            logger.error("Exception during creating ping process " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Остановить мониторинг
     */
    public void stopMonitoring() {
        pingProcess.destroy();
    }

    public String getPing() {
        return ping;
    }
}
