package com.sg.gate.minispringstep02.context.event;

import com.sg.gate.minispringstep02.beans.factory.BeanFactory;
import com.sg.gate.minispringstep02.context.ApplicationEvent;
import com.sg.gate.minispringstep02.context.ApplicationListener;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster{

    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @Override
    public void multicastEvent(final ApplicationEvent event) {
        for (final ApplicationListener listener : getApplicationListeners(event)) {
            listener.onApplicationEvent(event);
        }
    }
}
