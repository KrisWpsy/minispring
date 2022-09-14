package com.sg.gate.minispringstep02.beans.factory.config;

/**
 * 接口提供了两个方法：postProcessBeforeInitialization 用于在
 * Bean 对象执行初始化方法之前，执行此方法、
 * postProcessAfterInitialization 用于在 Bean 对象执行初始化方法之
 * 后，执行此方法。
 *
 * BeanPostProcessor，因为它可以解决在 Bean 对象执行初始化方法之
 * 前，用于修改新实例化 Bean 对象的扩展点，所以我们也就可以处理自己的 AOP
 * 代理对象逻辑了。
 */
public interface BeanPostProcessor {

    /**
     * 在 Bean 对象执行初始化方法之前，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessBeforeInitialization(Object bean, String beanName);

    /**
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     */
    Object postProcessAfterInitialization(Object bean, String beanName);
}
