package com.baiyu.designpattern.dynamicproxydemo.proxy;

import android.content.Context;

public class ApiManager {

    private static DynamicProxy proxy;

    private ApiManager(Context context) {
        proxy = new DynamicProxy.Builder(new HttpAdapter(context))
                .setBaseUrl("").build();
    }

    public static NetService getInstance(Context context) {
        return new ApiManager(context).getNetService();
    }

    private NetService getNetService() {
        return proxy.create(NetService.class);
    }
}
