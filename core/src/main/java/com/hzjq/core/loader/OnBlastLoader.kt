package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

interface OnBlastLoader: OnCancelLoader {

    /**
     * 爆破
     */
    fun onBlast(caps:List<CapEntity>, callback: Callback<CapProgressEntity>)
}