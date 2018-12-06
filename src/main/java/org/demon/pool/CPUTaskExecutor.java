package org.demon.pool;

import lombok.extern.apachecommons.CommonsLog;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


/**
 * 线程池工具类
 *
 * @author Sean
 */
@CommonsLog
public final class CPUTaskExecutor {
    private static final int THREADS = 10;
    private static final int SINGLE = 1;
    private static final CPUTaskExecutor instance = newInstance();
    private final static ExecutorService EXECUTOR_CPU = new ThreadPoolExecutor(THREADS, THREADS, 60L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("cpu-task"));

    private final ExecutorService uniqueExecutor = new ThreadPoolExecutor(SINGLE, SINGLE, 60L,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("cpu-single"));

    private CPUTaskExecutor() {
    }

    /**
     * 获取实例
     */
    public static CPUTaskExecutor getInstance() {
        return instance;
    }

    /**
     * 新建线程池,如非必要使用 {@link #getInstance()}
     */
    private static CPUTaskExecutor newInstance() {
        return new CPUTaskExecutor();
    }

    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        try {
            EXECUTOR_CPU.execute(task);
        } catch (Exception e) {
            log.error(e);
        }
    }


    /**
     * 执行异步任务
     */
    public <U> CompletableFuture<U> execute(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier, EXECUTOR_CPU);
    }

    /**
     * 多任务执行
     */
    public CompletableFuture[] execute(Runnable[] tasks) {
        CompletableFuture[] result = new CompletableFuture[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            result[i] = CompletableFuture.runAsync(tasks[i], EXECUTOR_CPU);
        }
        return result;
    }

    /**
     * 多任务执行
     */
    public CompletableFuture[] execute(Collection<Runnable> tasks) {
        CompletableFuture[] result = new CompletableFuture[tasks.size()];
        int i = 0;
        for (Runnable task : tasks) {
            result[i++] = CompletableFuture.runAsync(task, EXECUTOR_CPU);
        }
        return result;
    }

    /**
     * 顺序执行
     */
    public void executeOneByOne(Runnable task) {
        try {
            uniqueExecutor.execute(task);
        } catch (Exception e) {
            log.error(e);
        }
    }

}
