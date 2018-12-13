package org.umeframework.dora.task.pool.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.transaction.PlatformTransactionManager;
import org.umeframework.dora.service.BaseComponent;
import org.umeframework.dora.task.TaskRunner;
import org.umeframework.dora.task.TaskThread;
import org.umeframework.dora.task.pool.TaskPoolExecutor;

/**
 * TaskPoolExecutorImpl<br>
 * 
 * @author Yue MA
 */
public class TaskPoolExecutorImpl extends BaseComponent implements TaskPoolExecutor {
    /**
     * transactionManager
     */
    private PlatformTransactionManager transactionManager;

    /**
     * threadPoolExecutor<br>
     */
    private ThreadPoolExecutor threadPoolExecutor;
    /**
     * name<be>
     */
    private String name;

    /**
     * corePoolSize<br>
     */
    private int corePoolSize = 5;
    /**
     * maximumPoolSize<br>
     */
    private int maximumPoolSize = 25;
    /**
     * keepAliveTime<br>
     */
    private long keepAliveTime = 60;
    /**
     * blockingQueueMaximumSize<br>
     */
    private int blockingQueueMaximumSize = 65536;
    /**
     * timeUnit<br>
     */
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    /**
     * init<br>
     */
    @Override
    synchronized public void init() {
        this.threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, new ArrayBlockingQueue<Runnable>(blockingQueueMaximumSize), new ThreadPoolExecutor.AbortPolicy());
        super.getLogger().info("Create TaskPoolExecutor:" + name + " PoolSize:", maximumPoolSize, " QueueSize:", blockingQueueMaximumSize);
    }

    /**
     * close<br>
     */
    @Override
    synchronized public void close() {
        threadPoolExecutor.shutdown();
        super.getLogger().info("Shutdown TaskPoolExecutor:" + name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fubao.base.task.pool.TaskPoolExecutor#execute(com.fubao.base.task.TaskThread)
     */
    @Override
    public <E> void runAsync(TaskThread<E> taskThread) {
        try {
            this.getThreadPoolExecutor().execute(taskThread);
        } catch (Throwable e) {
            super.getLogger().error("TaskPoolExecutor run error:", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fubao.base.task.pool.TaskPoolExecutor#execute(com.fubao.base.task.TaskRunner, com.fubao.base.task.TaskParam)
     */
    @Override
    public <E> void runAsync(TaskRunner<E> taskProc, E taskParam) {
        doValidation(taskProc, taskParam);
        TaskThread<E> task = new TaskThread<E>(taskProc, taskParam, transactionManager);
        runAsync(task);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fubao.base.task.pool.TaskPoolExecutor#doSync(com.fubao.base.task.TaskRunner, com.fubao.base.task.TaskParam)
     */
    @Override
    public <E> void run(TaskRunner<E> taskProc, E taskParam) {
        doValidation(taskProc, taskParam);
        TaskThread<E> task = new TaskThread<E>(taskProc, taskParam, transactionManager);
        run(task);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.fubao.base.task.pool.TaskPoolExecutor#doSync(com.fubao.base.task.TaskThread)
     */
    @Override
    public <E> void run(TaskThread<E> taskThread) {
        try {
            taskThread.run();
        } catch (Throwable e) {
            super.getLogger().error("TaskPoolExecutor run error:", e);
        }
    }

    /**
     * doValidation
     * 
     * @param taskProc
     * @param taskParam
     */
    protected <E> void doValidation(TaskRunner<E> taskProc, E taskParam) {
        String key = taskProc.getName();
        if (!taskProc.getClass().getName().equals(key)) {
            super.getLogger().warn("*** Inconsistent task name and class name:" + key + "<>" + taskProc.getClass().getName());
        }
    }

    /**
     * Setup ThreadPoolExecutor properties.<br>
     * 
     * @param threadFactory
     *            the threadFactory to set
     */
    public void setThreadFactory(ThreadFactory threadFactory) {
        this.getThreadPoolExecutor().setThreadFactory(threadFactory);
    }

    /**
     * Setup ThreadPoolExecutor properties.<br>
     * 
     * @param rejectedHandler
     *            the rejectedHandler to set
     */
    public void setRejectedHandler(RejectedExecutionHandler rejectedHandler) {
        this.getThreadPoolExecutor().setRejectedExecutionHandler(rejectedHandler);
    }

    /**
     * @return the threadPoolExecutor
     */
    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            init();
        }
        return threadPoolExecutor;
    }

    /**
     * @return the corePoolSize
     */
    public int getCorePoolSize() {
        return corePoolSize;
    }

    /**
     * @param corePoolSize
     *            the corePoolSize to set
     */
    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    /**
     * @return the maximumPoolSize
     */
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    /**
     * @param maximumPoolSize
     *            the maximumPoolSize to set
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    /**
     * @return the keepAliveTime
     */
    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    /**
     * @param keepAliveTime
     *            the keepAliveTime to set
     */
    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    /**
     * @return the blockingQueueMaximumSize
     */
    public int getBlockingQueueMaximumSize() {
        return blockingQueueMaximumSize;
    }

    /**
     * @param blockingQueueMaximumSize
     *            the blockingQueueMaximumSize to set
     */
    public void setBlockingQueueMaximumSize(int blockingQueueMaximumSize) {
        this.blockingQueueMaximumSize = blockingQueueMaximumSize;
    }

    /**
     * @return the timeUnit
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * @param timeUnit
     *            the timeUnit to set
     */
    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * @param transactionManager the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
