/*
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0
 */
package org.umeframework.dora.bean.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.umeframework.dora.bean.BeanFactory;
import org.umeframework.dora.bean.BeanSupport;

/**
 * Bean Factory 
 * 
 * @author Yue MA
 * 
 */
public class BeanFactoryImpl extends BeanSupport implements ApplicationContextAware, BeanFactory, java.io.Serializable {
    /**
     * serial version UID
	 */
	private static final long serialVersionUID = 8681672883192666731L;
    /**
     * applicationContext
     */
    private ApplicationContext applicationContext;
    
    /**
     * enable or disable dependency checking while create bean
     */
    private boolean dependencyCheck = false;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext
     * (org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.bean.BeanFactory#getBean(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <E> E getBean(
            String beanName) {
        return (E) applicationContext.getBean(beanName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.bean.BeanFactory#autowireCapableCreateBean(java.lang.Class)
     */
    @Override
    public Object autowireCapableCreateBean(
            Class<?> clazz) throws Exception {

        return autowireCapableCreateBean(clazz, dependencyCheck);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.umeframework.dora.bean.BeanFactory#autowireCapableCreateBean(java.lang.Class, boolean)
     */
    public Object autowireCapableCreateBean(
            Class<?> clazz,
            boolean check) throws Exception {

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        Object obj = autowireCapableBeanFactory.createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, check);
        
        // rev01: Change doAutoInject to postProcessAfterInitialization once implement BeanPostProcessor, but can not inject instance
        // rev02: AutoInjectUtil.doAutoInject(obj, obj.getClass(), this);
        // rev03: Deprecated "@AutoInject" annotation since DORA 3.0.1 RELEASE and use "@Resource" replace
        
        return obj;
    }

	/**
	 * @return the dependencyCheck
	 */
	public boolean isDependencyCheck() {
		return dependencyCheck;
	}

	/**
	 * @param dependencyCheck the dependencyCheck to set
	 */
	public void setDependencyCheck(boolean dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

}
