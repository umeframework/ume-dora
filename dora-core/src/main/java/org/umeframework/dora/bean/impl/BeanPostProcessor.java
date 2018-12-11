package org.umeframework.dora.bean.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.umeframework.dora.bean.BeanFactory;
import org.umeframework.dora.contant.BeanConfigConst;
import org.umeframework.dora.service.BaseComponent;

/**
 * BeanPostProcessor<br>
 * 
 * Deprecated from 3.0.1<br>
 * 
 * @author Yue MA
 * 
 */
@Deprecated
public class BeanPostProcessor implements org.springframework.beans.factory.config.BeanPostProcessor, java.io.Serializable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 8971524583156524362L;

	/**
	 * beanFactory
	 */
	@Resource(name = BeanConfigConst.DEFAULT_BEAN_FACTORY)
	private BeanFactory beanFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#
	 * postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof BaseComponent) {
			try {
				AutoInjectUtil.doAutoInject(bean, bean.getClass(), (BeanFactoryImpl) beanFactory);
			} catch (Exception e) {
				throw new BeanInitializationException("Error during post initialized bean " + beanName, e);
			}
		}
		return bean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#
	 * postProcessBeforeInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
