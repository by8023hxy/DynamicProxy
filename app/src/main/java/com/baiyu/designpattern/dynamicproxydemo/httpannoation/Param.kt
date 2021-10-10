package com.baiyu.designpattern.dynamicproxydemo.httpannoation

@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Param(val value: String)