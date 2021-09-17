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
class InnerScanModeWork : Work<CapEntity> {

    private var caps: MutableList<CapEntity>? = null

    constructor(caps: MutableList<CapEntity>?,callback: Callback<CapEntity>?) : super(callback){
        this.caps = caps
    }

    override fun doWork(vararg args: Any) {
        if(caps.isNullOrEmpty()) {
            onProgressChanged(2, "正在进入扫描模式")
        } else {
            onProgressChanged(30, "正在进入扫描模式")
        }
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getScanCmd(),
                object : Receiver {
                    override fun convert(msg: String): Any {
                        return msg
                    }

                    override fun onSuccess(msg: Any) {
                        if(caps.isNullOrEmpty()) {
                            onProgressChanged(3, "进入扫描模式成功")
                            doNext(0)
                        } else {
                            onProgressChanged(30, "进入扫描模式成功")
                            doNext(0,caps!!)
                        }
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

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getScanCmd())
    }
}