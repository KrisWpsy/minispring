package com.sg.gate.minispringstep02.context;

/**
 *  ApplicationEventPublisher 是整个一个事件的发布接口，所有的事件都需要从这个
 * 接口发布出去。
 */
public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);
}
