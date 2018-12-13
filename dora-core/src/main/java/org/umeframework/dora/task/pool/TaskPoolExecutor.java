package org.umeframework.dora.task.pool;

import org.umeframework.dora.task.TaskRunner;
import org.umeframework.dora.task.TaskThread;

/**
 * TaskPoolExecutor<br>
 * 
 * @author Yue MA
 */
public interface TaskPoolExecutor {
    /**
     * init<br>
     */
    void init();

    /**
     * close<br>
     */
    void close();

    /**
     * runAsync<br>
     * 
     * @param taskThread
     */
    <E> void runAsync(TaskThread<E> taskThread);

    /**
     * runAsync<br>
     * 
     * @param taskRunner
     * @param taskParam
     */
    <E> void runAsync(TaskRunner<E> taskRunner, E taskParam);

    /**
     * run<br>
     * 
     * @param taskRunner
     * @param taskParam
     */
    <E> void run(TaskRunner<E> taskRunner, E taskParam);

    /**
     * run<br>
     * 
     * @param taskThread
     */
    <E> void run(TaskThread<E> taskThread);

}
