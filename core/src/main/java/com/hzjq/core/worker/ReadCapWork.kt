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

    private var missCaps: MutableList<CapEntity> = arrayListOf() //errorCode:-1失败时，漏接数据

   private var meetCaps: MutableList<CapEntity> = arrayListOf() //errorCode:-1失败时，多接数据

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
                onProgressChanged(50 + progress, "正在读取雷管充电信息")
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
                                onProgressChanged(100, "读取雷管信息失败")
                                callback?.onError(-62)
                                onDestroy()
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

        if (isContainsNotMatchCap) {
            if (TextUtils.equals("0", cap.status.toCharArray()[5].toString())) {
                meetCaps.add(cap)
            } else if (TextUtils.equals("0", cap.status.toCharArray()[6].toString())) {
                missCaps.add(cap)
            }
        }

        if (count + 1 >= BlastDelegate.getDelegate().getMaxSupportCapCount()) {
            onProgressChanged(100, "已达到雷管支持最大数")
            checkNotMatchCap(cap)
            onDestroy()
        } else if (count + 1 >= cap.total) {
            onProgressChanged(100, "已读取全部雷管信息")
            checkNotMatchCap(cap)
            onDestroy()
        } else {
            callback?.onResult(cap)
            if (callbackResult == null) {
                progress = ((count + 1) * (50f / cap.total)).toInt()
                retry(++count)
            } else {
                progress = ((count + 1) * (10f / cap.total)).toInt()
                retry(++count, callbackResult!!, caps!!, isContainsNotMatchCap)
            }
        }
    }

    private fun checkNotMatchCap(cap: CapEntity) {
        if (callbackResult == null) {
            cap.isScanEnd = true
            callback?.onResult(cap)
        }
        if (callbackResult != null) {
            val resultEntity = CapResultEntity()
            resultEntity.caps = caps
            resultEntity.errorCode = if (isContainsNotMatchCap) 0 else -1
            resultEntity.meetCaps = meetCaps
            resultEntity.missCaps = missCaps
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