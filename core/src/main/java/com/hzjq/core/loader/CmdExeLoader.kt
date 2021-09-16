package com.hzjq.core.loader

import com.hzjq.core.callback.Callback

interface CmdExeLoader : OnCancelLoader{


    /**
     * 执行单次命令
     */
    fun <T> exeOnceCmd(cmd:ByteArray,callback: Callback<T>?)


    /**
     * 执行轮询结果命令
     */
    fun <T> exePollResultCmd(cmd: ByteArray,callback: Callback<T>?)
}