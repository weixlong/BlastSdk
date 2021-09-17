package com.hzjq.core.impl

import com.hzjq.core.AckCode
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnUpgradeExitLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.UpgradeExitModeWork

class UpgradeExitImpl : OnUpgradeExitLoader {

    override fun onUpgradeExit(targetVersion:Int,callback: Callback<Boolean>) {
        Works.Builder.newBuilder()
            .addWork(UpgradeExitModeWork(object : Callback<Int>{
                override fun onResult(t: Int) {
                    if(t == AckCode.EXIT_UPGRADE_MODE_OK){
                        callback.onResult(true)
                    } else {
                        callback.onError(t)
                    }
                }

                override fun onError(errorCode: Int) {
                    callback.onResult(false)
                    callback.onError(errorCode)
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    callback.onRetryCountChanged(retryCount, action)
                }
            })).build().queue()
    }
}