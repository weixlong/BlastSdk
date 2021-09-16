package com.hzjq.core.receive

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.cmd.Cmd

class Receives private constructor() {

    private val receives = HashMap<Cmd, Receiver>()

    private object B {
        val b = Receives()
    }

    companion object {
        /**
         * 获取起爆入口类，单例
         */
        fun getInstance(): Receives {
            return B.b
        }
    }

    /**
     * 找到对应的接收者并执行
     */
    fun findReceiverDoWork(msg: String) {
        receives.forEach {
            val cmd = findReceiverCmd(msg, it.key, it.value)
            if (cmd) {
                receives.remove(it.key)
                BlastDelegate.getDelegate().getCmdExeLoader().cancel()
                return@forEach
            }
            val cmdKey = findReceiverCmdKey(msg, it.key, it.value)
            if (cmdKey) {
                receives.remove(it.key)
                BlastDelegate.getDelegate().getCmdExeLoader().cancel()
                return@forEach
            }
        }
    }


    /**
     * 注册接收者
     */
    fun registerReceiver(cmd: Cmd,receiver: Receiver){
        receives[cmd] = receiver
    }


    /**
     * 取消注册接收者
     */
    fun unRegisterReceiver(cmd: Cmd){
        receives.remove(cmd)
    }


    /**
     *  匹配cmd命令
     */
    private fun findReceiverCmd(msg: String, cmd: Cmd, receiver: Receiver): Boolean {
        if (!cmd.keyOk.isNullOrEmpty()) {
            if (msg.startsWith(cmd.keyOk)) {
                val any = receiver.convert(msg)
                BlastDelegate.getDelegate().post(Runnable {
                    receiver.onSuccess(any)
                })
                return true
            }
        } else if(!cmd.keyError.isNullOrEmpty()){
            if (msg.startsWith(cmd.keyError)) {
                BlastDelegate.getDelegate().post(Runnable {
                    receiver.failed()
                })
                return true
            }
        }
        return false
    }


    /**
     * 匹配cmd里的key值
     */
    private fun findReceiverCmdKey(msg: String, cmd: Cmd, receiver: Receiver): Boolean {
        if(cmd.key != null){
            if(msg.length >= cmd.key!!.endIndex){
                if(msg.startsWith(cmd.key!!.start)){
                    val key = msg.substring(cmd.key!!.startIndex,cmd.key!!.endIndex)
                    if(TextUtils.equals(key,cmd.key!!.key)){
                        val any = receiver.convert(msg)
                        BlastDelegate.getDelegate().post(Runnable {
                            receiver.onSuccess(any)
                        })
                        return true
                    }
                }
            }
        }
        return false
    }



}