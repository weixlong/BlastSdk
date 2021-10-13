package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work

class ClearOccupyWork : Work<Boolean> {

    constructor(callback: Callback<Boolean>?) : super(callback)

    override fun doWork(vararg args: Any) {
        Receives.getInstance().registerReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .sginOut0ccupy(),object : Receiver{
                override fun convert(msg: String): Any {
                    return msg
                }

                override fun onSuccess(msg: Any) {
                    callback?.onResult(true)
                }

                override fun failed() {
                    callback?.onResult(false)
                }

            }
        )
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver( BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .sginOut0ccupy())
    }
}