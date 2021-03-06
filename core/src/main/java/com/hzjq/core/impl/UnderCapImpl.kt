package com.hzjq.core.impl

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.bean.ErrorResult
import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnUnderCapLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.*

class UnderCapImpl : OnUnderCapLoader {

    private var works: Works? = null

    override fun onUnderCap(caps: MutableList<CapEntity>, callback: ProgressCallback<CapResultEntity>) {
        val capCallback = object :Callback<CapEntity>{
            override fun onResult(t: CapEntity) {

            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: ErrorResult) {
                callback.onError(errorCode)
            }
        }
        works = Works.Builder.newBuilder()
            .addWork(CheckCapPassWordWork(caps, callback))
            .addWork(ClearChipStateWork(caps,capCallback))
            .addWork(UnderCapWork(callback))
            .addWork(UnderInnerScanModeWork(caps,capCallback))
            .addWork(UnderScanShipWork(callback,capCallback))
            .addWork(ReadCapWork(capCallback))
            .build()
            .queue()

    }



    override fun cancel() {
        works?.onDestroy()
    }
}