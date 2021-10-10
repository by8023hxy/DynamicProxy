package com.baiyu.designpattern.dynamicproxydemo.proxy

class BaseResponse<T> {
    val code = 0
    val msg: String? = null
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }
}