package com.hzjq.core.impl

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.bean.ErrorResult
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnQuickRegisterLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.*
import io.reactivex.functions.Consumer

class QuickRegisterImpl : OnQuickRegisterLoader {

    private var underWorks: Works? = null
    private var authWorks: Works? = null

    override fun onQuickRegister(caps: MutableList<CapEntity>,callback: ProgressCallback<CapProgressEntity>,underErrorCallback: Consumer<CapResultEntity>) {
        val capCallback = object :ProgressCallback<CapEntity>{
            override fun onResult(t: CapEntity) {

            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: ErrorResult) {
                callback.onError(errorCode)
            }

            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                callback.onProgressChanged(progress, total, action)
            }
        }

        val authCall = object : ProgressCallback<CapProgressEntity>{
            override fun onResult(t: CapProgressEntity) {
                callback.onResult(t)
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: ErrorResult) {
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
                    underErrorCallback.accept(t)
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: ErrorResult) {
                callback.onError(errorCode)
            }

            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                callback.onProgressChanged(progress/2,100,action)
            }

        }

        underWorks = Works.Builder.newBuilder()
            .addWork(CheckCapPassWordWork(caps, underCall))
            .addWork(ClearChipStateWork(caps,capCallback))
            .addWork(UnderCapWork(underCall))
            .addWork(UnderInnerScanModeWork(caps,capCallback))
            .addWork(UnderScanShipWork(underCall,capCallback))
            .addWork(ReadCapWork(capCallback))
            .build()
            .queue()
    }

    override fun cancel() {
        underWorks?.onDestroy()
        authWorks?.onDestroy()
    }
}