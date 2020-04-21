package org.umeframework.dora.task;

/**
 * TaskRunner<br>
 * 
 * @author Yue MA
 */
public interface TaskRunner<T> {
    /**
     * 获取任务名称<br>
     * 
     * @return
     */
    String getName();

    /**
     * 运行任务<br>
     * 
     * @param taskParam
     */
    void run(T taskParam) throws Throwable ;
}
