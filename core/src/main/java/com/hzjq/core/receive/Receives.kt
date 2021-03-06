package com.hzjq.core.receive

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.cmd.Cmd
import com.hzjq.core.cmd.CmdExeImpl
import com.hzjq.core.massage.DataMessageBean

class Receives private constructor() {

    private val receives = HashMap<Cmd, Receiver>()

    private var willRemoveReceiver = arrayListOf<Cmd>()

    private val willAddReceiver = HashMap<Cmd, Receiver>()

    private var lock = false

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
    @Synchronized
    fun findReceiverDoWork(msg: String) {
        try {
            if (!exeInterceptor(msg)) {
                checkAddAndRemoveCmd()
                receives.forEach {
                    val cmd = findReceiverCmd(msg, it.key, it.value)
                    if (cmd) {
                        unRegisterReceiver(it.key)
                        return@forEach
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /**
     * 检查是否有新注册的或者已被标记移除的cmd
     */
    private fun checkAddAndRemoveCmd(){
        if(willAddReceiver.isNotEmpty()){
            receives.putAll(willAddReceiver)
            willAddReceiver.clear()
        }
        if(willRemoveReceiver.isNotEmpty()){
            willRemoveReceiver.forEach {
                receives.remove(it)
            }
            willRemoveReceiver.clear()
        }
    }


    /**
     * 执行拦截器
     */
    private fun exeInterceptor(msg: String): Boolean {
        val list = ReceiverInterceptorPool.getInstance().getAllInterceptor()
        list.forEach {
            if (!TextUtils.isEmpty(msg) && msg.length >= 30) {
                val stateCode = Integer.valueOf(msg.substring(28, 30), 16)
                val b = it.onInterceptor(stateCode)
                if (b) return true
            }
        }
        return false
    }

    /**
     * 注册接收者
     */
    fun registerReceiver(cmd: Cmd, receiver: Receiver) {
        willAddReceiver[cmd] = receiver
        if(willRemoveReceiver.contains(cmd)){
            willRemoveReceiver.remove(cmd)
        }
    }


    /**
     * 取消注册接收者
     */
    fun unRegisterReceiver(cmd: Cmd) {
        willRemoveReceiver.add(cmd)
    }


    /**
     *  匹配cmd命令
     */
    private fun findReceiverCmd(msg: String, cmd: Cmd, receiver: Receiver): Boolean {
        if (!cmd.keyOk.isNullOrEmpty()) {
            if (msg.startsWith(cmd.keyOk)) {
                BlastDelegate.getDelegate().getCmdExeLoader().cancel()
                return onCallbackCmdReceiver(msg, cmd, receiver)
            }
        }
        if (!cmd.keyError.isNullOrEmpty()) {
            if (msg.startsWith(cmd.keyError)) {
                BlastDelegate.getDelegate().getCmdExeLoader().cancel()
                BlastDelegate.getDelegate().post(Runnable {
                    receiver.failed()
                })
                return true
            }
        }
        return findReceiverCmdKey(msg, cmd, receiver)
    }


    /**
     * 匹配cmd里的key值
     */
    private fun findReceiverCmdKey(msg: String, cmd: Cmd, receiver: Receiver): Boolean {
        if (cmd.key != null) {
            if (msg.length >= cmd.key!!.endIndex) {
                if (msg.startsWith(cmd.key!!.start)) {
                    val key = msg.substring(cmd.key!!.startIndex, cmd.key!!.endIndex)
                    if (TextUtils.equals(key, cmd.key!!.key)) {
                        BlastDelegate.getDelegate().getCmdExeLoader().cancel()
                        return onCallbackCmdReceiver(msg, cmd, receiver)
                    }
                }
            }
        }
        return false
    }


    /**
     * 回调这个指令
     */
    private fun onCallbackCmdReceiver(msg: String, cmd: Cmd, receiver: Receiver): Boolean {
        return if (retryCmdByAckLessThenMinLength(msg, cmd)) {
            lock = true
            false
        } else {
            val any = receiver.convert(msg)
            BlastDelegate.getDelegate().post(Runnable {
                receiver.onSuccess(any)
            })
            true
        }
    }

    /**
     * 如果ack长度小于最小长度时重试
     */
    private fun retryCmdByAckLessThenMinLength(ack: String, cmd: Cmd): Boolean {
        if (cmd.minAckLength > 0 && ack.length < cmd.minAckLength) {
            val loader = BlastDelegate.getDelegate().getCmdExeLoader()
            if (loader is CmdExeImpl) {
                val msg = DataMessageBean(cmd.cmd)
                loader.retryCmdByAckLessThenMinLength(msg.assembly())
                return true
            }
        }
        return false
    }

    /**
     * 执行失败
     */
    fun onCallbackCmdFailed(cmd: ByteArray) {
        receives.forEach {
            if (cmd.contentEquals(DataMessageBean(it.key.cmd).assembly())) {
                it.value.failed()
                return@forEach
            }
        }
    }

}