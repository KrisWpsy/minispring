package com.sg.gate.minispringstep02.aop.framework.adapter;

import com.sg.gate.minispringstep02.aop.BeforeAdvice;
import com.sg.gate.minispringstep02.aop.MethodBeforeAdvice;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 *  MethodBeforeAdviceInterceptor 实现了 MethodInterceptor 接口，在 invoke 方
 * 法中调用 advice 中的 before 方法，传入对应的参数信息。
 *  而这个 advice.before 则是用于自己实现 MethodBeforeAdvice 接口后做的相应处
 * 理。
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor {

    private MethodBeforeAdvice advice;

    public MethodBeforeAdviceInterceptor(){}

    public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
        this.advice = advice;
    }


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        return methodInvocation.proceed();
    }
}
