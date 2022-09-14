package com.sg.gate.minispringstep02.context.event;

import com.sg.gate.minispringstep02.context.ApplicationEvent;
import com.sg.gate.minispringstep02.context.ApplicationListener;

/**
 *  在事件广播器中定义了添加监听和删除监听的方法以及一个广播事件的方法
 * multicastEvent 最终推送时间消息也会经过这个接口方法来处理谁该接收事
 * 件。
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event);
}
