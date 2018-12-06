package org.demon.pool;


import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sean on 2018/9/21.
 */
public class NamedThreadFactory implements ThreadFactory {

    private final String baseName;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    NamedThreadFactory(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(baseName + threadNum.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }

}