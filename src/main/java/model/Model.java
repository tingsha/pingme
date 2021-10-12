package main.java.model;

import main.java.view.utils.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    private static final Logger logger = LoggerFactory.getLogger(Model.class);
    private String domain;
    private final Executor executor = Executors.newFixedThreadPool(2);
    private Thread pingThread;
    private Thread speedThread;

    public void setDomain(String domain){
        this.domain = domain;
    }

    public String getDomain() {
        if (domain == null || domain.equals("")){
            Properties properties = PropertiesHelper.loadProperties();
            domain = properties.getProperty("domain");
        }
        return domain;
    }

    public void speedTest(){
        speedThread = new SpeedTestTask();
        executor.execute(speedThread);
    }

    public void pingTest(){
        pingThread = new PingTask(domain);
        executor.execute(pingThread);
    }

    public void stopPingTask(){
        PingTask.pingProcess.destroy();
        pingThread.interrupt();
    }
}
