package com.hzjq.core.loader

import com.hzjq.core.bean.AlongCapResultEntity
import com.hzjq.core.callback.Callback

interface AlongCapCheckLoader {

    fun start(callback:Callback<AlongCapResultEntity>)
}