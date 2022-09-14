package com.sg.gate.minispringstep02.beans.factory.support;

import com.sg.gate.minispringstep02.beans.factory.ConfigurableListableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  DefaultListableBeanFactory 在 Spring 源码中也是一个非常核心的类，在我们目前
 * 的实现中也是逐步贴近于源码，与源码类名保持一致。
 *
 *  DefaultListableBeanFactory 继承了 AbstractAutowireCapableBeanFactory 类，也
 * 就具备了接口 BeanFactory 和 AbstractBeanFactory 等一连串的功能实现。所以有
 * 时候你会看到一些类的强转，调用某些方法，也是因为你强转的类实现接口或继承
 * 了某些类。
 *
 *  除此之外这个类还实现了接口 BeanDefinitionRegistry 中的
 * registerBeanDefinition(String beanName, BeanDefinition beanDefinition) 方法，当
 * 然你还会看到一个 getBeanDefinition 的实现，这个方法我们文中提到过它是抽象
 * 类 AbstractBeanFactory 中定义的抽象方法。现在注册 Bean 定义与获取 Bean 定义就可以同时使用了。接口定义了注册，
 * 抽象类定义了获取，都集中在 DefaultListableBeanFactory 中的 beanDefinitionMap 里.
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            Class beanClass = beanDefinition.getBeanClass();
            if (type.isAssignableFrom(beanClass)) {
                result.put(beanName, (T) getBean(beanName));
            }
        });
        return result;
    }

    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getBeanClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (beanNames.size() == 1) {
            return getBean(beanNames.get(0), requiredType);
        }
        return null;
    }
}
