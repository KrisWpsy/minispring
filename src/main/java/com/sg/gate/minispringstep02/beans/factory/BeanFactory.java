package com.sg.gate.minispringstep02.beans.factory;

public interface BeanFactory {

//    获取无参类型的bean
    Object getBean(String name);
//    获取有参类型的bean
    Object getBean(String name, Object... args);

//    增加了按照类型获取 Bean 的方法
    <T> T getBean(String name, Class<T> requiredType);

    <T> T getBean(Class<T> requiredType);

}
