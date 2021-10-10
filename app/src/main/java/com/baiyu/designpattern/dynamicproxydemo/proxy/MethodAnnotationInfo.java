package com.baiyu.designpattern.dynamicproxydemo.proxy;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 最终请求的信息（也就是注解信息）
 */
@SuppressWarnings("unused")
public class MethodAnnotationInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    //请求的来源
    public String className;
    //请求方式
    private String requestType;
    //请求url地址
    private String url;
    //添加请求头
    private LinkedHashMap<String, String> headers;
    //添加请求体
    private String body;
    //请求参数
    private Map<String, String> params;
    //返回值类型
    private Type resultType;
    //组拼的url路径
    private LinkedList<KVEntry<String, String>> paths;

    public Type getResultType() {
        return resultType;
    }

    void setResultType(Type resultType) {
        this.resultType = resultType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    void addHeader(String key, String value) {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        this.headers.put(key, value);
    }

    void addParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        this.params.put(key, value);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getBody() {
        return body;
    }

    void setBody(String body) {
        this.body = body;
    }

    public String getRequestType() {
        return requestType;
    }

    void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return parseUrl(this.url, this.paths);
    }

    void setUrl(String url) {
        this.url = url;
    }

    LinkedList<KVEntry<String, String>> getPaths() {
        return paths;
    }

    void addPath(KVEntry<String, String> KVEntry) {
        if (paths == null) {
            paths = new LinkedList<>();
        }
        paths.add(KVEntry);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @NonNull
    @Override
    public String toString() {
        return "MethodAnnotationInfo{" +
                "requestType='" + requestType + '\'' +
                ", className='" + className + '\'' +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", params=" + params +
                ", body='" + body + '\'' +

                '}';
    }

    /**
     * 解析url--得到真正的url
     *
     * @return 解析后的url
     */
    private String parseUrl(String url, LinkedList<KVEntry<String, String>> paths) {
        if (paths != null && paths.size() > 0 && url.contains("{") && url.contains("}")) {
            return checkUrl(url, paths);
        } else {
            return url;
        }
    }

    /**
     * 检查设置的url中是否还存在@Path中设置的路径
     *
     * @param newUrl           原url
     * @param parameterEntries @Path中设置的路径
     * @return 返回实际的url
     */
    private String checkUrl(String newUrl, LinkedList<KVEntry<String, String>> parameterEntries) {
        if (parameterEntries.size() <= 0) {
            return newUrl;
        }
        KVEntry<String, String> KVEntry = parameterEntries.removeLast();
        String key = KVEntry.getKey();
        if (newUrl.contains(key)) {
            String replace = newUrl.replace("{" + key + "}", KVEntry.getValue());
            return checkUrl(replace, parameterEntries);
        }
        return newUrl;
    }
}
