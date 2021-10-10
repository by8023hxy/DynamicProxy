package com.baiyu.designpattern.dynamicproxydemo.proxy;

import android.content.Context;
import android.util.Log;


@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class HttpAdapter implements ProxyHttpAdapter {

    private final Context mContext;

    public HttpAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void async(MethodAnnotationInfo annotationInfo, CallBack callBack) {
        if (annotationInfo != null) {
            String url = annotationInfo.getUrl();
            Log.d("DynamicProxy", "url==" + url);
            if (callBack!=null){
//                callBack.onSuccess("Success");
                callBack.onError("Error","1010");
            }
        }
    }
}
