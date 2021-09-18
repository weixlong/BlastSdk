package com.hzjq.core.impl

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnQuickUnderAuthLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.*

class QuickUnderAuthImpl : OnQuickUnderAuthLoader {

    private var underWorks: Works? = null
    private var authWorks: Works? = null

    override fun onQuickUnderAuth(caps: MutableList<CapEntity>,callback: ProgressCallback<CapProgressEntity>,underErrorCallback:ProgressCallback<CapResultEntity>) {
        val capCallback = object :Callback<CapEntity>{
            override fun onResult(t: CapEntity) {

            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: Int) {
                callback.onError(errorCode)
            }
        }

        val authCall = object : ProgressCallback<CapProgressEntity>{
            override fun onResult(t: CapProgressEntity) {
                callback.onResult(t)
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: Int) {
                callback.onError(errorCode)
            }

            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                callback.onProgressChanged(50+progress/2,100,action)
            }

        }


        val underCall = object : ProgressCallback<CapResultEntity>{
            override fun onResult(t: CapResultEntity) {
                if(t.errorCode == 0){
                    authWorks = Works.Builder.newBuilder()
                        .addWork(InnerAuthModeWork(authCall))
                        .addWork(AuthQueryWork(authCall))
                        .addWork(AuthWriteWork(authCall))
                        .addWork(AuthWriteQueryWork(authCall))
                        .build()
                        .queue()
                } else {
                    underErrorCallback.onResult(t)
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: Int) {
                callback.onError(errorCode)
            }

            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                callback.onProgressChanged(progress/2,100,action)
            }

        }

        underWorks = Works.Builder.newBuilder()
            .addWork(CheckCapPassWordWork(caps, underCall))
            .addWork(UnderCapWork(true,underCall))
            .addWork(InnerScanModeWork(caps,capCallback))
            .addWork(ScanShipCapWork(underCall,capCallback))
            .addWork(ReadCapWork(capCallback))
            .build()
            .queue()
    }

    override fun cancel() {
        underWorks?.onDestroy()
        authWorks?.onDestroy()
    }
}