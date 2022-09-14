package com.sg.gate.minispringstep02.context.support;

import com.sg.gate.minispringstep02.beans.factory.support.DefaultListableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.xml.XmlBeanDefinitionReader;

/**
 *  在 AbstractXmlApplicationContext 抽象类的 loadBeanDefinitions 方法实现中，使
 * 用 XmlBeanDefinitionReader 类，处理了关于 XML 文件配置信息的操作。
 *  同时这里又留下了一个抽象类方法，getConfigLocations()，此方法是为了从入口上
 * 下文类，拿到配置信息的地址描述。
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            xmlBeanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();
}
