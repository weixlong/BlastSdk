package com.hzjq.core.receive

interface ReceiverInterceptor {

    /**
     * 返回true则被拦截，不往下继续传出，运行在子线程中
     * @param stateCode  254时发生了短路
     */
    fun onInterceptor(stateCode:Int):Boolean
}