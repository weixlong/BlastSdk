package com.hzjq.core.impl

import com.hzjq.core.bean.AlongCapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.AlongCapCheckLoader
import com.hzjq.core.worker.AlongCapCheckWork

class AlongCapCheckImpl : AlongCapCheckLoader {

    override fun start(callback: Callback<AlongCapResultEntity>) {
        AlongCapCheckWork(callback).doWork()
    }
}