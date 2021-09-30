package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

abstract class ScanShipCapWork : Work<CapEntity> {

    internal var progress = 0


    constructor( callback: Callback<CapEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            progress = args[0] as Int
            onDoWorkStart(*args)
            Receives.getInstance()
                .registerReceiver(
                    BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd(),
                    object : Receiver {
                        override fun convert(msg: String): Any {
                            return BlastDelegate.getDelegate().getParseLoader()
                                .parseScanProgress(msg)
                        }

                        override fun onSuccess(msg: Any) {
                            val progress = msg as CapProgressEntity
                            onScanShipSuccess(progress)
                        }

                        override fun failed() {
                            onProgressChanged(100, "扫描雷管信息失败")
                            callback?.onError(ErrorCode.getErrorResult(-20))
                            onDestroy()
                        }

                    })
            val msg = DataMessageBean(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd().cmd
            )
            BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
        }
    }


    internal fun onScanError(msg:CapProgressEntity){
        if(ErrorCode.getRetryResult(msg.stateCode) == null) {
            onProgressChanged(100, "扫描雷管信息失败")
            callback?.onError(ErrorCode.getErrorResult(msg.stateCode))
            onDestroy()
        } else {
            val result = ErrorCode.getRetryResult(msg.stateCode)
            onProgressChanged(progress, result!!.errorAction)
        }
    }


    abstract fun onDoWorkStart(vararg args: Any)


    abstract fun onScanShipSuccess(msg:CapProgressEntity)

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd()
            )
    }

}