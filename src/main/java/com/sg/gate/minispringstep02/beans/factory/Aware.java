package com.sg.gate.minispringstep02.beans.factory;

/**
 * 标记类接口，实现该接口可以被 Spring 容器感知
 *
 *  Aware 有四个继承的接口，其他这些接口的继承都是为了继承一个标记，有了标
 * 记的存在更方便类的操作和具体判断实现。
 *  另外由于 ApplicationContext 并不是在 AbstractAutowireCapableBeanFactory 中
 * createBean 方法下的内容，所以需要像容器中注册
 * addBeanPostProcessor ，再由 createBean 统一调用
 * applyBeanPostProcessorsBeforeInitialization 时进行操作。
 */
public interface Aware {
}
