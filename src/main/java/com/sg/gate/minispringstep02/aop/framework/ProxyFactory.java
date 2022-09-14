package com.sg.gate.minispringstep02.aop.framework;

import com.sg.gate.minispringstep02.aop.AdviseSupport;

/**
 *  其实这个代理工厂主要解决的是关于 JDK 和 Cglib 两种代理的选择问题，有了代
 * 理工厂就可以按照不同的创建需求进行控制。
 */
public class ProxyFactory {

    private AdviseSupport adviseSupport;

    public ProxyFactory(AdviseSupport adviseSupport) {
        this.adviseSupport = adviseSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    public AopProxy createAopProxy() {
        if (adviseSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(adviseSupport);
        }

        return new JdkDynamicAopProxy(adviseSupport);
    }
}
