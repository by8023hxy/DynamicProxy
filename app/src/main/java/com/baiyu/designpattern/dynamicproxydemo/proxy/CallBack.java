package com.baiyu.designpattern.dynamicproxydemo.proxy;


/**
 * 执行的网络请求后的回调
 * @param <R>
 */
public abstract class CallBack<R> {

    public abstract void onSuccess(R result);

    public abstract void onError(String errorMsg, String errorCode);
}
