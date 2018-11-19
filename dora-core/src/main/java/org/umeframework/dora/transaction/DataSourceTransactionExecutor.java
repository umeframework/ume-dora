package org.umeframework.dora.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务执行模板类。<br>
 * 
 * @author MA YUE
 */
public class DataSourceTransactionExecutor {
    /**
     * 业务逻辑回调接口。<br>
     *
     */
    public interface Callbacker {
        /**
         * 业务逻辑回调方法。<br>
         * 
         * @throws Throwable
         */
        Object call() throws Throwable;
    }

    /**
     * 无返回值的事务执行回调接口类<br>
     */
    public static abstract class CallbackerWithoutReturn implements Callbacker {
        /**
         * 回调函数
         * 
         * @throws Throwable
         */
        public abstract void callWithoutReturn() throws Throwable;

        /*
         * (non-Javadoc)
         * 
         * @see com.fubao.base.jdbc.tx.TransactionExecutor.Callbacker#call()
         */
        @Override
        public Object call() throws Throwable {
            callWithoutReturn();
            return null;
        }
    }

    /**
     * 事务管理器<br>
     */
    private PlatformTransactionManager transactionManager;

    /**
     * 构造时指定使用的事务管理器<br>
     * 
     * @param transactionManager
     */
    public DataSourceTransactionExecutor(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * 业务逻辑执行模板<br>
     * 
     * @param param
     *            输入参数
     * @param transactional
     *            事务属性定义
     * @param callbacker
     *            回调接口
     * @return 执行结果
     * @throws Throwable
     */
    public <IN> Object execute(Transactional transactional, Callbacker callbacker) throws Throwable {
        boolean isRollback = false;
        TransactionStatus txStatus = null;
        Object result = null;
        try {
            if (this.transactionManager != null) {
                txStatus = getTransactionStatus(transactional);
            }
            result = callbacker.call();
        } catch (Throwable e) {
            if (this.transactionManager != null) {
                isRollback = getRollback(transactional, e);
            }
            throw e;
        } finally {
            if (this.transactionManager != null) {
                finishTransaction(txStatus, isRollback);
            }
        }
        return result;
    }

    /**
     * 获取事务属性类型<br>
     * 
     * @param transactional
     * @return
     * @throws SQLException
     */
    protected TransactionStatus getTransactionStatus(Transactional transactional) throws SQLException {
        int txPropagation = Propagation.REQUIRED.value();
        if (transactional != null) {
            // 如果TaskThread上设有transactionPropagation属性则优先使用自身设定，否则使用TaskRunner类上的@Transactional声明的事务属性
            txPropagation = transactional.propagation().value();
            if (transactional.isolation() != null && !Isolation.DEFAULT.equals(transactional.isolation()) && this.transactionManager instanceof DataSourceTransactionManager) {
                DataSourceTransactionManager dtm = (DataSourceTransactionManager) this.transactionManager;
                Connection conn = DataSourceUtils.getConnection(dtm.getDataSource());
                conn.setTransactionIsolation(transactional.isolation().value());
            }
        }
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition(txPropagation);
        return this.transactionManager.getTransaction(txDefinition);
    }

    /**
     * 检查事务是否需要撤销<br>
     * 
     * @param transactional
     * @param e
     * @return
     */
    protected boolean getRollback(Transactional transactional, Throwable e) {
        boolean isRollback = false;
        if (transactional != null) {
            if (!isRollback && transactional.rollbackFor() != null) {
                // 检查是否有回滚设置
                Class<? extends Throwable>[] range = transactional.rollbackFor();
                for (Class<? extends Throwable> i : range) {
                    if (i.isAssignableFrom(e.getClass())) {
                        isRollback = true;
                        break;
                    }
                }
            }
            if (!isRollback && transactional.rollbackForClassName() != null) {
                // 检查是否有回滚设置
                String[] range = transactional.rollbackForClassName();
                for (String i : range) {
                    if (i.equalsIgnoreCase(e.getClass().getSimpleName())) {
                        isRollback = true;
                        break;
                    }
                }
            }
            // 此处设为默认回滚
            isRollback = true;

            if (isRollback && transactional.noRollbackFor() != null) {
                // 检查是否有排除设置
                Class<? extends Throwable>[] range = transactional.noRollbackFor();
                for (Class<? extends Throwable> i : range) {
                    if (i.isAssignableFrom(e.getClass())) {
                        isRollback = false;
                        break;
                    }
                }
            }
            if (isRollback && transactional.noRollbackForClassName() != null) {
                // 检查是否有排除设置
                String[] range = transactional.noRollbackForClassName();
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
     * 提交或撤销事务<br>
     * 
     * @param txStatus
     * @param isRollback
     */
    protected void finishTransaction(TransactionStatus txStatus, boolean isRollback) {
        if (isRollback) {
            // 回滚事务
            this.transactionManager.rollback(txStatus);
        } else {
            this.transactionManager.commit(txStatus);
        }
    }

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
