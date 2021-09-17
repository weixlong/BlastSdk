package com.hzjq.core.worker

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work


/**
 * 读雷管数据
 */
class ReadCapWork : Work<CapEntity> {

    private var progress = 0
    private var count = 0

    private var isContainsNotMatchCap = false//是否包含不匹配的雷管数据

    private var callbackResult: Callback<CapResultEntity>? = null // 该值不为空时，在做方案的下传

    private var caps: MutableList<CapEntity>? = null

    private var resultCaps: MutableList<CapEntity> = arrayListOf()

    constructor(callback: Callback<CapEntity>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if (args.isNotEmpty()) {
            count = args[0] as Int
            if (args.size >= 4) {
                callbackResult = args[1] as Callback<CapResultEntity>
                caps = args[2] as MutableList<CapEntity>
                isContainsNotMatchCap = args[3] as Boolean
            }
            if (callbackResult == null) {
                onProgressChanged(50 + progress, "正在读取雷管信息")
            } else {
                onProgressChanged(40 + progress, "正在读取雷管信息")
            }
            Receives.getInstance()
                .registerReceiver(
                    BlastDelegate.getDelegate().getAssemblyCmdLoader().getReadCapCmd(count),
                    object : Receiver {
                        override fun convert(msg: String): Any {
                            if (msg.length > 40) {
                                return BlastDelegate.getDelegate().getParseLoader()
                                    .parseCap(msg)
                            }
                            return msg
                        }

                        override fun onSuccess(msg: Any) {
                            if (msg is CapEntity) {
                                checkMaxLimitCap(msg)
                            } else {
                                failed()
                            }
                        }

                        override fun failed() {
                            onProgressChanged(100, "读取雷管信息失败")
                            callback?.onError(-13)
                            onDestroy()
                        }

                    })
            val msg = DataMessageBean(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getReadCapCmd(count).cmd
            )
            BlastDelegate.getDelegate().getCmdExeLoader().exePollResultCmd(msg.assembly(), callback)
        }
    }


    private fun checkMaxLimitCap(cap: CapEntity) {
        if (callbackResult == null) {
            callback?.onResult(cap)
        } else {
            if(isContainsNotMatchCap) {
                if (TextUtils.equals("0", cap.status.toCharArray()[5].toString())) {
                    resultCaps.add(cap)
                } else if (TextUtils.equals("0", cap.status.toCharArray()[6].toString())) {
                    resultCaps.add(cap)
                }
            }
        }
        if (count + 1 >= BlastDelegate.getDelegate().getMaxSupportCapCount()) {
            onProgressChanged(100, "已达到雷管支持最大数")
            checkNotMatchCap()
            onDestroy()
        } else if (count + 1 >= cap.total) {
            onProgressChanged(100, "已读取全部雷管信息")
            checkNotMatchCap()
            onDestroy()
        } else {
            if (callbackResult == null) {
                progress = (count + 1) * (50 / cap.total)
                retry(++count)
            } else {
                progress = (count + 1) * (10 / cap.total)
                retry(++count, callbackResult!!, caps!!, isContainsNotMatchCap)
            }
        }
    }

    private fun checkNotMatchCap() {
        if (callbackResult != null) {
            val resultEntity = CapResultEntity()
            resultEntity.caps = caps
            resultEntity.errorCode = if (isContainsNotMatchCap) 0 else -1
            resultEntity.resultCaps = resultCaps
            callbackResult?.onResult(resultEntity)
        }
    }

    override fun cancel() {
        Receives.getInstance()
            .unRegisterReceiver(
                BlastDelegate.getDelegate().getAssemblyCmdLoader().getReadCapCmd(count)
            )
    }

}