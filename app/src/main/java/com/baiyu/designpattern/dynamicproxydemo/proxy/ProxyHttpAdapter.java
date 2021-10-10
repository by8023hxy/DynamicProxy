package com.baiyu.designpattern.dynamicproxydemo.proxy;

/**
 * 网络框架适配器 用于集成其它网络框架
 */
public interface ProxyHttpAdapter {

    void async(MethodAnnotationInfo annotationInfo, CallBack<?> callBack);

}
