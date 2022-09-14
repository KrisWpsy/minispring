package com.sg.gate.minispringstep02.beans.factory;

public interface InitializingBean {

    /**
     * Bean 处理了属性填充后调用
     *
     * @throws Exception
     */
    void afterPropertiesSet();
}
