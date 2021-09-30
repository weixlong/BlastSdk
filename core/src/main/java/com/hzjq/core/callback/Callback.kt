package com.hzjq.core.callback

import com.hzjq.core.bean.ErrorResult

interface Callback<T> {


    /**
     * 结果回调
     */
    fun onResult(t:T)

    /**
     * 重试次数变化
     */
    fun onRetryCountChanged(retryCount:Int,action: String)

    /**
     * 请对照错误码进行错误原因分析
     */
    fun onError(errorCode: ErrorResult)
}