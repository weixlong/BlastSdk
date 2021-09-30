package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class InnerAuthModeWork : Work<CapProgressEntity> {

    constructor(callback: Callback<CapProgressEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onProgressChanged(0, "正在进入授权模式")
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getAuthCmd(),
                object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        onProgressChanged(1, "进入授权成功")
                        doNext(1)
                    }

                    override fun failed() {
                        onProgressChanged(100, "进入授权模式失败")
                        callback?.onError(ErrorCode.getErrorResult(-13))
                        onDestroy()
                    }

                })

        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getAuthCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getAuthCmd())
    }
}