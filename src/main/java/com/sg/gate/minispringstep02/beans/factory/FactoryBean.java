package com.sg.gate.minispringstep02.beans.factory;

public interface FactoryBean<T> {

    T getObject();

    Class<?> getObjectType();

    boolean isSingleton();
}
