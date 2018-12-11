package org.umeframework.dora.task.pool;

import java.io.Serializable;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.umeframework.dora.context.RequestContext;
import org.umeframework.dora.dsm.DataSourceTransactionExecutor;
import org.umeframework.dora.log.Logger;

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
    private RequestContext<?> context;

    /**
     * TaskThread<br>
     * 
     * @param taskRunner
     * @param taskParam
     * @param transactionManager
     * @param transactionDefinition
     */
    public TaskThread(TaskRunner<T> taskRunner, T taskParam, PlatformTransactionManager transactionManager) {
        this.context = RequestContext.open();
        this.taskRunner = taskRunner;
        this.taskParam = taskParam;
        this.transactionManager = transactionManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        RequestContext.openFrom(this.context);
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
            long useTime = System.currentTimeMillis() - startTime;
            if (logger != null) {
                logger.debug(runnerClass.getSimpleName(), ",", "run", ",", useTime);
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
        String runnerName = taskRunner.getName();
        Class<?> runnerClass = taskRunner.getClass();
        if (logger != null) {
            logger.info("Start thread for " + runnerName);
        }

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
     * getRollback<br>
     * 
     * @param isTransactional
     * @param e
     * @return
     */
    protected boolean getRollback(Transactional isTransactional, Throwable e) {
        boolean isRollback = false;
        if (isTransactional != null) {
            if (!isRollback && isTransactional.rollbackFor() != null) {
                Class<? extends Throwable>[] range = isTransactional.rollbackFor();
                for (Class<? extends Throwable> i : range) {
                    if (i.isAssignableFrom(e.getClass())) {
                        isRollback = true;
                        break;
                    }
                }
            }
            if (!isRollback && isTransactional.rollbackForClassName() != null) {
                String[] range = isTransactional.rollbackForClassName();
                for (String i : range) {
                    if (i.equalsIgnoreCase(e.getClass().getSimpleName())) {
                        isRollback = true;
                        break;
                    }
                }
            }
            isRollback = true;

            if (isRollback && isTransactional.noRollbackFor() != null) {
                Class<? extends Throwable>[] range = isTransactional.noRollbackFor();
                for (Class<? extends Throwable> i : range) {
                    if (i.isAssignableFrom(e.getClass())) {
                        isRollback = false;
                        break;
                    }
                }
            }
            if (isRollback && isTransactional.noRollbackForClassName() != null) {
                String[] range = isTransactional.noRollbackForClassName();
                for (String i : range) {
                    if (i.equalsIgnoreCase(e.getClass().getSimpleName())) {
                        isRollback = false;
                        break;
                    }
                }
            }
        } else {
            isRollback = true;
        }
        return isRollback;
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
