package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work


/**
 * 进入扫描模式
 */
abstract class InnerScanModeWork : Work<CapEntity> {

    constructor(callback: Callback<CapEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        onDoWorkStart()
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getScanCmd(),
                object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        onInnerScanDoNext()
                    }

                    override fun failed() {
                        onProgressChanged(100,"进入扫描模式失败")
                        callback?.onError(-11)
                        onDestroy()
                    }

                })
        val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getScanCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }


    abstract fun onDoWorkStart()

    abstract fun onInnerScanDoNext()

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getScanCmd())
    }
}