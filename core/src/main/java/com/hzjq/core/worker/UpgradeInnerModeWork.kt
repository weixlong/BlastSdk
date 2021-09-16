package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class UpgradeInnerModeWork : Work<Int> {

    constructor(callback: Callback<Int>) : super(callback)


    override fun doWork(vararg args: Any) {
        innerUpgradeMode()
    }


    /**
     * 进入升级模式
     */
    private fun innerUpgradeMode() {
        Receives.getInstance().registerReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getInnerUpgradeModeCmd()
            , object : Receiver {
                override fun convert(msg: String): Any {
                    return BlastDelegate.getDelegate().getParseLoader().parseUpgradeModeMsg(msg)
                }

                override fun onSuccess(msg: Any) {
                    val success = msg as Boolean
                    if (success) {
                        onAddProgressChanged(5, 100, "进入升级模式成功")
                        doNext()
                    } else {
                        onProgressChanged(100, "进入升级模式失败")
                        callback?.onError(-6)
                        onDestroy()
                    }
                }

                override fun failed() {
                    onProgressChanged(100, "进入升级模式失败")
                    callback?.onError(-6)
                    onDestroy()
                }
            })
        val msg = DataMessageBean(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getInnerUpgradeModeCmd().cmd
        )
        BlastDelegate.getDelegate().getCmdExeLoader()
            .exePollResultCmd(msg.assembly(), callback)
    }


    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getInnerUpgradeModeCmd()
        )
    }
}