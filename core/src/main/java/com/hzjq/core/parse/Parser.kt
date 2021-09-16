package com.hzjq.core.parse

import android.annotation.SuppressLint
import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.sdk.DeviceManager_LXR5000.util.ByteUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

class Parser : ParseLoader {

    override fun parseVersionMsg(msg: String): Int {
        if(msg.length >= 16) {
            return Integer.valueOf(msg.substring(12, 16), 16)
        }
        return 0
    }

    override fun parseUpgradeModeMsg(msg: String): Boolean {
        if (!TextUtils.isEmpty(msg) && msg.length >= 12) {
            val code = msg.substring(4, 6)
            if (TextUtils.equals(code,BlastDelegate.getDelegate().getCmdType())) {
                return  true
            }
        }
        return false
    }

    @SuppressLint("CheckResult")
    override fun parseUpgradeFileData(
        binFile: File,
        mSectorDataList: ArrayList<String>,
        mSectorAddrList: ArrayList<String>,
        onNext: Consumer<Boolean>
    ) {
        Observable.create<Boolean> {
            DataInputStream(FileInputStream(binFile)).use { dataStream ->
                val block = ByteArray(2048)
                while (dataStream.read(block) != -1) {
                    mSectorDataList.add(ByteUtil.bytesToHexString(block))
                    mSectorAddrList.add(ByteUtil.getCRC_16(block))
                }
                dataStream.close()
                it.onNext(true)
            }
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .doOnError {
                onNext.accept(false)
            }.subscribe(onNext)
    }
}