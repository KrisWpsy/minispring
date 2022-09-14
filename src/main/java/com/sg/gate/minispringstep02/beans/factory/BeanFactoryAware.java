package com.sg.gate.minispringstep02.beans.factory;

/**
 *  实现此接口，既能感知到所属的 BeanFactory
 */
public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory beanFactory);
}
