package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class BlastWork : Work<CapResultEntity> {

    constructor(callback: Callback<CapResultEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getBlastCmd(),
                object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        doNext(true,System.currentTimeMillis())
                    }

                    override fun failed() {
                        onProgressChanged(100,"爆破失败")
                        callback?.onError(-56)
                        doNext(false,System.currentTimeMillis())
                    }

                })

        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getBlastCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getBlastCmd())
    }
}