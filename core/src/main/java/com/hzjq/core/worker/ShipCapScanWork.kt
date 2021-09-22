package com.hzjq.core.worker

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

class ShipCapScanWork : ScanShipCapWork {

    constructor(callback: Callback<CapEntity>?) : super(callback)

    override fun onDoWorkStart(vararg args: Any) {
        onProgressChanged(2 + progress * (48 / 100), "正在扫描雷管信息")
    }

    override fun onScanShipSuccess(progress: CapProgressEntity) {
        if (progress.stateCode == 0) {
            if (progress.progress < 100) {
                retry(progress.progress)
            } else {
                doNext(0)
            }
        } else {
            onScanError(progress)
        }
    }
}