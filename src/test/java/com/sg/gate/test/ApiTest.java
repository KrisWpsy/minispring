package com.sg.gate.test;

import cn.hutool.core.io.IoUtil;
import com.sg.gate.minispringstep02.aop.AdviseSupport;
import com.sg.gate.minispringstep02.aop.TargetSource;
import com.sg.gate.minispringstep02.aop.aspectj.AspectJExpressionPointcut;
import com.sg.gate.minispringstep02.aop.framework.Cglib2AopProxy;
import com.sg.gate.minispringstep02.aop.framework.JdkDynamicAopProxy;
import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.PropertyValues;
import com.sg.gate.minispringstep02.beans.factory.config.BeanDefinition;
import com.sg.gate.minispringstep02.beans.factory.config.BeanReference;
import com.sg.gate.minispringstep02.beans.factory.support.DefaultListableBeanFactory;
import com.sg.gate.minispringstep02.beans.factory.xml.XmlBeanDefinitionReader;
import com.sg.gate.minispringstep02.context.annotation.ClassPathBeanDefinitionScanner;
import com.sg.gate.minispringstep02.context.support.ClassPathXmlApplicationContext;
import com.sg.gate.minispringstep02.core.io.DefaultResourceLoader;
import com.sg.gate.minispringstep02.core.io.Resource;
import com.sg.gate.test.bean.*;
import com.sg.gate.test.common.MyBeanFactoryPostProcessor;
import com.sg.gate.test.common.MyBeanPostProcessor;
import com.sg.gate.test.event.CustomEvent;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ApiTest {

    /*@Test
    public void test_BeanFactory() {
//      1、生成bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//      2、初始化bean的自定义信息，并且注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        beanFactory.registryBeanDefinition("userService", beanDefinition);
//      3、获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.queryInfo();
//      4、重复获取bean,得到的还是之前的那个bean
        UserService userService1 = (UserService) beanFactory.getBean("userService");
        userService1.queryInfo();

    }

    @Test
    public void test_BeanFactory2() {
//        1、获取工厂类
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
//        2、获取UserService的定义信息，并注册bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class);
        defaultListableBeanFactory.registryBeanDefinition("userService", beanDefinition);
//        3、获取bean
        UserService bean =(UserService) defaultListableBeanFactory.getBean("userService", "小王");
        bean.queryInfo();
    }


    @Test
    public void test_BeanFactory3() {
//        创建工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        注册UserDao
        beanFactory.registryBeanDefinition("userDao", new BeanDefinition(UserDao.class));
//        给UserService填充属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uId", "10001"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
//        UserService注入bean
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registryBeanDefinition("userService", beanDefinition);
//        获取UserService
        UserService userService =(UserService) beanFactory.getBean("userService");
        userService.queryInfo();
    }*/

    /*private DefaultResourceLoader resourceLoader;

    @Before
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }

    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_file() throws IOException {
        Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("https://github.com/fuzhengwei/small-spring/important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }*/

    /**
     * 我们把以前通过手动注册 Bean 以及配置属性信息
     * 的内容，交给了 new XmlBeanDefinitionReader(beanFactory) 类读取 Spring.xml 的方式来处理
     */
   /* @Test
    public void test_xml() {
//        初始化BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//        读取配置文件，注册Bean
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
//        获取Bean对象
        UserService userService = beanFactory.getBean("userService", UserService.class);
        userService.queryInfo();
    }*/


    /**
     *  DefaultListableBeanFactory 创建 beanFactory 并使用 XmlBeanDefinitionReader
     * 加载配置文件的方式，还是比较熟悉的。
     *  接下来就是对 MyBeanFactoryPostProcessor 和 MyBeanPostProcessor 的处理，一
     * 个是在 BeanDefinition 加载完成 & Bean 实例化之前，修改 BeanDefinition 的属
     * 性值，另外一个是在 Bean 实例化之后，修改 Bean 属性信息。
     */
    /*@Test
    public void test_BeanFactoryPostProcessorAndBeanPostProcessor() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        MyBeanPostProcessor myBeanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(myBeanPostProcessor);
        UserService userService = beanFactory.getBean("userService", UserService.class);
        userService.queryInfo();
    }*/

    /**
     * 另外使用新增加的 ClassPathXmlApplicationContext 应用上下文类，再操作起来就
     * 方便多了，这才是面向用户使用的类，在这里可以一步把配置文件交给
     * ClassPathXmlApplicationContext，也不需要管理一些自定义实现的 Spring 接口的
     * 类。
     */
   /* @Test
    public void text_Xml2() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.queryInfo();
    }

    @Test
    public void test_Xml3() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutDownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.queryInfo();

    }*/

   /* @Test
    public void test_Xml4() {
//        1、初始化BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutDownHook();
//        2、获取Bean对象调用方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.queryInfo();
        System.out.println(userService.getApplicationContext());
        System.out.println(userService.getBeanFactory());


    }*/

    /*@Test
    public void test_prototype() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutDownHook();
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);


        System.out.println(userService01);
        System.out.println(userService02);
    }

    @Test
    public void test_factory_bean() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.registerShutDownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
//        userService.queryInfo();
    }

    @Test
    public void test_event() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));
        applicationContext.registerShutDownHook();
    }

    @Test
    public void test_aop() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* com.sg.gate.test.bean.UserService.*(..))");
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("queryUserInfo");

        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }

    @Test
    public void test_dynamic() {
//        目标对象
        IUserService userService = new UserService();

//        组装代理信息
        AdviseSupport adviseSupport = new AdviseSupport();
        adviseSupport.setTargetSource(new TargetSource(userService));
        adviseSupport.setMethodInterceptor(new UserServiceInterceptor());
        adviseSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* com.sg.gate.test.bean.IUserService.*(..))"));

//        代理对象（jdk)
       *//* IUserService proxy_jdk =(IUserService) new JdkDynamicAopProxy(adviseSupport).getProxy();
        System.out.println(proxy_jdk.queryUserInfo());*//*

//      代理对象（cglib)
        IUserService proxy_cglib = (IUserService) new Cglib2AopProxy(adviseSupport).getProxy();
        System.out.println(proxy_cglib.queryUserInfo());
    }*/

    /*@Test
    public void test_aop() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
//        在这里打断点可以看到获取到的userService对象其实已经被替换成了代理对象
        System.out.println(userService.queryUserInfo());
    }*/

    /*@Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-scan.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println(userService.queryUserInfo());
    }*/

    /*@Test
    public void test_scan() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println(userService.queryUserInfo());
    }*/

//  虽然能成功获取结果，但是存在类型转换异常，拦截方法也没有拦截到
//  java.lang.ClassCastException
    /*@Test
    public void test_autoProxy() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println(userService.queryUserInfo());
    }*/

    @Test
    public void test_circular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());

    }

}
