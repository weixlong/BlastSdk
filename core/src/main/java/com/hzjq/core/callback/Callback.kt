package com.hzjq.core.callback

interface Callback<T> {


    /**
     * 结果回调
     */
    fun onResult(t:T)


    /**
     * 请对照错误码进行错误原因分析
     */
    fun onError(errorCode:Int)
}