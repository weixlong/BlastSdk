package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class VersionWork : Work<Int> {

    constructor(callback: Callback<Int>?) : super(callback)


    override fun doWork(vararg args: Any) {
        onProgressChanged(98, "正在获取当前版本")
        Receives.getInstance()
            .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getVersionCmd(),
                object : Receiver {
                    override fun convert(msg: String): Any {
                        return BlastDelegate.getDelegate().getParseLoader().parseVersionMsg(msg)
                    }

                    override fun onSuccess(msg: Any) {
                        val version = msg as Int
                        if (version == 0) {
                            onProgressChanged(100,"正在获取当前版本失败")
                            callback?.onError(-5)
                        } else {
                            onProgressChanged(100,"正在获取当前版本成功")
                            callback?.onResult(version)
                        }
                        onDestroy()
                    }

                    override fun failed() {
                        callback?.onError(-5)
                        onDestroy()
                    }
                })
        val msg =
            DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader().getVersionCmd().cmd)
        BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader().getVersionCmd())
    }
}