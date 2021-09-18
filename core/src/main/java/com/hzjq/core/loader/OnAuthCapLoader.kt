package com.hzjq.core.loader

import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.ProgressCallback

interface OnAuthCapLoader:OnCancelLoader {


    /**
     * 授权
     */
    fun onAuthCap(callback: ProgressCallback<CapProgressEntity>)
}