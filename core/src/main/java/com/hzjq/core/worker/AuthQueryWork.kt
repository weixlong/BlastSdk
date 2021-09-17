package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

/**
 * 授权进度查询
 */
class AuthQueryWork : Work<CapProgressEntity> {

    private var progress = 0

    constructor(callback: Callback<CapProgressEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            progress = args[0] as Int
            onProgressChanged(progress, "正在扫描授权信息")
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
                                    callback?.onResult(msg)
                                    if (msg.progress < 100) {
                                        progress = 1 + msg.progress * 50 / 100
                                        retry(progress)
                                    } else {
                                        doNext(progress)
                                    }
                                } else {
                                    onProgressChanged(100, "扫描授权信息失败")
                                    callback?.onError(CapProgressEntity.convertStateCode(msg.stateCode))
                                    onDestroy()
                                }
                            }
                        }

                        override fun failed() {
                            onProgressChanged(100, "扫描授权信息失败")
                            callback?.onError(-50)
                            onDestroy()
                        }

                    })

            val msg =DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getAuthCycleQueryCmd().cmd)
            BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(),callback)
        }
    }

    override fun cancel() {
        progress = 0
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getAuthCycleQueryCmd())
    }
}