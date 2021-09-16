package com.hzjq.core.impl

import com.hzjq.core.callback.OnVersionCallback
import com.hzjq.core.loader.OnVersionLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.VersionWork

class VersionImpl : OnVersionLoader {

    override fun getVersion(callback: OnVersionCallback) {
        Works.Builder.newBuilder()
            .addWork(VersionWork(callback))
            .build()
            .queue()
    }
}