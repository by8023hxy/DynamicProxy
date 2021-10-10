package com.baiyu.designpattern.dynamicproxydemo.proxy

import com.baiyu.designpattern.dynamicproxydemo.httpannoation.POST
import com.baiyu.designpattern.dynamicproxydemo.httpannoation.PathVariable

/**
 *
 */
interface NetService {
    /**
     * 测试
     */
    @POST(value = "topic_post/{postInfo}?uid={uid}&tid={tid}")
    fun test(
        @PathVariable("postInfo") postInfo: String?,
        @PathVariable("uid") uid: String?,
        @PathVariable("tid") username: String?
    ): Call<String>
}