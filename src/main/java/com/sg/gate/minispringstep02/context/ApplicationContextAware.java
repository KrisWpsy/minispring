package com.sg.gate.minispringstep02.context;

import com.sg.gate.minispringstep02.beans.factory.Aware;

/**
 *  实现此接口，既能感知到所属的 ApplicationContext
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}
