package com.hzjq.core.worker

import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.work.Work
import io.reactivex.functions.Consumer
import java.io.File
import java.util.*

class ReadUpgradeFileWork : Work<Int> {

    private val mSectorDataList = ArrayList<String>() // 扇区数据

    private val mSectorAddrList = ArrayList<String>() // 扇区地址

    private var binFile:File

    constructor(binFile:File,callback: Callback<Int>) : super(callback){
        this.binFile = binFile
    }

    override fun doWork(vararg args: Any) {
        if(!binFile.exists() || !binFile.name.toLowerCase().endsWith(".bin")){
            cancel()
            onProgressChanged(100,"文件解析失败,请检查文件是否正确")
            callback?.onError(-8)
        }
        mSectorDataList.clear()
        mSectorAddrList.clear()
        onProgressChanged(5,"正在解析升级文件")
        BlastDelegate.getDelegate().getParseLoader()
            .parseUpgradeFileData(binFile,mSectorDataList,mSectorDataList,
                Consumer<Boolean> {
                    if(it) {
                        onProgressChanged(10,"文件解析成功")
                        doNext(mSectorDataList, mSectorAddrList,0)
                    } else {
                        cancel()
                        onProgressChanged(100,"文件解析失败,请检查文件是否正确")
                        callback?.onError(-8)
                    }
                })
    }

    override fun cancel() {
        mSectorDataList.clear()
        mSectorAddrList.clear()
    }


}