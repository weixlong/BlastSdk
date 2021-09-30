package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.AlongCapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class AlongCapCheckWork : Work<AlongCapResultEntity> {

    constructor(callback: Callback<AlongCapResultEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        Receives.getInstance().registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getAlongCheckCmd(),object : Receiver{
            override fun convert(msg: String): Any {
                return BlastDelegate.getDelegate().getParseLoader().parseAlongCap(msg)
            }

            override fun onSuccess(msg: Any) {
                if(msg is AlongCapResultEntity){
                    callback?.onResult(msg)
                }
            }

            override fun failed() {
                callback?.onError(ErrorCode.getErrorResult(-1))
            }

        })

        val msg = DataMessageBean( BlastDelegate.getDelegate().getAssemblyCmdLoader().getAlongCheckCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getAlongCheckCmd())
    }
}