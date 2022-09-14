package com.sg.gate.minispringstep02.beans.factory.support;

import com.sg.gate.minispringstep02.core.io.Resource;
import com.sg.gate.minispringstep02.core.io.ResourceLoader;

/**
 *  这是一个 Simple interface for bean definition readers. 其实里面无非定义了几个方
 * 法，包括：getRegistry()、getResourceLoader()，以及三个加载 Bean 定义的方法。
 *
 *  这里需要注意 getRegistry()、getResourceLoader()，都是用于提供给后面三个方法
 * 的工具，加载和注册，这两个方法的实现会包装到抽象类中，以免污染具体的接口
 * 实现方法。
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource);

    void loadBeanDefinitions(Resource... resources);

    void loadBeanDefinitions(String location);

    void loadBeanDefinitions(String...locations);
}
