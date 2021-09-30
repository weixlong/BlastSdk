package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class CloseChargeWork : Work<Boolean> {

    constructor(callback: Callback<Boolean>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onProgressChanged(0,"正在充电")
        Receives.getInstance()
            .registerReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getClosePowerCmd()
                , object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        onProgressChanged(100,"下电成功")
                        callback?.onResult(true)
                    }

                    override fun failed() {
                        onProgressChanged(100,"下电失败")
                        callback?.onError(ErrorCode.getErrorResult(-12))
                        onDestroy()
                    }

                })

        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getClosePowerCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getClosePowerCmd())
    }
}