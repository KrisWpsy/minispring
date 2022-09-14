package com.sg.gate.minispringstep02.beans.factory.config;


//这个类定义了一个获取单例对象的接口
public interface SingletonBeanRegistry {


    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);

}
