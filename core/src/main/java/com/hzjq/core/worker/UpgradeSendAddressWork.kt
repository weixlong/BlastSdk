package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.ErrorResult
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.DataMessageBean
import com.hzjq.core.receive.Receiver
import com.hzjq.core.receive.Receives
import com.hzjq.core.work.Work
import java.util.*


/**
 * 发送升级地址
 */
class UpgradeSendAddressWork : Work<Int> {

    private val maxAddressNum = 23 // 起爆器总页数
    private var position = 0 //当前页数
    private var curAddressData = ""
    private var targetVersion:Int = 0
    private var retryCount = 0

    constructor(targetVersion:Int,callback: Callback<Int>) : super(callback){
        this.targetVersion = targetVersion
    }

    override fun doWork(vararg args: Any) {
        if (args.size >= 3) {
            val mSectorDataList = args[0] as ArrayList<String>// 扇区数据
            val mSectorAddrList = args[1] as ArrayList<String>// 扇区地址
            position = args[2] as Int
            onProgressChanged(position*82/22,"写入当前扇区地址(${position}/${maxAddressNum})")
            if (maxAddressNum > position) {
                curAddressData = mSectorAddrList[position]
                Receives.getInstance().registerReceiver(
                    BlastDelegate.getDelegate().getAssemblyCmdLoader()
                        .getUpgradeSectorCmd(position, curAddressData)
                    , object : Receiver {
                        override fun convert(msg: String): Any {
                            return msg
                        }

                        override fun onSuccess(msg: Any) {
                            val data = msg as String
                            if (data.length >= 14) {
                                val state: String = data.substring(8, 10)
                                when (state) {
                                    "81" -> {//成功，写入这个扇区的数据
                                        retryCount = 0
                                        doNext(mSectorDataList,mSectorAddrList,position)
                                    }
                                    "82" -> {//跳过这个地址，进行下一个地址写入
                                        retryCount = 0
                                        onProgressChanged(position*82/22,"跳过当前扇区地址(${position}/${maxAddressNum})")
                                        position++
                                        retry(mSectorDataList,mSectorAddrList,position)
                                    }
                                    else -> {
                                        if(retryCount < BlastDelegate.getDelegate().getUpgradeWriteRetryCount()) {
                                            onProgressChanged(
                                                position * 82 / 22,
                                                "重试当前扇区地址(${position}/${maxAddressNum})"
                                            )
                                            retry(mSectorDataList, mSectorAddrList, position)//失败重试
                                            retryCount++
                                        } else {
                                            retryCount = 0
                                            exit()
                                        }
                                    }
                                }
                            }

                        }

                        override fun failed() {

                        }

                    })

                val msg = DataMessageBean(
                    BlastDelegate.getDelegate().getAssemblyCmdLoader()
                        .getUpgradeSectorCmd(position, curAddressData).cmd
                )
                BlastDelegate.getDelegate().getCmdExeLoader()
                    .exePollResultCmd(msg.assembly(), callback)
            } else {
                exit()
            }
        }
    }


    private fun exit(){
        BlastDelegate.getDelegate().getUpgradeExitLoader()
            .onUpgradeExit(targetVersion,object : Callback<Boolean>{
                override fun onResult(t: Boolean) {
                    if(t){
                        onProgressChanged(98,"退出升级模式成功")
                        VersionWork(callback).doWork()
                    } else {
                        onProgressChanged(100,"退出升级模式失败")
                        callback?.onError(ErrorCode.getErrorResult(-22))
                        onDestroy()
                    }
                }

                override fun onError(errorCode: ErrorResult) {
                    onProgressChanged(100,"退出升级模式失败")
                    callback?.onError(errorCode)
                    onDestroy()
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    callback?.onRetryCountChanged(retryCount, action)
                }
            })
    }

    override fun cancel() {
        Receives.getInstance().unRegisterReceiver(
            BlastDelegate.getDelegate().getAssemblyCmdLoader()
                .getUpgradeSectorCmd(position, curAddressData)
        )
    }
}