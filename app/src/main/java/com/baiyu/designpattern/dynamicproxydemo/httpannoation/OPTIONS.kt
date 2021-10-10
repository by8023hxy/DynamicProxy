package com.baiyu.designpattern.dynamicproxydemo.httpannoation

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class OPTIONS(val value: String = "", val useBaseUrl: Boolean = true)