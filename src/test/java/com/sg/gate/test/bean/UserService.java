package com.sg.gate.test.bean;

import com.sg.gate.minispringstep02.beans.annotation.Autowired;
import com.sg.gate.minispringstep02.beans.annotation.Value;
import com.sg.gate.minispringstep02.beans.factory.*;
import com.sg.gate.minispringstep02.context.ApplicationContext;
import com.sg.gate.minispringstep02.context.ApplicationContextAware;
import com.sg.gate.minispringstep02.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

//@Component("userService")
public class UserService implements IUserService /*implements BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, InitializingBean, DisposableBean*/ {

//    @Value("${token}")
    private String token;

    /*@Autowired
    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }*/

    /*@Override*/
    public String queryUserInfo() {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return /*userDao.queryUserName("10001")*/ "小明，100001，深圳，" + token;
    }

    /*@Override*/
    public String register(String userName) {
        try {
            Thread.sleep(new Random(1).nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "注册用户：" + userName + "成功!";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "UserService#token = { " + token + " }";
    }

    /*private ApplicationContext applicationContext;
    private BeanFactory beanFactory;*/

//    private String name;

    /*private String uId;

    private String company;

    private String location;

    private IUserDao userDao;


    public void queryInfo() {
        System.out.println("查询用户信息: " + "," + userDao.queryUserName(uId) + "," + company + "," + location);
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public IUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }*/

    /*@Override
    public void destroy() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("执行：UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("执行：UserService.afterPropertiesSet");
    }
*/
   /* @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("ClassLoader："  + classLoader);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Bean Name is：" + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext =applicationContext;
    }*/

    /*public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }*/



    /*public UserService(String name) {
        this.name = name;
    }

    public void queryInfo() {
        System.out.println("查询用户信息" + name);
    }

    @Override
    public String toString() {
        return "UserService{" +
                "name='" + name + '\'' +
                '}';
    }*/
}
