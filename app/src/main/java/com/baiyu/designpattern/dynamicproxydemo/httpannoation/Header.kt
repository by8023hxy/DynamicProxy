package com.baiyu.designpattern.dynamicproxydemo.httpannoation

@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Header(val value: String)