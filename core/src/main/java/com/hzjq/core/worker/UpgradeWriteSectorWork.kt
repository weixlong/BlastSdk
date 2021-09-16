package com.hzjq.core.worker

import android.annotation.SuppressLint
import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.massage.HexMessageBean
import com.hzjq.core.work.Work
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 写入一个扇区的数据
 */
class UpgradeWriteSectorWork : Work<Int> {

    private var dataCount = 0
    private var disposable: Disposable? = null

    constructor(callback: Callback<Int>?) : super(callback)

    override fun doWork(vararg args: Any) {
        if (args.size >= 3) {
            cancel()
            val mSectorDataList = args[0] as ArrayList<String>// 扇区数据
            val mSectorAddrList = args[1] as ArrayList<String>// 扇区地址
            val position = args[3] as Int
            writeSectorData(mSectorDataList, mSectorAddrList, position)
        }
    }


    @SuppressLint("CheckResult")
    private fun writeSectorData(
        mSectorDataList: ArrayList<String>,
        mSectorAddrList: ArrayList<String>,
        position: Int
    ) {
        if (dataCount == 8) {
            doNext(mSectorDataList, mSectorAddrList,position)
            return
        }
        onProgressChanged(position*22/90,"写入当前扇区数据(${dataCount}/${8})")
        disposable?.dispose()
        disposable = Observable.timer(60, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val str = "0" + dataCount + mSectorDataList.get(position)
                    .substring(512 * dataCount, 512 * (dataCount + 1)).toUpperCase()
                val msg = HexMessageBean(str)
                BlastDelegate.getDelegate().getCmdExeLoader()
                    .exeOnceCmd(msg.assembly(), callback)
                ++dataCount
            }.subscribe(Consumer {
                if (it < 8) {
                    writeSectorData(mSectorDataList, mSectorAddrList, position)
                }
            })
    }

    override fun cancel() {
        dataCount = 0
        disposable?.dispose()
    }
}