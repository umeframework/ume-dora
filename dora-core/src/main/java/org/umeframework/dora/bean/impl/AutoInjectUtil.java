package org.umeframework.dora.bean.impl;

import java.lang.reflect.Field;

import org.umeframework.dora.bean.AutoInject;
import org.umeframework.dora.exception.SystemException;
import org.umeframework.dora.util.StringUtil;

/**
 * AutoInjectUtil
 * 
 * @author Yue MA
 * 
 */
@Deprecated
public abstract class AutoInjectUtil {
	/**
	 * default auto inject element package post fix
	 */
	private static final String DEFAULT_AUTO_INJECT_PACKAGE_POSTFIX = ".impl.";
	/**
	 * default auto inject element class post fix
	 */
	private static final String DEFAULT_AUTO_INJECT_CLASS_POSTFIX = "Impl";

	/**
	 * inject
	 * 
	 * @param instance
	 * @param clazz
	 * @throws Exception
	 */
	synchronized public static void doAutoInject(Object instance, Class<?> clazz, BeanFactoryImpl beanFactory) throws Exception {
		// get super class type
		Class<?> superClazz = clazz.getSuperclass();

		if (!superClazz.equals(org.umeframework.dora.service.BaseComponent.class)) {
			// if (!superClazz.equals(org.umeframework.dora.service.CoreComponent.class) &&
			// beanFactory.isBeanType(superClazz)) {
			// do inject for super class firstly if super class is in scope
			doAutoInject(instance, superClazz, beanFactory);
		}

		// get internal filed list
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			// get AutoInject annotation
			AutoInject anno = field.getAnnotation(AutoInject.class);
			if (anno != null) {
				Object implObj = null;
				String beanName = anno.name();

				if (StringUtil.isNotEmpty(beanName)) {
					// inject by name
					implObj = beanFactory.getBean(beanName);
				} else {
					try {
						// try inject by field name
						beanName = field.getName();
						implObj = beanFactory.getBean(beanName);
					} catch (Exception e) {
						// ignore error for this case
					}
					if (implObj == null) {
						// inject by type
						Class<?> fieldClazz = field.getType();
						Class<?> tarClazzClass = fieldClazz;
						if (fieldClazz.isInterface()) {
							String tarClazzName = fieldClazz.getSimpleName() + DEFAULT_AUTO_INJECT_CLASS_POSTFIX;
							tarClazzName = fieldClazz.getPackage().getName() + DEFAULT_AUTO_INJECT_PACKAGE_POSTFIX + tarClazzName;
							try {
								tarClazzClass = clazz.getClassLoader().loadClass(tarClazzName);
							} catch (Exception e) {
								throw new SystemException(e, "Failed in load class "
								        + tarClazzName
								        + " during auto inject field "
								        + clazz.getName()
								        + "."
								        + field.getName());
							}
						}
						// create instance by type
						implObj = beanFactory.autowireCapableCreateBean(tarClazzClass);
					}
				}
				// set field value
				if (!field.isAccessible()) {
					field.setAccessible(true);
					field.set(instance, implObj);
					field.setAccessible(false);
				} else {
					field.set(instance, implObj);
				}
			}
		}
	}
}
