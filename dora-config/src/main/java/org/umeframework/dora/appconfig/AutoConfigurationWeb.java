/* 
 * Copyright 2014-2017 UME Framework Group, Apache License Version 2.0 
 */
package org.umeframework.dora.appconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Default basic configuration for DoraFramework.<br>
 * 
 * @author Yue Ma
 */
@Configuration
@Import({
          DefaultWebControllerConfiguration.class 
          })
public class AutoConfigurationWeb {
}
