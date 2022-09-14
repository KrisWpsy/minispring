package com.sg.gate.minispringstep02.context.support;

import com.sg.gate.minispringstep02.beans.factory.Aware;
import com.sg.gate.minispringstep02.beans.factory.config.BeanPostProcessor;
import com.sg.gate.minispringstep02.context.ApplicationContext;
import com.sg.gate.minispringstep02.context.ApplicationContextAware;

/**
 *  由于 ApplicationContext 的获取并不能直接在创建 Bean 时候就可以拿到，所以
 * 需要在 refresh 操作时，把 ApplicationContext 写入到一个包装的
 * BeanPostProcessor 中去，再由AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInitialization
 * 方法调用。
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
