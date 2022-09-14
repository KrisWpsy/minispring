package com.sg.gate.minispringstep02.core.io;

public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

//     定义获取资源接口，里面传递 location 地址即可。
    Resource getResource(String location);
}
