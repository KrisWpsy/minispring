package com.sg.gate.minispringstep02.beans.factory.config;


import com.sg.gate.minispringstep02.beans.PropertyValues;

/**
 *
 * 本章节用到的 BeanPostProcessor，因为它可以解决在 Bean 对象执行初始化方法之
 * 前，用于修改新实例化 Bean 对象的扩展点，所以我们也就可以处理自己的 AOP
 * 代理对象逻辑了。
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor{

//    在Bean对象实例化之前，执行此方法
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName);

//    在Bean对象实例化之后，执行此方法
    boolean postProcessAfterInstantiation(Object bean, String beanName);

//    在Bean对象实例化完成后，设置属性操作之前执行此方法
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName);

    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
