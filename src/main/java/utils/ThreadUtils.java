package main.java.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Утилита для работы с потоками
 */
public class ThreadUtils {

    /**
     * Получить {@link ExecutorService}, который завершается при выходе из программы
     *
     * @param threadsCount количество одновременно выполняемых потоков
     */
    public static ExecutorService getDaemonExecutorService(int threadsCount) {
        return Executors.newFixedThreadPool(threadsCount, r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * Безопасно завершить {@link ExecutorService}
     */
    public static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
