package com.baiyu.designpattern.dynamicproxydemo.proxy;

/**
 * 接口返回值的包装类（也是真正执行类）
 * @param <R>
 */
public class Call<R> {

    private final ProxyHttpAdapter proxyHttpAdapter;

    private final MethodAnnotationInfo methodAnnotationInfo;

    Call(ProxyHttpAdapter ProxyHttpAdapter, MethodAnnotationInfo methodAnnotationInfo) {
        this.proxyHttpAdapter = ProxyHttpAdapter;
        this.methodAnnotationInfo = methodAnnotationInfo;
    }

    public void execute(CallBack<R> callBack) {
        proxyHttpAdapter.async(methodAnnotationInfo, callBack);
    }

}
