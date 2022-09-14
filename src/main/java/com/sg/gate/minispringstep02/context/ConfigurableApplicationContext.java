package com.sg.gate.minispringstep02.context;

/**
 * ConfigurableApplicationContext 继承自 ApplicationContext，并提供了 refresh 这
 * 个核心方法。如果你有看过一些 Spring 源码，那么一定会看到这个方法。 接下
 * 来也是需要在上下文的实现中完成刷新容器的操作过程。
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 刷新容器
     */
    void refresh();

    /**
     *  首先我们需要在 ConfigurableApplicationContext 接口中定义注册虚拟机钩子的方
     * 法 registerShutdownHook 和手动执行关闭的方法 close。
     */
    void registerShutDownHook();

    void close();
}
