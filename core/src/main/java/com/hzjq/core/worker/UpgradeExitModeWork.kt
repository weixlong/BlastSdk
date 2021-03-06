package com.hzjq.core.worker

import com.hzjq.core.AckCode
import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class UpgradeExitModeWork : Work<Int> {

    private var targetVersion:Int = 0

    constructor(targetVersion:Int,callback: Callback<Int>) : super(callback){
        this.targetVersion = targetVersion
    }


    override fun doWork(vararg args: Any) {
        exitUpgradeMode()
    }


    /**
     * 退出升级模式
     */
    private fun exitUpgradeMode() {
        Receives.getInstance().registerReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getExitUpgradeModeCmd(targetVersion)
            , object : Receiver {
                override fun convert(msg: String): Any {
                    return BlastDelegate.getDelegate().getParseLoader().parseUpgradeModeMsg(msg)
                }

                override fun onSuccess(msg: Any) {
                    val success = msg as Boolean
                    if (success) {
                        onProgressChanged(100, "退出升级模式成功")
                        callback?.onResult(AckCode.EXIT_UPGRADE_MODE_OK)
                    } else {
                        onProgressChanged(100, "退出升级模式失败")
                        callback?.onError(ErrorCode.getErrorResult(-22))
                        onDestroy()
                    }
                }

                override fun failed() {
                    onProgressChanged(100, "退出升级模式失败")
                    callback?.onError(ErrorCode.getErrorResult(-22))
                    onDestroy()
                }
            })
        val msg = DataMessageBean(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getExitUpgradeModeCmd(targetVersion).cmd
        )
        BlastDelegate.getDelegate().getCmdExeLoader()
            .exePollResultCmd(msg.assembly(), callback)
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getExitUpgradeModeCmd(targetVersion)
        )
    }
}