package com.sg.gate.minispringstep02.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 这里先要提供一个可以通过配置路径
 * basePackage=cn.bugstack.springframework.test.bean，解析出
 * classes 信息的工具方法 findCandidateComponents，通过这个方法就可以扫描到
 * 所有 @Component 注解的 Bean 对象了。
 */
public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            candidates.add(new BeanDefinition(clazz));
        }
        return candidates;
    }
}
