package com.sg.gate.test.bean;

import com.sg.gate.minispringstep02.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class UserServiceBeforeAdvice implements MethodBeforeAdvice {


    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("拦截方法：" + method.getName());
    }
}
