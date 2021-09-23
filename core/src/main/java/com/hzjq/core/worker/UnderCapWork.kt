package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work


/**
 * 下传雷管
 */
class UnderCapWork : Work<CapResultEntity> {

    private var caps: MutableList<CapEntity>? = null
    private var position = 0
    private var isUnderOnly = true

    constructor(
        isUnderOnly: Boolean,
        callback: Callback<CapResultEntity>?
    ) : super(callback) {
        this.isUnderOnly = isUnderOnly
    }

    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            caps = args[0] as MutableList<CapEntity>
            underCap()
        } else {
            onDestroy()
        }
    }

    /**
     * 下传雷管信息
     */
    private fun underCap() {
        if (caps.isNullOrEmpty()) {
            onDestroy()
            return
        }
        if (isUnderOnly) {
            onProgressChanged(1 + position * 99 / caps!!.size, "正在下传雷管信息")
        } else {
            onProgressChanged((1 + position * 29f / caps!!.size).toInt(), "正在下传雷管信息")
        }
        if (position < caps!!.size) {
            doUnderCap()
        } else {
            if (isUnderOnly) {
                onProgressChanged(100, "已完成下传雷管信息")
                val t = CapResultEntity()
                callback?.onResult(t)
            } else {
                onProgressChanged((1 + position * 29f / caps!!.size).toInt(), "已完成下传雷管信息")
                doNext(0,caps!!)
            }
        }
    }

    private fun doUnderCap() {
        Receives.getInstance().registerReceiver(BlastDelegate.getDelegate()
            .getAssemblyCmdLoader().getUnderCapCmd(position, caps!!),
            object : Receiver {
                override fun convert(msg: String): Any {
                    return msg
                }

                override fun onSuccess(msg: Any) {
                    position++
                    underCap()
                }

                override fun failed() {
                    onProgressChanged(100, "正在下传雷管信息失败")
                    callback?.onError(-15)
                    onDestroy()
                }

            })
        val msg = DataMessageBean(
            BlastDelegate.getDelegate()
                .getAssemblyCmdLoader().getUnderCapCmd(position, caps!!).cmd
        )
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
    }

    override fun cancel() {
        if(!caps.isNullOrEmpty()) {
            Receives.getInstance().unRegisterReceiver(
                BlastDelegate.getDelegate()
                    .getAssemblyCmdLoader().getUnderCapCmd(if(position >= caps!!.size) caps!!.size -1 else position, caps!!)
            )
        }
    }
}