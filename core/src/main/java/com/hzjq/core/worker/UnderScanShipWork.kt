package com.hzjq.core.worker

import android.text.TextUtils
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback

class UnderScanShipWork : ScanShipCapWork {

    private var isContainsNotMatchCap = false//是否包含不匹配的雷管数据

    private var callbackResult: Callback<CapResultEntity>? = null // 该值不为空时，在做方案的下传

    private var caps: MutableList<CapEntity>? = null

    constructor(callbackResult: Callback<CapResultEntity>?, callback: Callback<CapEntity>?) : super(callback){
        this.callbackResult = callbackResult
    }

    override fun onDoWorkStart(vararg args: Any) {
        isContainsNotMatchCap = false
        caps = args[1] as MutableList<CapEntity>
        onProgressChanged((30 + progress * (10f / 100)).toInt(), "正在扫描雷管信息")
    }

    override fun onScanShipSuccess(progress: CapProgressEntity) {
        if(TextUtils.equals("05",progress.errorCode) || TextUtils.equals("07",progress.errorCode)){
            isContainsNotMatchCap = true
        }
        if (progress.stateCode == 0) {
            if (progress.progress < 100) {
                retry(progress.progress,caps!!)
            } else {
                doNext(0,callbackResult!!,caps!!,isContainsNotMatchCap)
            }
        } else {
            doNext(0,callbackResult!!,caps!!,true)
        }
    }
}