package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class ChargeQueryWork : Work<ChargeProgressEntity> {

    constructor(callback: Callback<ChargeProgressEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        Receives.getInstance()
            .registerReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCycleQueryCmd()
                , object : Receiver {
                    override fun convert(msg: String): Any {
                        return BlastDelegate.getDelegate().getParseLoader().parseScanProgress(msg)
                    }

                    override fun onSuccess(msg: Any) {
                        if (msg is CapProgressEntity) {
                            if (msg.mElectric == 0.0 && msg.mVoltage == 0.0) {//充电异常
                                doNext(0)//查询雷管数据
                                return
                            }

//                            if(msg.stateCode == 0){
                            if (msg.progress < 100) {
                                onProgressChanged(msg.progress / 2, "正在充电")
                                retry()
                            } else {
                                doNext(0)//查询雷管数据
                            }
//                            } else {
//                                onProgressChanged(100,"查询充电结果失败")
//                                callback?.onError(CapProgressEntity.convertStateCode(msg.stateCode))
//                                onDestroy()
//                            }
                        }

                    }

                    override fun failed() {
                        onProgressChanged(100, "充电失败")
                        callback?.onError(ErrorCode.getErrorResult(-7))
                        onDestroy()
                    }

                })

        val msg = DataMessageBean(
            BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCycleQueryCmd().cmd
        )
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getChargeCycleQueryCmd()
            )
    }
}