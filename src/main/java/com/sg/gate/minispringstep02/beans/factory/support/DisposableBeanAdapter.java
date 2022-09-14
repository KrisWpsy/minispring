package com.sg.gate.minispringstep02.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.sg.gate.minispringstep02.beans.factory.DisposableBean;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final Object beanName;

    private String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        1、实现接口DisposableBean
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

//        2、配置信息destroy-method{为了避免执行二次销毁}
        if (StrUtil.isNotEmpty(destroyMethodName) && (!(bean instanceof DisposableBean))) {
            Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
            if (destroyMethod == null) {
                throw new RuntimeException();
            }
            destroyMethod.invoke(bean);
        }
    }

}
