package com.sg.gate.minispringstep02.beans.factory;

import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.PropertyValues;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.BeanFactoryPostProcessor;
import com.sg.gate.minispringstep02.core.io.DefaultResourceLoader;
import com.sg.gate.minispringstep02.core.io.Resource;
import com.sg.gate.minispringstep02.utils.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * PropertyPlaceholderConfigurer 目前看上去像一块单独的内容，后续会把这块的内
 * 容与自动加载 Bean 对象进行整合，也就是可以在注解上使用占位符配置一些在配
 * 置文件里的属性信息。
 *
 *  依赖于 BeanFactoryPostProcessor 在 Bean 生命周期的属性，可以在 Bean 对象
 * 实例化之前，改变属性信息。所以这里通过实现 BeanFactoryPostProcessor 接
 * 口，完成对配置文件的加载以及摘取占位符中的在属性文件里的配置。
 *  这样就可以把提取到的配置信息放置到属性配置中了，
 * buffer.replace(startIdx, stopIdx + 1, propVal);
 * propertyValues.addPropertyValue
 *
 * ==============================================================
 *
 * 在解析属性配置的类 PropertyPlaceholderConfigurer 中，最主要的其实就是这行
 * 代码的操作
 * beanFactory.addEmbeddedValueResolver(valueResolver) 这是把
 * 属性值写入到了 AbstractBeanFactory 的 embeddedValueResolvers 中。
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        try {
//            加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);

//            占位符替换属性值
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String)) continue;
                    value = resolvePlaceholder((String) value, properties);
                    propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
                }
            }

//          向容器中添加字符串解析器，供解析@Value注解使用
            StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
            beanFactory.addEmbeddedValueResolver(valueResolver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String resolvePlaceholder(String value, Properties properties) {
        String strVal = (String) value;
        StringBuilder buffer = new StringBuilder(strVal);
        int startIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
            String propKey = strVal.substring(startIdx + 2, stopIdx);
            String propVal = properties.getProperty(propKey);
            buffer.replace(startIdx, stopIdx + 1, propVal);
        }
        return buffer.toString();
    }


    public void setLocation(String location) {
        this.location = location;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }
}
