package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

/**
 * 清除芯片状态
 */
class ClearChipStateWork : Work<CapEntity> {

    constructor(callback: Callback<CapEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onProgressChanged(1,"正在清除底板状态")
        Receives.getInstance().registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getClearCmd(),object : Receiver{
            override fun convert(msg: String): Any {
                return msg
            }

            override fun onSuccess(msg: Any) {
                onProgressChanged(2,"正在清除底板状态成功")
                doNext()
            }

            override fun failed() {
                onProgressChanged(100,"正在清除底板状态失败")
                callback?.onError(-9)
                onDestroy()
            }
        })
        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getClearCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getClearCmd())
    }


}