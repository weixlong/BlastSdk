package com.hzjq.core.work

import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnCancelLoader

abstract class Work<T> : OnCancelLoader {

    internal var callback: Callback<T>? = null

    private var works: Works? = null

    private var progress = 0

    constructor(callback: Callback<T>?) {
        this.callback = callback
    }

    internal fun setWorks(works: Works) {
        this.works = works
    }

    abstract fun doWork(vararg args: Any)

    internal fun onDestroy() {
        works?.onDestroy()
        callback = null
    }

    /**
     * 强转为进度回调
     */
    private fun <T> castProgressCallback(): ProgressCallback<T> {
        return callback as ProgressCallback<T>
    }

    /**
     * 进度变化
     */
    internal fun onProgressChanged(progress:Int,action:String){
        if(callback is ProgressCallback){
            this.progress = progress
           castProgressCallback<T>().onProgressChanged(progress, 100, action)
        }
    }

    /**
     * 设置新的进度，total叠加,progress叠加
     */
    internal fun onAddProgressChanged(progress:Int,total:Int,action:String){
        if(callback is ProgressCallback){
            this.progress += progress
            castProgressCallback<T>().onProgressChanged(progress, 100+total, action)
        }
    }
    /**
     * 执行下一个任务
     */
    fun doNext(vararg args: Any) {
        works?.doNextWork(*args)
    }

    /**
     * 执行指定的任务
     */
    fun <work : Work<Any>> todoWork(clazz: Class<work>, vararg args: Any) {
        works?.doTargetWork(clazz, *args)
    }

    /**
     * 重新执行本任务
     */
    fun retry(vararg args: Any) {
        works?.retryCurWork(*args)
    }
}