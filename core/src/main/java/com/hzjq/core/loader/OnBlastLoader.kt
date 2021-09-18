package com.hzjq.core.loader

import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.ProgressCallback

interface OnBlastLoader: OnCancelLoader {

    /**
     * 爆破
     */
    fun onBlast(callback: ProgressCallback<CapResultEntity>)
}