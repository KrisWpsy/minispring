package com.sg.gate.test.common;

import com.sg.gate.minispringstep02.beans.factory.config.BeanPostProcessor;
import com.sg.gate.test.bean.UserService;

public class MyBeanPostProcessor /*implements BeanPostProcessor*/ {

   /* @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if ("userService".equals(beanName)) {
            UserService userService = (UserService) bean;
            userService.setLocation("改为： 上海;");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }*/
}
