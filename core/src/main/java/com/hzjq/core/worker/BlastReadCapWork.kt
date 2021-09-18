package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class BlastReadCapWork : Work<CapResultEntity> {

    private var position = 0
    private val cre = CapResultEntity()
    private val caps = arrayListOf<CapEntity>()

    constructor(callback: Callback<CapResultEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if(args.isNotEmpty()){
            val success = args[0] as Boolean
            Receives.getInstance().registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getReadCapCmd(position),
            object : Receiver{
                override fun convert(msg: String): Any {
                    return BlastDelegate.getDelegate().getParseLoader().parseCap(msg)
                }

                override fun onSuccess(msg: Any) {
                    if(msg is CapEntity){
                        caps.add(msg)
                        onProgressChanged((50+(position*1.0/msg.total)*50).toInt(),"正在查询起爆雷管数据")
                        if(position < msg.total){
                            position ++
                            retry(success)
                        } else {
                            if(success){
                                cre.caps = caps
                                cre.errorCode = 0
                            } else {
                                cre.resultCaps = caps
                                cre.errorCode = -2
                            }
                            callback?.onResult(cre)
                            onDestroy()
                        }
                    }
                }

                override fun failed() {

                }

            })
        }
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getReadCapCmd(position))
    }
}