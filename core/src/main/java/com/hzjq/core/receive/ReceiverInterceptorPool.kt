package com.hzjq.core.receive

class ReceiverInterceptorPool private constructor(){

    private object B {
        val b = ReceiverInterceptorPool()
    }

    companion object {
        /**
         * 获取拦截器入口类，单例
         */
        fun getInstance(): ReceiverInterceptorPool {
            return B.b
        }
    }

    private val pools = arrayListOf<ReceiverInterceptor>()

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: ReceiverInterceptor){
        pools.add(interceptor)
    }

    internal fun getAllInterceptor():MutableList<ReceiverInterceptor>{
        return pools
    }

}