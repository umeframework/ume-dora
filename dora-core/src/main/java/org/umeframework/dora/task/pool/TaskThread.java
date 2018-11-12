package org.umeframework.dora.task.pool;

import java.io.Serializable;
import java.sql.Connection;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.umeframework.dora.context.SessionContext;
import org.umeframework.dora.log.Logger;
import org.umeframework.dora.transaction.TransactionManager;

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
     * transactionManager
     */
    private TransactionManager transactionManager;
    /**
     * SessionContext<br>
     */
    private SessionContext context;

    /**
     * TaskThread<br>
     * 
     * @param taskRunner
     * @param taskParam
     * @param transactionManager
     * @param transactionDefinition
     */
    public TaskThread(TaskRunner<T> taskRunner, T taskParam, TransactionManager transactionManager) {
        this.context = SessionContext.open();
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
        long startTime = System.currentTimeMillis();
        String runnerName = taskRunner.getName();
        Class<?> runnerClass = taskRunner.getClass();
        if (logger != null) {
            logger.info("Start thread for " + runnerName);
        }

        SessionContext currentRequestContext = SessionContext.open();
        currentRequestContext.inheritFrom(this.context);

        Transactional isTransactional = runnerClass.getAnnotation(Transactional.class);
        boolean isRollback = false;
        try {
            if (this.transactionManager != null) {
                int txPropagation = Propagation.REQUIRES_NEW.value();
                if (isTransactional != null) {
                    txPropagation = isTransactional.propagation().value();
                    if (isTransactional.isolation() != null && !Isolation.DEFAULT.equals(isTransactional.isolation()) && this.transactionManager instanceof DataSourceTransactionManager) {
                        DataSourceTransactionManager dtm = (DataSourceTransactionManager) this.transactionManager;
                        Connection conn = DataSourceUtils.getConnection(dtm.getDataSource());
                        conn.setTransactionIsolation(isTransactional.isolation().value());
                    }
                }
                this.transactionManager.begin(txPropagation);
            }
            taskRunner.run(this.getTaskParam());
            if (logger != null) {
                logger.info("End thread for " + runnerName);
            }
        } catch (Throwable e) {
            if (logger != null) {
                logger.error("Error in thread " + runnerName + " cause:" + e);
                logger.error("Error parammeters:" + this.getTaskParam());
            }
            if (this.transactionManager != null) {
                isRollback = getRollback(isTransactional, e);
            }
        } finally {
            if (this.transactionManager != null) {
                finalTransaction(isRollback);
            }
            long useTime = System.currentTimeMillis() - startTime;
            if (logger != null) {
                logger.debug(runnerClass.getSimpleName(), ",", "run", ",", useTime);
            }
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
     * finalTransaction<br>
     * 
     * @param txStatus
     * @param isRollback
     */
    protected void finalTransaction(boolean isRollback) {
        if (isRollback) {
            this.transactionManager.rollback();
        } else {
            this.transactionManager.commit();
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
