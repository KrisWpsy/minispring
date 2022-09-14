package com.sg.gate.minispringstep02.beans.factory.config;

import com.sg.gate.minispringstep02.beans.factory.ConfigurableListableBeanFactory;

/**
 * 这个接口是满足于在所有的 BeanDefinition 加载完成后，
 * 实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制。
 */
public interface BeanFactoryPostProcessor {

    /**
     * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修
     改 BeanDefinition 属性的机制
     *
     * @param beanFactory
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);
}
