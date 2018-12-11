/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Default configuration for DoraFramework.<br>
 * 
 * @author Yue Ma
 */
@Configuration
@Import({
          DefaultBeanFactoryConfiguration.class,
          DefaultDaoConfiguration.class,
          DefaultDataSourceConfiguration.class,
          DefaultExceptionHandlerConfiguration.class,
          DefaultHttpProxyConfiguration.class,
          DefaultLogConfiguration.class,
          DefaultMessageConfiguration.class,
          DefaultServiceAjaxConfiguration.class,
          DefaultServiceRunnerConfiguration.class,
          DefaultServiceMappingConfiguration.class,
          DefaultSystemPropertyConfiguration.class,
          DefaultTransactionManagerConfiguration.class,
          DefaultUserCacheConfiguration.class,
          DefaultUserLoginConfiguration.class,
          DefaultWebControllerConfiguration.class 
          })
public class AutoConfiguration {
}
