package com.hzjq.core.impl

import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.ClearOccupyLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.ClearOccupyWork

class ClearOccupyImpl : ClearOccupyLoader {

    private var works:Works?=null

    override fun clearOccupy(callback: Callback<Boolean>) {
        works = Works.Builder.newBuilder()
            .addWork(ClearOccupyWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
    }


}