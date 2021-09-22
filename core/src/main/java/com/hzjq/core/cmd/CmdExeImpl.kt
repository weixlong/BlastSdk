package com.hzjq.core.cmd

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.CmdExeLoader

class CmdExeImpl : CmdExeLoader {

    private val openPortCallback = OpenPortCallback()
    private var callback: Callback<Any>? = null
    private var retryCount = 0
    private val SEND_MSG_WHAT = 0x006785
    private val handler = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            if(msg.what == SEND_MSG_WHAT) {
                val cmd = msg.obj as ByteArray
                if (retryCount > 0) {
                    if (retryCount > BlastDelegate.getDelegate().getRetryCount()) {
                        callback?.onError(-10)
                        cancel()
                        return
                    }
                    callback?.onRetryCountChanged(retryCount, "正在重试")
                }
                retryCount++
                BlastDelegate.getDelegate().getOnSendMessageLoader().sendData(cmd)
                startInterval(true,cmd)
            }
        }
    }

    override fun <T> exeOnceCmd(cmd: ByteArray, callback: Callback<T>?) {
        cancel()
        this.callback = callback as Callback<Any>
        openPortCallback.isPoll = false
        openPortCallback.cmd = cmd
        BlastDelegate.getDelegate().getOnSendMessageLoader()
            .makeSurePortOpen(openPortCallback, callback)
    }


    override fun <T> exePollResultCmd(cmd: ByteArray, callback: Callback<T>?) {
        cancel()
        this.callback = callback as Callback<Any>
        openPortCallback.isPoll = true
        openPortCallback.cmd = cmd
        retryCount = 0
        BlastDelegate.getDelegate().getOnSendMessageLoader()
            .makeSurePortOpen(openPortCallback, callback)
    }


    /**
     * 开始轮询结果
     */
    private fun startInterval(isInterval:Boolean,cmd: ByteArray){
        cancel()
        val message = Message.obtain()
        message.what = SEND_MSG_WHAT
        message.obj = cmd
        if(isInterval) {
            handler.sendMessageDelayed(message, BlastDelegate.getDelegate().getReceiveOutTime())
        } else {
            handler.sendMessage(message)
        }
    }


    private inner class OpenPortCallback : Callback<Boolean> {
        var isPoll = false
        lateinit var cmd: ByteArray
        override fun onResult(t: Boolean) {
            if(t){
                if(!isPoll){
                    BlastDelegate.getDelegate().getOnSendMessageLoader().sendData(cmd)
                } else {
                    startInterval(false,cmd)
                }
            }
        }

        override fun onError(errorCode: Int) {

        }

        override fun onRetryCountChanged(retryCount: Int, action: String) {

        }
    }

    override fun cancel() {
        retryCount = 0
        handler.removeMessages(SEND_MSG_WHAT)
    }
}