package com.hzjq.core.impl

import android.text.TextUtils
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnChargeLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.ChargeQueryWork
import com.hzjq.core.worker.ChargeWork
import com.hzjq.core.worker.CloseChargeWork
import com.hzjq.core.worker.ReadCapWork

class ChargeImpl : OnChargeLoader {

    private var works: Works? = null
    private var closeWork:Works?=null
    val notAuthArray = arrayListOf<CapEntity>()
    val notMatchArray = arrayListOf<CapEntity>()
    val notChargeArray = arrayListOf<CapEntity>()

    override fun onCharge(callback: ProgressCallback<ChargeProgressEntity>) {
        val cpe = ChargeProgressEntity()
        val call = object : Callback<CapEntity> {
            override fun onResult(it: CapEntity) {
                if (!TextUtils.isEmpty(it.status)) {
                    if (TextUtils.equals("0", it.status.toCharArray()[5].toString())
                        || TextUtils.equals("0", it.status.toCharArray()[6].toString())
                    ) {
                        cpe.code = -1
                        notMatchArray.add(it)
                    }

                    if (TextUtils.equals("0", it.status.toCharArray()[0].toString())) {
                        cpe.code = -1
                        notChargeArray.add(it)
                    }

                    if (TextUtils.equals("0", it.status.toCharArray()[1].toString())) {
                        cpe.code = -1
                        notAuthArray.add(it)
                    }
                    if(cpe.isEnd){
                        cpe.chargeErrCaps = notChargeArray
                        cpe.notAuthCaps = notAuthArray
                        cpe.notMatchCaps = notMatchArray
                        callback.onResult(cpe)
                    }
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                callback.onRetryCountChanged(retryCount, action)
            }

            override fun onError(errorCode: Int) {
                callback.onError(errorCode)
            }

        }
        works = Works.Builder.newBuilder()
            .addWork(ChargeWork(callback))
            .addWork(ChargeQueryWork(callback))
            .addWork(ReadCapWork(call))
            .build()
            .queue()
    }

    override fun onCloseElectric(callback: Callback<Boolean>) {
        closeWork = Works.Builder.newBuilder()
            .addWork(CloseChargeWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
        closeWork?.onDestroy()
    }
}