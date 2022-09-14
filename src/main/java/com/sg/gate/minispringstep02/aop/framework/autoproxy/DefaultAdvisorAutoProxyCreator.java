package com.sg.gate.minispringstep02.aop.framework.autoproxy;

import com.sg.gate.minispringstep02.aop.*;
import com.sg.gate.minispringstep02.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.sg.gate.minispringstep02.aop.framework.ProxyFactory;
import com.sg.gate.minispringstep02.beans.PropertyValues;
import com.sg.gate.minispringstep02.beans.factory.BeanFactory;
import com.sg.gate.minispringstep02.beans.factory.BeanFactoryAware;
import com.sg.gate.minispringstep02.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.sg.gate.minispringstep02.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;


import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *  这个 DefaultAdvisorAutoProxyCreator 类的主要核心实现在于
 * postProcessBeforeInstantiation 方法中，从通过 beanFactory.getBeansOfType 获
 * 取 AspectJExpressionPointcutAdvisor 开始。
 *  获取了 advisors 以后就可以遍历相应的 AspectJExpressionPointcutAdvisor 填充对
 * 应的属性信息，包括：目标对象、拦截方法、匹配器，之后返回代理对象即可。
 *  那么现在调用方获取到的这个 Bean 对象就是一个已经被切面注入的对象了，当调
 * 用方法的时候，则会被按需拦截，处理用户需要的信息。
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private final Set<Object> earlyProxyReference = Collections.synchronizedSet(new HashSet<Object>());

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
            return null;
        }


    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) {
        return true;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) {
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 关于 DefaultAdvisorAutoProxyCreator 类的操作主要就是把创建 AOP 代理的操作
     * 从 postProcessBeforeInstantiation 移动到 postProcessAfterInitialization 中去。
     * @param bean
     * @param beanName
     * @return
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!earlyProxyReference.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }

        return bean;
    }

    protected Object wrapIfNecessary(Object bean, String beanName) {
        if (isInfrastructureClass(bean.getClass())) return bean;

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
//            过滤匹配类
            if (!classFilter.matches(bean.getClass())) continue;

            AdviseSupport adviseSupport = new AdviseSupport();

            TargetSource targetSource = new TargetSource(bean);
            adviseSupport.setTargetSource(targetSource);
            adviseSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            adviseSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            adviseSupport.setProxyTargetClass(true);

//            返回代理对象
            return new ProxyFactory(adviseSupport).getProxy();
        }

        return bean;
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReference.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}
