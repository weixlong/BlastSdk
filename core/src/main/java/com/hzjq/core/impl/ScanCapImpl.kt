package com.hzjq.core.impl

import com.hzjq.core.callback.OnScanCapCallback
import com.hzjq.core.loader.OnScanCapLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.ClearChipStateWork
import com.hzjq.core.worker.InnerScanModeWork
import com.hzjq.core.worker.ReadCapWork
import com.hzjq.core.worker.ScanShipCapWork

class ScanCapImpl : OnScanCapLoader {

    private var works: Works? = null

    override fun onScanCap(callback: OnScanCapCallback) {
        works = Works.Builder.newBuilder()
            .addWork(ClearChipStateWork(callback))
            .addWork(InnerScanModeWork(null,callback))
            .addWork(ScanShipCapWork(null,callback))
            .addWork(ReadCapWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
    }
}