package com.sg.gate.minispringstep02.aop.framework;

/**
 * AopProxy 是代理的抽象对象，它的实现主要是基于 JDK 的代理和 Cglib 代理。
 * 定义一个标准接口，用于获取代理类。因为具体实现代理的方式可以有 JDK 方
 * 式，也可以是 Cglib 方式，所以定义接口会更加方便管理实现类。
 */
public interface AopProxy {

    Object getProxy();
}
