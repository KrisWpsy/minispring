package com.sg.gate.test.event;

import com.sg.gate.minispringstep02.context.ApplicationListener;
import com.sg.gate.minispringstep02.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("刷新事件：" + this.getClass().getName());
    }
}
