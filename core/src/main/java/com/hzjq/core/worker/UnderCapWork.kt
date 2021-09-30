package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
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

    constructor(
        callback: Callback<CapResultEntity>?
    ) : super(callback)

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

        onProgressChanged((1 + position * 29f / caps!!.size).toInt(), "正在下传雷管信息")

        if (position < caps!!.size) {
            doUnderCap()
        } else {
            onProgressChanged((1 + position * 29f / caps!!.size).toInt(), "已完成下传雷管信息")
            doNext(0,caps!!)
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
                    onProgressChanged(100, "下传雷管信息失败")
                    callback?.onError(ErrorCode.getErrorResult(-21))
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