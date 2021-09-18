package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class BlastQueryWork : Work<CapResultEntity> {

    private var blastTime = 0L

    constructor(callback: Callback<CapResultEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if(args.size >= 2) {
            val isSuccess = args[0] as Boolean
            blastTime = args[1] as Long
            if(isSuccess) {
                Receives.getInstance()
                    .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd(),
                        object : Receiver {
                            override fun convert(msg: String): Any {
                                return BlastDelegate.getDelegate().getParseLoader()
                                    .parseScanProgress(msg)
                            }

                            override fun onSuccess(msg: Any) {
                                if (msg is CapProgressEntity) {
                                    if (msg.progress < 100) {
                                        if(blastTime+BlastDelegate.getDelegate().getBlastOutTime() < System.currentTimeMillis()){
                                            onProgressChanged(50,"查询起爆结果超时")
                                            callback?.onError(-57)
                                            onDestroy()
                                        } else {
                                            if(msg.stateCode == 0) {
                                                onProgressChanged(msg.progress / 2, "正在查询起爆结果")
                                                retry(isSuccess, blastTime)
                                            } else {
                                                onProgressChanged(msg.progress / 2, "查询起爆结果错误")
                                                callback?.onError(CapProgressEntity.convertBlastError(msg.stateCode))
                                                doNext(false)
                                            }
                                        }
                                    } else {
                                        onProgressChanged(50, "起爆成功")
                                        doNext(true)
                                    }
                                }
                            }

                            override fun failed() {

                            }

                        })

                val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                    .getCycleQueryCmd().cmd)
                BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
            } else {
                doNext(false)
            }
        }
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getCycleQueryCmd())
    }
}