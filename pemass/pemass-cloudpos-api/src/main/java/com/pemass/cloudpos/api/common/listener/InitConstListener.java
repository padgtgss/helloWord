package com.pemass.cloudpos.api.common.listener;

import com.pemass.common.core.constant.ConfigurationConst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

/**
 * Description: 初始化配置文件
 * Author: estn.zuo
 * CreateTime: 2014-07-29 21:23
 */
public class InitConstListener implements ServletContextListener {
    private Log logger = LogFactory.getLog(InitConstListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (logger.isDebugEnabled()) {
            logger.debug("load properties file");
        }
        Properties prop = new Properties();
        try {
            prop.load(InitConstListener.class.getClassLoader().getResourceAsStream("pemass-cloudpos-api.properties"));
            ConfigurationConst.RESOURCE_ROOT_PATH = prop.getProperty("RESOURCE_ROOT_PATH");
            ConfigurationConst.RESOURCE_ROOT_URL = prop.getProperty("RESOURCE_ROOT_URL");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
