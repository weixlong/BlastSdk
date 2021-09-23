package com.hzjq.core.worker

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work
import java.util.*


/**
 * 写入一个扇区结束
 */
class UpgradeWriteSectorEndWork : Work<Int> {

    constructor(callback: Callback<Int>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if (args.size >= 3) {
            val mSectorDataList = args[0] as ArrayList<String>// 扇区数据
            val mSectorAddrList = args[1] as ArrayList<String>// 扇区地址
            var position = args[2] as Int
            Receives.getInstance()
                .registerReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                    .getUpgradeWriteSectorCmd(), object : Receiver {
                    override fun convert(msg: String): Any {
                        if (msg.length >= 14) {
                            val state: String = msg.substring(8, 10)
                            return TextUtils.equals("81", state)
                        }
                        return false
                    }

                    override fun onSuccess(msg: Any) {
                        val isOk = msg as Boolean
                        if(isOk){
                            todoWork(UpgradeSendAddressWork::class.java as Class<Work<Any>>,mSectorDataList,mSectorAddrList,++position)
                        } else {
                            todoWork(UpgradeSendAddressWork::class.java as Class<Work<Any>>,mSectorDataList,mSectorAddrList,position)
                        }
                    }

                    override fun failed() {

                    }
                })

            val msg = DataMessageBean(BlastDelegate.getDelegate().getAssemblyCmdLoader()
                    .getUpgradeWriteSectorCmd().cmd)
            BlastDelegate.getDelegate().getCmdExeLoader()
                .exePollResultCmd(msg.assembly(), callback)
        }
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(BlastDelegate.getDelegate().getAssemblyCmdLoader()
            .getUpgradeWriteSectorCmd())
    }

}