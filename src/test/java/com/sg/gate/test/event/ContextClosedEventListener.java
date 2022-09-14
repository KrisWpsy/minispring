package com.sg.gate.test.event;

import com.sg.gate.minispringstep02.context.ApplicationListener;
import com.sg.gate.minispringstep02.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("关闭事件：" + this.getClass().getName());
    }
}
