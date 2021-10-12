package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
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

    private var caps: MutableList<CapEntity>?=null

    constructor(caps: MutableList<CapEntity>?,callback: Callback<CapEntity>?) : super(callback){
        this.caps = caps
    }

    override fun doWork(vararg args: Any) {
        onProgressChanged(1,"正在清除底板数据")
        Receives.getInstance().registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getClearCmd(),object : Receiver{
            override fun convert(msg: String): Any {
                return msg
            }

            override fun onSuccess(msg: Any) {
                onProgressChanged(2,"清除底板数据成功")
                if(caps == null) {
                    doNext()
                } else {
                    doNext(caps!!)
                }
            }

            override fun failed() {
                onProgressChanged(100,"清除底板数据失败")
                callback?.onError(ErrorCode.getErrorResult(-11))
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