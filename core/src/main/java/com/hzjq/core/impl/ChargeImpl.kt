package com.hzjq.core.impl

import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnChargeLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.ChargeQueryWork
import com.hzjq.core.worker.ChargeWork

class ChargeImpl : OnChargeLoader {

    private var works:Works?=null

    override fun onCharge( callback: Callback<ChargeProgressEntity>) {
         works = Works.Builder.newBuilder()
            .addWork(ChargeWork(callback))
            .addWork(ChargeQueryWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
    }
}