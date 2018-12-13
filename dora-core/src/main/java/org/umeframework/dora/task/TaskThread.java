package org.umeframework.dora.task;

import java.io.Serializable;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.transaction.DataSourceTransactionExecutor;

/**
 * TaskThread<br>
 * 
 * @author Yue MA
 */
public class TaskThread<T> implements Runnable, Serializable {
    /**
     * serial version UID<br>
     */
    private static final long serialVersionUID = -8152915260201355576L;
    /**
     * logger<br>
     */
    private Logger logger;
    /**
     * taskParam<br>
     */
    private T taskParam;
    /**
     * taskRunner<br>
     */
    private TaskRunner<T> taskRunner;
    /**
     * PlatformTransactionManager
     */
    private PlatformTransactionManager transactionManager;
    /**
     * SessionContext<br>
     */
    private RequestContext context;

    /**
     * TaskThread<br>
     * 
     * @param taskRunner
     * @param taskParam
     * @param transactionManager
     * @param transactionDefinition
     */
    public TaskThread(TaskRunner<T> taskRunner, T taskParam, PlatformTransactionManager transactionManager, Logger logger) {
        this.context = RequestContext.getCurrentContext();
        this.taskRunner = taskRunner;
        this.taskParam = taskParam;
        this.transactionManager = transactionManager;
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        RequestContext.cloneFrom(this.context);
        long startTime = System.currentTimeMillis();
        String runnerName = taskRunner.getName();
        Class<?> runnerClass = taskRunner.getClass();
        try {
            if (logger != null) {
                logger.info("Start thread for " + runnerName);
            }
            execute();
        } catch (Throwable e) {
            if (logger != null) {
                logger.error("Error in thread " + runnerName + " cause:" + e);
                logger.error("Error parammeters:" + this.getTaskParam());
            }
        } finally {
            if (logger != null) {
                logger.debug(runnerClass.getSimpleName(), ",", "run", ",", System.currentTimeMillis() - startTime);
            }
            RequestContext.close();
        }
    }

    /**
     * execute
     * 
     * @throws Throwable
     */
    public void execute() throws Throwable {
        Class<?> runnerClass = taskRunner.getClass();
        T inParam = this.getTaskParam();
        if (transactionManager != null) {
            Transactional transactional = runnerClass.getAnnotation(Transactional.class);
            if (transactional != null) {
                new DataSourceTransactionExecutor(transactionManager).execute(transactional, new DataSourceTransactionExecutor.CallbackerWithoutReturn() {
                    @Override
                    public void callWithoutReturn() throws Throwable {
                        taskRunner.run(inParam);
                    }
                });
            } else {
                new DataSourceTransactionExecutor(transactionManager).execute(Propagation.REQUIRED, Exception.class, new DataSourceTransactionExecutor.CallbackerWithoutReturn() {
                    @Override
                    public void callWithoutReturn() throws Throwable {
                        taskRunner.run(inParam);
                    }
                });
            }
        } else {
            taskRunner.run(inParam);
        }
    }

    /**
     * @return the taskParam
     */
    public T getTaskParam() {
        return taskParam;
    }

    /**
     * @param taskParam
     *            the taskParam to set
     */
    public void setTaskParam(T taskParam) {
        this.taskParam = taskParam;
    }

}
