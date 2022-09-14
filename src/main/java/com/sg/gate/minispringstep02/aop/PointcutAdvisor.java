package com.sg.gate.minispringstep02.aop;

public interface PointcutAdvisor extends Advisor{


    Pointcut getPointcut();
}
