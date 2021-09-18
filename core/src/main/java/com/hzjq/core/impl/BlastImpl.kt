package com.hzjq.core.impl

import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.ProgressCallback
import com.hzjq.core.loader.OnBlastLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.BlastQueryWork
import com.hzjq.core.worker.BlastReadCapWork
import com.hzjq.core.worker.BlastWork

class BlastImpl : OnBlastLoader {
    
    private var works:Works?=null
    
    override fun onBlast(callback: ProgressCallback<CapResultEntity>) {
         works = Works.Builder.newBuilder()
            .addWork(BlastWork(callback))
            .addWork(BlastQueryWork(callback))
            .addWork(BlastReadCapWork(callback))
            .build()
            .queue()
    }

    override fun cancel() {
        works?.onDestroy()
    }
}