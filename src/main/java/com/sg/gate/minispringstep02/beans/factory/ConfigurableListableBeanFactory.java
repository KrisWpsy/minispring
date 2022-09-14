package com.sg.gate.minispringstep02.beans.factory;

import com.sg.gate.minispringstep02.beans.factory.config.AutowireCapableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.BeanPostProcessor;
import com.sg.gate.minispringstep02.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory, ListableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void preInstantiateSingletons();

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
