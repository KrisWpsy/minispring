package com.sg.gate.minispringstep02.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.PropertyValues;
import com.sg.gate.minispringstep02.beans.factory.*;
import com.sg.gate.minispringstep02.beans.factory.config.*;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 11111111111111111111111111111111111111111111111111111111
 *  在 AbstractAutowireCapableBeanFactory 类中实现了 Bean 的实例化操作
 * newInstance，其实这块会埋下一个坑，有构造函数入参的对象怎么处理？可以
 * 提前思考
 *=============================================================
 * 2222222222222222222222222222222222222222222222222222222
 *  在处理完 Bean 对象的实例化后，直接调用 addSingleton 方法存放到单例对
 * 象的缓存中去。
 *=============================================================
 *
 * 33333333333333333333333333333333333333333333333333333
 *  首先在 AbstractAutowireCapableBeanFactory 抽象类中定义了一个创建对象的实
 * 例化策略属性类 InstantiationStrategy instantiationStrategy，
 * 这里我们选择了 Cglib 的实现类。
 *  接下来抽取 createBeanInstance 方法，在这个方法中需要注意 Constructor
 * 代表了你有多少个构造函数，通过 beanClass.getDeclaredConstructors() 方式可以
 * 获取到你所有的构造函数，是一个集合。
 *  接下来就需要循环比对出构造函数集合与入参信息 args 的匹配情况，这里我们
 * 对比的方式比较简单，只是一个数量对比，而实际 Spring 源码中还需要比对入参
 * 类型，否则相同数量不同入参类型的情况，就会抛异常了。
 * =============================================================
 *
 * 4444444444444444444444444444444444444444444444444444
 *  实现 BeanPostProcessor 接口后，会涉及到两个接口方法，
 * postProcessBeforeInitialization、
 * postProcessAfterInitialization，分别作用于 Bean 对象执行初始化前
 * 后的额外处理。
 *  也就是需要在创建 Bean 对象时，在 createBean 方法中添加
 * initializeBean(beanName, bean, beanDefinition); 操作。而这个
 * 操作主要主要是对于方法
 * applyBeanPostProcessorsBeforeInitialization、
 * applyBeanPostProcessorsAfterInitialization 的使用。
 *  另外需要提一下，applyBeanPostProcessorsBeforeInitialization、
 * applyBeanPostProcessorsAfterInitialization 两个方法是在接口类
 * AutowireCapableBeanFactory 中新增加的。
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();


    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
//            判断是否返回代理Bean对象
        Object bean = bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object bean = null;
        try {
//            实例化bean
            bean = createBeanInstance(beanDefinition, beanName, args);

//            处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
            if (beanDefinition.isSingleton()) {
                Object finalBean = bean;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

            //            实例化后判断
            boolean continueWithPropertyPopulation = applyBeanPostProcessorAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }
//            在设置Bean属性之前，允许BeanPostProcessor修改属性值
            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
//            给bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
//            执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        注册实现了DisposableBean接口的Bean对象
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

//        判断SCOPE_SINGLETON、SCOPE_PROTOTYPE
        Object exposedObject = bean;
        if (beanDefinition.isSingleton()) {
//            获取代理对象
            exposedObject = getSingleton(beanName);
            registerSingleton(beanName, exposedObject);
        }

        return exposedObject;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor). getEarlyBeanReference(bean, beanName);
                if (exposedObject == null) return exposedObject;
            }
        }

        return exposedObject;
    }



    protected Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (bean != null) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) return result;
            }
        }
        return null;
    }


    protected Object createBeanInstance(BeanDefinition beanDefinition, String beanName, Object[] args) throws Exception {
        Constructor constructorToUse = null;
        Class beanClass = beanDefinition.getBeanClass();
        Constructor[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors) {
//            这里我们对比的方式比较简单，只是一个数量对比，而实际 Spring 源码中还需要比对入参
//            类型，否则相同数量不同入参类型的情况，就会抛异常了。
            if (args != null && ctor.getParameterTypes().length == args.length) {
                constructorToUse = ctor;
                break;
            }
        }
        return getInstantiationStrategy().instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    private boolean applyBeanPostProcessorAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    /**
     * 在设置Bean属性之前， 允许BeanPostProcessor修改属性值
     *
     * applyBeanPostProcessorsBeforeApplyingPropertyValues 方法中，首先就
     * 是获取已经注入的 BeanPostProcessor 集合并从中筛选出继承接口
     * InstantiationAwareBeanPostProcessor 的实现类。
     *
     * 最后就是调用相应的 postProcessPropertyValues 方法以及循环设置属性值信息，
     * beanDefinition.getPropertyValues().addPropertyValue(prope
     * rtyValue);
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                if (pvs != null) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }
    }


    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
             for (PropertyValue pv : propertyValues.getPropertyValues()) {
                String name = pv.getName();
                Object value = pv.getValue();
                if (value instanceof BeanReference) {
//                    A依赖B，获取B的实例化
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }
//                属性填充
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }


    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }


    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        /**
         *  首先在 initializeBean 中，通过判断 bean instanceof Aware，调用了三个
         * 接口方法，BeanFactoryAware.setBeanFactory(this)、
         * BeanClassLoaderAware.setBeanClassLoader(getBeanClassLoade
         * r())、BeanNameAware.setBeanName(beanName)，这样就能通知到已经
         * 实现了此接口的类。
         */
        if (bean instanceof Aware) {
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
            if (bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
            }
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
        }

//        1、执行BeanPostProcessorBefore处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

//        2、待完成内容：invokeInitMethods（...）
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

//        3、执行BeanPostProcessorAfter处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) return result;
            result = current;
        }
        return result;
    }


    /**
     *  抽象类 AbstractAutowireCapableBeanFactory 中的 createBean 是用来创建 Bean
     * 对象的方法，在这个方法中我们之前已经扩展了 BeanFactoryPostProcessor、
     * BeanPostProcessor 操作，这里我们继续完善执行 Bean 对象的初始化方法的处理
     * 动作。
     *  在方法 invokeInitMethods 中，主要分为两块来执行实现了 InitializingBean 接口
     * 的操作，处理 afterPropertiesSet 方法。另外一个是判断配置信息 init-method 是
     * 否存在，执行反射调用 initMethod.invoke(bean)。这两种方式都可以在 Bean 对象
     * 初始化过程中进行处理加载 Bean 对象中的初始化操作，让使用者可以额外新增加
     * 自己想要的动作。
     */
//    bean的初始化
    private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        1、实现接口InitializingBean
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

//        2、配置信息 init-method {为了避免二次执行初始化}
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName) && (!(bean instanceof  InitializingBean))) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (initMethod == null) {
                throw new RuntimeException();
            }
            initMethod.invoke(bean);
        }
    }


    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) return result;
            result = current;
        }
        return result;
    }

    /**
     * 在创建 Bean 对象的实例的时候，需要把销毁方法保存起来，方便后续执行销毁动
     * 作进行调用。
     *  那么这个销毁方法的具体方法信息，会被注册到 DefaultSingletonBeanRegistry 中
     * 新增加的 Map<String, DisposableBean> disposableBeans 属性中
     * 去，因为这个接口的方法最终可以被类 AbstractApplicationContext 的 close 方法
     * 通过 getBeanFactory().destroySingletons() 调用。
     *  在注册销毁方法的时候，会根据是接口类型和配置类型统一交给
     * DisposableBeanAdapter 销毁适配器类来做统一处理。实现了某个接口的类可以被
     * instanceof 判断或者强转后调用接口方法
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
//        非 Singleton 类型的 Bean 不执行销毁方法
        if (!beanDefinition.isSingleton()) return;

        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
