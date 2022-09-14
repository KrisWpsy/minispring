package com.sg.gate.minispringstep02.context.support;

/**
 *  ClassPathXmlApplicationContext，是具体对外给用户提供的应用上下文方法。
 *  在继承了 AbstractXmlApplicationContext 以及层层抽象类的功能分离实现后，在
 * 此类 ClassPathXmlApplicationContext 的实现中就简单多了，主要是对继承抽象类
 * 中方法的调用和提供了配置文件地址信息。
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {}

//    从 XML 中加载 BeanDefinition，并刷新上下文
    public ClassPathXmlApplicationContext(String configLocations) {
        this(new String[]{configLocations});
    }

//    从 XML 中加载 BeanDefinition，并刷新上下文
    public ClassPathXmlApplicationContext(String[] configLocations) {
        this.configLocations = configLocations;
        refresh();
    }


    @Override
    protected String[] getConfigLocations() {
        return configLocations;
    }
}
