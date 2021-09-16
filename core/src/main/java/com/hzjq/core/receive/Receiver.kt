package com.hzjq.core.receive

interface Receiver {


    /**
     * 解析接收到的数据,子线程中
     */
     fun  convert(msg:String):Any


    /**
     * 匹配返回数据成功key，主线程
     */
     fun  onSuccess(msg:Any)

    /**
     * 对数据进行比较后，匹配到失败key，主线程
     */
    fun failed()
}