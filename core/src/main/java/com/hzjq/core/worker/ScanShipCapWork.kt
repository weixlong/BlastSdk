package com.hzjq.core.worker

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class ScanShipCapWork : Work<CapEntity> {

    private var progress = 0

    private var isContainsNotMatchCap = false//是否包含不匹配的雷管数据

    private var callbackResult: Callback<CapResultEntity>? = null // 该值不为空时，在做方案的下传

    private var caps: MutableList<CapEntity>? = null

    constructor(callbackResult: Callback<CapResultEntity>?, callback: Callback<CapEntity>?) : super(
        callback
    ) {
        this.callbackResult = callbackResult
    }

    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            progress = args[0] as Int
            if (callbackResult != null) {
                caps = args[1] as MutableList<CapEntity>
            }
            if (callbackResult == null) {
                onProgressChanged(2 + progress * (48 / 100), "正在扫描雷管信息")
            } else {
                onProgressChanged(30 + progress * (10 / 100), "正在扫描雷管信息")
            }
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
                            if(callbackResult != null){
                                if(TextUtils.equals("05",progress.errorCode) || TextUtils.equals("07",progress.errorCode)){
                                    isContainsNotMatchCap = true
                                }
                            }
                            if (progress.stateCode == 0) {
                                if (progress.progress < 100) {
                                    if(callbackResult == null) {
                                        retry(progress.progress)
                                    } else {
                                        retry(progress.progress,caps!!)
                                    }
                                } else {
                                    if(callbackResult == null) {
                                        doNext(0)
                                    } else {
                                        doNext(0,callbackResult!!,caps!!,isContainsNotMatchCap)
                                    }
                                }
                            } else {
                                onProgressChanged(100, "扫描雷管信息失败")
                                callback?.onError(CapProgressEntity.convertStateCode(msg.stateCode))
                                onDestroy()
                            }

                        }

                        override fun failed() {
                            onProgressChanged(100, "扫描雷管信息失败")
                            callback?.onError(-12)
                            onDestroy()
                        }

                    })
            val msg = DataMessageBean(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd().cmd
            )
            BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
        }
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getCycleQueryCmd()
            )
    }

}