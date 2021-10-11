package main.java.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingTask extends Thread{
    private volatile static String ping;
    private static final Logger logger = LoggerFactory.getLogger(PingTask.class);
    private String domain;
    public static Process pingProcess;

    public PingTask(String serverAddress) {
        this.domain = serverAddress;
    }

    @Override
    public void run() {
        setDaemon(true);
        synchronized (this){
            try {
                pingProcess = Runtime.getRuntime().exec("ping " + domain + " -t");
                BufferedReader bis = new BufferedReader(new InputStreamReader(pingProcess.getInputStream(), "cp866") );
                while (!isInterrupted()) {
                    //Ответ от 217.69.139.200: число байт=32 время=131мс TTL=50
                    //Reply from 94.100.180.200: bytes=32 time=79ms TTL=50
                    String response;
                    if ((response = bis.readLine()) == null)
                        continue;
                    if (!response.equals("") && !response.contains("=") && !response.contains("[")){
                        ping = "loss";
                        continue;
                    }
                    Pattern pattern = Pattern.compile("=\\d*");
                    Matcher matcher = pattern.matcher(response);
                    String s = bis.readLine();
                    try {
                        for (int i = 0; i < 2; i++) {
                            if (matcher.find())
                                ping = matcher.group().replaceAll("=", "");
                        }
                    } catch (IllegalStateException e){
                        logger.warn("Can't get ping from string " + ping);
                    }
                }
            } catch (IOException e) {
                logger.error("Can't execute ping command! " + e.getMessage());
            } finally {
                pingProcess.destroy();
            }
        }
    }

    public static String getPing() {
        return ping;
    }
}
