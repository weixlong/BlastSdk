package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.ErrorCode
import com.hzjq.core.bean.ErrorResult
import com.hzjq.core.callback.Callback
import com.hzjq.core.work.Work
import io.reactivex.functions.Consumer
import java.io.File
import java.util.*

class ReadUpgradeFileWork : Work<Int> {

    private val mSectorDataList = ArrayList<String>() // 扇区数据

    private val mSectorAddrList = ArrayList<String>() // 扇区地址

    private var binFile:File

    private var targetVersion:Int = 0

    constructor(targetVersion:Int,binFile:File,callback: Callback<Int>) : super(callback){
        this.binFile = binFile
        this.targetVersion = targetVersion
    }

    override fun doWork(vararg args: Any) {
        if(!binFile.exists() || !binFile.name.toLowerCase().endsWith(".bin") || binFile.length() == 0L){
            cancel()
            onProgressChanged(100,"文件解析失败,请检查文件后缀与大小是否正确")
            callback?.onError(ErrorCode.getErrorResult(-17))
            return
        }
        mSectorDataList.clear()
        mSectorAddrList.clear()
        onProgressChanged(5,"正在解析升级文件")
        BlastDelegate.getDelegate().getParseLoader()
            .parseUpgradeFileData(binFile,mSectorDataList,mSectorAddrList,
                Consumer<Boolean> {
                    if(it) {
                        onProgressChanged(10,"文件解析成功")
                        doNext(mSectorDataList, mSectorAddrList,0)
                    } else {
                        onProgressChanged(98,"文件解析失败,请检查文件内容是否正确")
                        callback?.onError(ErrorCode.getErrorResult(-18))
                        BlastDelegate.getDelegate().getUpgradeExitLoader()
                            .onUpgradeExit(targetVersion,object : Callback<Boolean>{
                                override fun onResult(t: Boolean) {
                                    if(t){
                                        onProgressChanged(100,"文件解析失败,退出升级模式成功")
                                        onDestroy()
                                    } else {
                                        onProgressChanged(100,"文件解析失败,退出升级模式失败")
                                        callback?.onError(ErrorCode.getErrorResult(-19))
                                        onDestroy()
                                    }
                                }

                                override fun onError(errorCode: ErrorResult) {
                                    onProgressChanged(100,"文件解析失败,退出升级模式失败")
                                    callback?.onError(errorCode)
                                    onDestroy()
                                }

                                override fun onRetryCountChanged(retryCount: Int, action: String) {
                                    callback?.onRetryCountChanged(retryCount, action)
                                }
                            })
                    }
                })
    }

    override fun cancel() {
        mSectorDataList.clear()
        mSectorAddrList.clear()
    }


}