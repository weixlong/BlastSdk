package com.hzjq.core.impl

import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnAuthCapLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.AuthQueryWork
import com.hzjq.core.worker.AuthWriteQueryWork
import com.hzjq.core.worker.AuthWriteWork
import com.hzjq.core.worker.InnerAuthModeWork

class AuthCapImpl : OnAuthCapLoader {

    private var works: Works? = null

    override fun onAuthCap(callback: Callback<CapProgressEntity>) {
        works = Works.Builder.newBuilder()
            .addWork(InnerAuthModeWork(callback))
            .addWork(AuthQueryWork(callback))
            .addWork(AuthWriteWork(callback))
            .addWork(AuthWriteQueryWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
    }
}