package com.sg.gate.minispringstep02.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 *  在 Spring 框架下创建 core.io 核心包，在这个包中主要用于处理资源加载流。
 *  定义 Resource 接口，提供获取 InputStream 流的方法，接下来再分别实现三种
 * 不同的流文件操作：classPath、FileSystem、URL
 */
public interface Resource {

    InputStream getInputStream() throws IOException;
}
