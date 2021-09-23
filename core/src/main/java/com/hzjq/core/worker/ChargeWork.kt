package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class ChargeWork : Work<ChargeProgressEntity> {

    constructor(callback: Callback<ChargeProgressEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onProgressChanged(0,"进入充电模式")
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCmd()
                , object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        onProgressChanged(0,"进入充电模式成功")
                        doNext()
                    }

                    override fun failed() {
                        onProgressChanged(100,"进入充电模式失败")
                        callback?.onError(-53)
                        onDestroy()
                    }

                })

        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCmd())
    }
}