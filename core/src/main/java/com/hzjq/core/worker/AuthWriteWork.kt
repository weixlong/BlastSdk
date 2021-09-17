package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class AuthWriteWork : Work<CapProgressEntity> {

    constructor(callback: Callback<CapProgressEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onProgressChanged(50,"正在写入授权信息")
        Receives.getInstance().registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getWriteCapDelayCmd(BlastDelegate.getDelegate().isDelayWriteData()),
            object : Receiver {
                override fun convert(msg: String): Any {
                    return msg
                }

                override fun onSuccess(msg: Any) {
                    onProgressChanged(50,"正在写入授权信息成功")
                    doNext()
                }

                override fun failed() {
                    onProgressChanged(100,"正在写入授权信息失败")
                    callback?.onError(-51)
                    onDestroy()
                }

            })

        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getWriteCapDelayCmd(BlastDelegate.getDelegate().isDelayWriteData()).cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)

    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getWriteCapDelayCmd(BlastDelegate.getDelegate().isDelayWriteData()))
    }
}