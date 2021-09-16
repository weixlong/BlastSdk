package com.hzjq.core.callback


/**
 * 进度回调
 */
interface ProgressCallback<T> : Callback<T> {

    /**
     * 进度完成，回调onResult方法
     */

    /**
     * 进度改变
     * @param progress 当前进度
     * @param total 总进度
     * @param action 当前事件
     */
    fun onProgressChanged(progress:Int,total:Int,action:String)


    /**
     * 失败回调onError方法
     */
}