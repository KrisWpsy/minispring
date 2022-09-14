package com.sg.gate.minispringstep02.context.support;

import com.sg.gate.minispringstep02.beans.factory.ConfigurableListableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.config.BeanFactoryPostProcessor;
import com.sg.gate.minispringstep02.beans.factory.config.BeanPostProcessor;
import com.sg.gate.minispringstep02.context.ApplicationEvent;
import com.sg.gate.minispringstep02.context.ApplicationListener;
import com.sg.gate.minispringstep02.context.ConfigurableApplicationContext;
import com.sg.gate.minispringstep02.context.event.ApplicationEventMulticaster;
import com.sg.gate.minispringstep02.context.event.ContextClosedEvent;
import com.sg.gate.minispringstep02.context.event.ContextRefreshedEvent;
import com.sg.gate.minispringstep02.context.event.SimpleApplicationEventMulticaster;
import com.sg.gate.minispringstep02.core.io.DefaultResourceLoader;

import java.util.Collection;
import java.util.Map;

/**
 *  AbstractApplicationContext 继承 DefaultResourceLoader 是为了处理
 * spring.xml 配置资源的加载。
 *  之后是在 refresh() 定义实现过程，包括：
 * 
 * 1. 创建 BeanFactory，并加载 BeanDefinition
 * 
 * 2. 获取 BeanFactory
 * 
 * 3. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke
 * factory processors registered as beans in the context.)
 * 
 * 4. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注
 * 册操作
 * 
 * 5. 提前实例化单例 Bean 对象
 *  另外把定义出来的抽象方法，refreshBeanFactory()、getBeanFactory() 由后面的继
 * 承此抽象类的其他抽象类实现。
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;


    @Override
    public void refresh() {
//        1、创建BeanFactory,并加载BeanDefinition
        refreshBeanFactory();

//        2、获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

//        3、添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

//        4、在Bean实例化之前，执行对BeanDefinition的扩展
        invokeBeanFactoryPostProcessors(beanFactory);

//        5、注册BeanPostProcessor
        registryBeanPostProcessors(beanFactory);

//        6、初始化事件发布者
        initApplicationEventMulticaster();

//        7、注册事件监听器
        registerListeners();

//        8、提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

//        9、发布器刷新完成事件
        finishRefresh();
    }


    protected abstract void refreshBeanFactory();

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     *  这里主要体现了关于注册钩子和关闭的方法实现，上文提到过的
     * Runtime.getRuntime().addShutdownHook，可以尝试验证。在一些中间
     * 件和监控系统的设计中也可以用得到，比如监测服务器宕机，执行备机启动操作。
     */
    @Override
    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
//        发布容器关闭
        publishEvent(new ContextClosedEvent(this));
//        执行销毁单例bean的销毁方法
        getBeanFactory().destroySingletons();
    }



    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registryBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 初始化事件发布者(initApplicationEventMulticaster)，主要用于实例化一个
     * SimpleApplicationEventMulticaster，这是一个事件广播器。
     */
    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册事件监听器(registerListeners)，通过 getBeansOfType 方法获取到所有从
     * spring.xml 中加载到的事件配置 Bean 对象。
     */
    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    /**
     * 发布容器刷新完成事件(finishRefresh)，发布了第一个服务器启动完成后的事件，这
     * 个事件通过 publishEvent 发布出去，其实也就是调用了
     * applicationEventMulticaster.multicastEvent(event); 方法。
     */
    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
