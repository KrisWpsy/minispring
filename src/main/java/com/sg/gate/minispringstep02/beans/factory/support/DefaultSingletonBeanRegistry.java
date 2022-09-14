package com.sg.gate.minispringstep02.beans.factory.support;

import com.sg.gate.minispringstep02.beans.factory.DisposableBean;
import com.sg.gate.minispringstep02.beans.factory.ObjectFactory;
import com.sg.gate.minispringstep02.beans.factory.config.SingletonBeanRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在 DefaultSingletonBeanRegistry 中主要实现 getSingleton 方法，同时实现了一个
 * 受保护的 addSingleton 方法，这个方法可以被继承此类的其他类调用。包括：
 * AbstractBeanFactory 以及继承的 DefaultListableBeanFactory 调用。
 *
 * ===========================================================================
 *
 *  在用于提供单例对象注册的操作的 DefaultSingletonBeanRegistry 类中，共有三个
 * 缓存对象的属性；singletonObjects、earlySingletonObjects、singletonFactories，
 * 如它们的名字一样，用于存放不同类型的对象（单例对象、早期的半成品单例对
 * 象、单例工厂对象）。
 *  紧接着在这三个缓存对象下提供了获取、添加和注册不同对象的方法，包括：
 * getSingleton、registerSingleton、addSingletonFactory，其实后面这两个方法都比
 * 较简单，主要是 getSingleton 的操作，它是在一层层处理不同时期的单例对象，
 * 直至拿到有效的对象。
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

//    一级缓存，普通对象（完全初始化好的）
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();


//    二级缓存，提前暴露对象，没有完全实例化的对象(没有填充属性)
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();


//    三级缓存，存放代理对象，用于解决循环依赖
    protected final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();


    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            singletonObject = earlySingletonObjects.get(beanName);
//            判断二级缓存中是否有对象，这个对象就是代理对象，因为只有代理对象才会放到三级缓存中
            if (singletonObject == null) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
//                    把三级缓存中的代理对象中的真实对象获取出来，放入二级缓存中
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }


    public void registerDisposableBean(String beenName, DisposableBean bean) {
        disposableBeans.put(beenName, bean);
    }


    public void destroySingletons()  {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
