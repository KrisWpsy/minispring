package com.sg.gate.minispringstep02.aop;

import org.aopalliance.aop.Advice;

/**
 * Advisor 承担了 Pointcut 和 Advice 的组合，Pointcut 用于获取 JoinPoint，而
 * Advice 决定于 JoinPoint 执行什么操作。
 */
public interface Advisor {

    Advice getAdvice();
}
