package com.sg.gate.minispringstep02.beans.factory.config;

import com.sg.gate.minispringstep02.beans.PropertyValue;
import com.sg.gate.minispringstep02.beans.PropertyValues;

/**
 *  在 Bean 注册的过程中是需要传递 Bean 的信息，在几个前面章节的测试中都有
 * 所体现 new BeanDefinition(UserService.class,
 * propertyValues);
 *
 *  所以为了把属性一定交给 Bean 定义，所以这里填充了 PropertyValues 属性，同
 * 时把两个构造函数做了一些简单的优化，避免后面 for 循环时还得判断属性填充
 * 是否为空。
 */
public class BeanDefinition {

    String  SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    String  SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

//    这样可以把Bean的实例化操作放到容器中处理。
    private Class beanClass;

    private PropertyValues propertyValues;

    /**
     * 在 BeanDefinition 新增加了两个属性：initMethodName、destroyMethodName，
     * 这两个属性是为了在 spring.xml 配置的 Bean 对象中，可以配置 initmethod="initDataMethod" destroymethod="destroyDataMethod" 操作，
     * 最终实现接口的效果是一样的。
     * 只不过一个是接口方法的直接调用，另外是一个在配置文件中读取到方法反射调用
     */
    private String initMethodName;

    private String destroyMethodName;

    /**
     *  singleton、prototype，是本次在 BeanDefinition 类中新增加的两个属性信息，用
     * 于把从 spring.xml 中解析到的 Bean 对象作用范围填充到属性中。
     */
    private String scope = SCOPE_SINGLETON;

    private boolean singleton = true;

    private boolean prototype = false;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public BeanDefinition(Class beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public BeanDefinition(Class beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
