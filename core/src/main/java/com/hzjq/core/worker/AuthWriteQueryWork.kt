package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class AuthWriteQueryWork : Work<CapProgressEntity> {

    private var progress = 0

    constructor(callback: Callback<CapProgressEntity>?) : super(callback)


    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            progress = args[0] as Int
            onProgressChanged(progress, "正在进行授权查询")
            Receives.getInstance()
                .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                    .getAuthCycleQueryCmd(),
                    object : Receiver {
                        override fun convert(msg: String): Any {
                            return BlastDelegate.getDelegate().getParseLoader()
                                .parseScanProgress(msg)
                        }

                        override fun onSuccess(msg: Any) {
                            if (msg is CapProgressEntity) {
                                if (msg.stateCode == 0) {
                                    if (msg.progress < 100) {
                                        callback?.onResult(msg)
                                        progress = 51 + msg.progress * 49 / 100
                                        retry(progress)
                                    } else {
                                        msg.isEnd = true
                                        callback?.onResult(msg)
                                    }
                                } else {
                                    onProgressChanged(100, "授权查询失败")
                                    callback?.onError(CapProgressEntity.convertStateCode(msg.stateCode))
                                    onDestroy()
                                }
                            }
                        }

                        override fun failed() {
                            onProgressChanged(100, "授权查询失败")
                            callback?.onError(-52)
                            onDestroy()
                        }

                    })

            val msg = DataMessageBean(
                BlastDelegate.getDelegate().getAssemblyCmdLoader()
                    .getAuthCycleQueryCmd().cmd
            )
            BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
        }
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getAuthCycleQueryCmd())
    }


}