package com.sg.gate.minispringstep02.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.BeanReference;
import com.sg.gate.minispringstep02.beans.factory.support.AbstractBeanDefinitionReader;
import com.sg.gate.minispringstep02.beans.factory.support.BeanDefinitionRegistry;
import com.sg.gate.minispringstep02.context.annotation.ClassPathBeanDefinitionScanner;
import com.sg.gate.minispringstep02.core.io.Resource;
import com.sg.gate.minispringstep02.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;



import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) {
        try {
            InputStream is = resource.getInputStream();
            doLoadBeanDefinitions(is);
        } catch (IOException | ClassNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String...locations) {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    /**
     *  loadBeanDefinitions 方法，处理资源加载，这里新增加了一个内部方法：
     * doLoadBeanDefinitions，它主要负责解析 xml
     *  在 doLoadBeanDefinitions 方法中，主要是对 xml 的读取
     * XmlUtil.readXML(inputStream) 和元素 Element 解析。在解析的过程中
     * 通过循环操作，以此获取 Bean 配置以及配置中的 id、name、class、value、ref
     * 信息。
     *  最终把读取出来的配置信息，创建成 BeanDefinition 以及 PropertyValue，最终把
     * 完整的 Bean 定义内容注册到 Bean 容器：
     * getRegistry().registerBeanDefinition(beanName,
     * beanDefinition)
     *
     * =======================================================================
     *
     * 关于 XmlBeanDefinitionReader 中主要是在加载配置文件后，处理新增的自定义配
     * 置属性 component-scan，解析后调用 scanPackage 方法，其实也就是我们在
     * ClassPathBeanDefinitionScanner#doScan 功能。
     * @param inputStream
     * @throws ClassNotFoundException
     */
    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {

        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        // 解析 context:component-scan 标签，扫描包中的类并提取相关信息，用于组装 BeanDefinition
        Element componentScan = root.element("component-scan");
        if (null != componentScan) {
            String scanPath = componentScan.attributeValue("base-package");
            if (StrUtil.isEmpty(scanPath)) {
                throw new RuntimeException();
            }
            scanPackage(scanPath);
        }


        List<Element> beanList = root.elements("bean");
        for (Element bean : beanList) {

            String id = bean.attributeValue("id");
            String name = bean.attributeValue("name");
            String className = bean.attributeValue("class");
            String initMethod = bean.attributeValue("init-method");
            String destroyMethodName = bean.attributeValue("destroy-method");
            String beanScope = bean.attributeValue("scope");

            // 获取 Class，方便获取类中的名称
            Class<?> clazz = Class.forName(className);
            // 优先级 id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // 定义Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            List<Element> propertyList = bean.elements("property");
            // 读取属性并填充
            for (Element property : propertyList) {
                // 解析标签：property
                String attrName = property.attributeValue("name");
                String attrValue = property.attributeValue("value");
                String attrRef = property.attributeValue("ref");
                // 获取属性值：引入对象、值对象
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
                // 创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new RuntimeException();
            }




//        Document document = XmlUtil.readXML(is);
//        Element root = document.getDocumentElement();
//        NodeList childNodes = root.getChildNodes();


        /*for (int i = 0; i < childNodes.getLength(); i++) {
//            判断元素
            if (!(childNodes.item(i) instanceof Element)) continue;
//            判断对象
            if (!"bean".equals(childNodes.item(i).getNodeName())) continue;

//          解析标签
            Element bean =(Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            String initMethod = bean.getAttribute("init-method");
            String destroyMethodName = bean.getAttribute("destroy-method");
            String beanScope = bean.getAttribute("scope");
//            获取class,方便获取类中的名称
            Class<?> clazz = Class.forName(className);
//            优先级 id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            *//**
             *  在解析 XML 处理类 XmlBeanDefinitionReader 中，新增加了关于 Bean 对象配置
             * 中 scope 的解析，并把这个属性信息填充到 Bean 定义中。
             * beanDefinition.setScope(beanScope)
             *//*
//            定义bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethodName);

            if (StrUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }
//            读取属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                if (!(bean.getChildNodes().item(j) instanceof Element)) continue;
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) continue;

//                解析标签
                Element property =(Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
//                获取属性值：引入对象、值对象
                Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
//                创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new RuntimeException();
            }*/
//            注册BeanDefinition
            getRegistry().registryBeanDefinition(beanName, beanDefinition);
        }
    }

    private void scanPackage(String scanPath) {
        String[] basePackages = StrUtil.splitToArray(scanPath, ',');
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
        scanner.doScan(basePackages);
    }
}
