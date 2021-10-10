package com.baiyu.designpattern.dynamicproxydemo.proxy

import android.os.Build
import android.text.TextUtils
import com.baiyu.designpattern.dynamicproxydemo.httpannoation.*
import java.lang.reflect.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 仿retrofit实现动态代理
 */
class DynamicProxy private constructor(builder: Builder) {

    private val methodAnnotationInfoCache = ConcurrentHashMap<String, MethodAnnotationInfo>()

    private val proxyHttpAdapter: ProxyHttpAdapter = builder.proxyHttpAdapter

    private val baseUrl: String

    fun <T> create(service: Class<T>): T {
        //验证service是否是接口 且接口中要有方法
        validateServiceInterface(service)
        //使用动态代理
        val classLoader = service.classLoader
        val classes = arrayOf<Class<*>>(service)
        val invocationHandler = InvocationHandler { proxy, method, args ->
            //如果是在Object类中声明的方法(也就是自己实现了)，就直接调用
            if (method.declaringClass == Any::class.java) {
                method.invoke(proxy, *args)
            } else if (isDefaultMethod(method)) {
                //如果是接口中定义的默认方法 抛出异常
                invokeDefaultMethod()
            } else { //开始实现LQRetrofit
                //获取方法返回值的全类名
                val genericReturnType = method.genericReturnType
                // 判断返回值的类型是否是参数化类型
                if (genericReturnType is ParameterizedType) {
                    val methodAnnotationInfo =
                        getMethodAnnotationInfo(method, args, genericReturnType)
                    Call<Any>(proxyHttpAdapter, methodAnnotationInfo)
                } else {
                    throw IllegalStateException("The return value type of the method must be the Call<T> type")
                }
            }
        }
        return Proxy.newProxyInstance(classLoader, classes, invocationHandler) as T
    }

    private fun isDefaultMethod(method: Method): Boolean {
        return if (Build.VERSION.SDK_INT < 24) {
            false
        } else method.isDefault
    }

    /**
     * 检查传递的类型是不是接口
     */
    private fun <T> validateServiceInterface(service: Class<T>) {
        require(service.isInterface) { "API declarations must be interfaces." }
        require(service.interfaces.isEmpty()) { "API interfaces must not extend other interfaces." }
    }

    private fun invokeDefaultMethod(
    ): Any? {
        throw UnsupportedOperationException()
    }

    /**
     * 获取解析后的MethodAnnotationInfo
     *
     * @param method 方法
     * @param args   方法参数
     * @return
     */
    private fun getMethodAnnotationInfo(
        method: Method,
        args: Array<Any>,
        genericReturnType: Type
    ): MethodAnnotationInfo {
        val methodName = method.name
        val annotationInfo = methodAnnotationInfoCache[methodName]
        return if (annotationInfo != null) {
            annotationInfo
        } else {
            val methodAnnotationInfo = parseMethodAnnotationInfo(method, args, genericReturnType)
            methodAnnotationInfoCache[methodName] = methodAnnotationInfo
            methodAnnotationInfo
        }
    }

    /**
     * 解析接口方法中的注解
     *
     * @param method 方法
     * @param args   方法参数
     * @return
     */
    private fun parseMethodAnnotationInfo(
        method: Method,
        args: Array<Any>,
        genericReturnType: Type
    ): MethodAnnotationInfo {
        //获取调用接口方法上的所有注解
        val methodAnnotations = method.annotations
        //解析方法上的所有注解
        check(methodAnnotations.isNotEmpty()) { "Please specify @GET or @POST or @PUT or @DELETE" }
        val parameterizedType = genericReturnType as ParameterizedType
        // 返回表示此类型实际类型参数的 Type 对象的数组
        val typeArguments = parameterizedType.actualTypeArguments
        //接口获取返回值的类型
        val resultType = typeArguments[0]
        //实例化MethodAnnotationInfo
        val annotationInfo = MethodAnnotationInfo()
        annotationInfo.resultType = resultType
        for (annotation in methodAnnotations) {
            if (annotation is GET) {
                annotationInfo.requestType = RequestType.GET
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is POST) {
                annotationInfo.requestType = RequestType.POST
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is DELETE) {
                annotationInfo.requestType = RequestType.DELETE
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is PUT) {
                annotationInfo.requestType = RequestType.PUT
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is PATCH) {
                annotationInfo.requestType = RequestType.PATCH
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is HEAD) {
                annotationInfo.requestType = RequestType.HEAD
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is OPTIONS) {
                annotationInfo.requestType = RequestType.OPTIONS
                var url: String = annotation.value
                if (annotation.useBaseUrl) {
                    url = baseUrl + url
                }
                annotationInfo.url = url
            } else if (annotation is Headers) {
                val headers: Array<String> = annotation.value as Array<String>
                if (headers.isNotEmpty()) {
                    for (header in headers) {
                        val split = header.split(":").toTypedArray()
                        require(split.size == 2) { "Designated header is not standard" }
                        annotationInfo.addHeader(split[0], split[1])
                    }
                } else {
                    throw NullPointerException("Headers is not null")
                }
            }
        }
        //解析方法中的参数注解
        val parameterAnnotations = method.parameterAnnotations
        if (parameterAnnotations.isNotEmpty()) {
            for (i in parameterAnnotations.indices) {
                val parameterAnnotation = parameterAnnotations[i]
                when (val annotation = parameterAnnotation[0]) {
                    is Header -> {
                        val key: String = annotation.value
                        annotationInfo.addHeader(key, args[i].toString() + "")
                    }
                    is Body -> {
                        annotationInfo.body = args[i].toString() + ""
                    }
                    is Param -> {
                        val key: String = annotation.value
                        annotationInfo.addParam(key, args[i].toString() + "")
                    }
                    is PathVariable -> {
                        annotationInfo.addPath(KVEntry(annotation.value, args[i].toString() + ""))
                    }
                }
            }
        }
        return annotationInfo
    }

    class Builder(val proxyHttpAdapter: ProxyHttpAdapter) {
        var baseUrl = ""
            private set

        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = if (TextUtils.isEmpty(baseUrl)) "" else baseUrl
            return this
        }

        fun build(): DynamicProxy {
            return DynamicProxy(this)
        }
    }

    init {
        baseUrl = builder.baseUrl
    }
}