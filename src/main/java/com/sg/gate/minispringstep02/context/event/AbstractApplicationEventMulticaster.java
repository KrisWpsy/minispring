package com.sg.gate.minispringstep02.context.event;

import com.sg.gate.minispringstep02.beans.factory.BeanFactory;
import com.sg.gate.minispringstep02.beans.factory.BeanFactoryAware;
import com.sg.gate.minispringstep02.context.ApplicationEvent;
import com.sg.gate.minispringstep02.context.ApplicationListener;
import com.sg.gate.minispringstep02.utils.ClassUtils;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *  AbstractApplicationEventMulticaster 是对事件广播器的公用方法提取，在这个类中
 * 可以实现一些基本功能，避免所有直接实现接口放还需要处理细节。
 *  除了像 addApplicationListener、removeApplicationListener，这样的通用方法，这
 * 里这个类中主要是对 getApplicationListeners 和 supportsEvent 的处理。
 *  getApplicationListeners 方法主要是摘取符合广播事件中的监听处理器，具体过滤
 * 动作在 supportsEvent 方法中。
 *  在 supportsEvent 方法中，主要包括对 Cglib、Simple 不同实例化需要获取目标
 * Class，Cglib 代理类需要获取父类的 Class，普通实例化的不需要。接下来就是通过
 * 提取接口和对应的 ParameterizedType 和 eventClassName，方便最后确认是否为
 * 子类和父类的关系，以此证明此事件归这个符合的类处理。
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> allListeners = new LinkedList<ApplicationListener>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    protected boolean supportEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();

        // 按照 CglibSubclassingInstantiationStrategy、SimpleInstantiationStrategy 不同的实例化类型，需要判断后获取目标 class
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String className = actualTypeArgument.getTypeName();
        Class<?> eventClassName;
        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ClassCastException();
        }

//        判定此eventClassName对象所表示的类或接口与指定的event.getClass()参数所表示的类或接口是否相同，或是否是其超类或接口
//        isAssignableFrom是用来判断子类和父类的关系的，或者接口的实现类和接口的关系的，默认所有的类的终极父类都是Object。如果A.isAssignableFrom(B)结果是true，证明B可以转换成为A,也就是A可以由B转换而来。
        return eventClassName.isAssignableFrom(event.getClass());
    }
}
