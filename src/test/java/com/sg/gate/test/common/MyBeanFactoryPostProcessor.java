package com.sg.gate.test.common;

import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.PropertyValues;
import com.sg.gate.minispringstep02.beans.factory.ConfigurableListableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.BeanFactoryPostProcessor;
import com.sg.gate.test.bean.UserService;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("company", "改为： 字节跳动"));
    }
}
