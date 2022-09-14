package com.sg.gate.minispringstep02.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.sg.gate.minispringstep02.beans.annotation.AutowiredAnnotationBeanPostProcessor;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.support.BeanDefinitionRegistry;
import com.sg.gate.minispringstep02.stereotype.Component;

import java.util.Set;

/**
 * ClassPathBeanDefinitionScanner 是继承自
 * ClassPathScanningCandidateComponentProvider 的具体扫描包处理的类，在
 * doScan 中除了获取到扫描的类信息以后，还需要获取 Bean 的作用域和类名，如
 * 果不配置类名基本都是把首字母缩写。
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String...basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : candidates) {
//                解析Bean的作用域singleton、prototype
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }
                registry.registryBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }

//        注册处理注解的BeanPostProcessor(@Autowired、@Value)
        registry.registryBeanDefinition("com.sg.gate.minispringstep02.contextan.annotation.internalAutowireAnnotationProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }
}
