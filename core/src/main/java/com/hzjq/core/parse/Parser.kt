package com.hzjq.core.parse

import android.annotation.SuppressLint
import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.AlongCapResultEntity
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.util.CapUtil
import com.hzjq.core.util.Convert
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
                    mSectorAddrList.add(CapUtil.getCRC_16(block))
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

    override fun parseScanProgress(msg: String): CapProgressEntity {
        val errorCode: String = msg.substring(20, 22)
        val progress = Integer.valueOf(msg.substring(22, 24), 16)
        val total = Integer.valueOf(msg.substring(24, 28), 16)
        val stateCode = Integer.valueOf(msg.substring(28, 30), 16)
        val mVoltage = Integer.valueOf(msg.substring(12, 16), 16)
        val mElectric = Integer.valueOf(msg.substring(16, 20), 16)
        return CapProgressEntity(errorCode,progress,total,stateCode,mVoltage*1.0/10,mElectric*1.0/10)
    }

    override fun parseCap(msg: String): CapEntity {
        val item = CapEntity()
        item.capNumber = Integer.valueOf(msg.substring(12, 16), 16).toString()
        item.uid = msg.substring(20, 36)
        item.delay = Integer.valueOf(msg.substring(44, 48), 16).toString()
        item.holeNumber = msg.substring(48, 52)
        item.status = Convert.HexToBin8(msg.substring(52, 54))
        item.total = Integer.valueOf(msg.substring(16, 20), 16)
        item.isStandardUid = BlastDelegate.getDelegate().isStandardUid()
        return item
    }

    override fun parseAlongCap(msg: String): AlongCapResultEntity {
        val item = AlongCapResultEntity()
        val mVoltage = Integer.valueOf(msg.substring(12, 16), 16)
        val mElectric = Integer.valueOf(msg.substring(16, 20), 16)
        item.mElectric = mElectric.toDouble()
        item.mVoltage = mVoltage*1.0/10
        if(msg.length > 36) {
            val uid = msg.substring(20, 36)
            item.uid = uid
        } else {
            item.error = -1
        }
        return item
    }
}