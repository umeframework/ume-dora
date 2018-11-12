/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean;

/**
 * BeanFactory
 * 
 * @author Yue MA
 */
public interface BeanFactory {

    /**
     * getBean
     * 
     * @param beanName
     * @return
     */
    <E> E getBean(
            String beanName);

    /**
     * autowireCapableCreateBean
     * 
     * @param clazz
     * @return
     */
    Object autowireCapableCreateBean(
            Class<?> clazz) throws Exception;
    
    
    /**
     * autowireCapableCreateBean
     * 
     * @param clazz
     * @param check
     * @return
     * @throws Exception
     */
    Object autowireCapableCreateBean(
            Class<?> clazz,
            boolean check) throws Exception;

}
