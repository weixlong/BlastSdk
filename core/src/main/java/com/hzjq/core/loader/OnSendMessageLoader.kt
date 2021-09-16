package com.hzjq.core.loader

import com.hzjq.core.callback.Callback

interface OnSendMessageLoader {

    /**
     * 确保串口已经打开
     */
    fun <T> makeSurePortOpen(openCallback:Callback<Boolean>,callback:Callback<T>?)

    /**
     * 发送数据
     */
    fun sendData(msg:ByteArray)
}