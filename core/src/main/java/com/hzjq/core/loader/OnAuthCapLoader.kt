package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

interface OnAuthCapLoader:OnCancelLoader {


    /**
     * 授权
     */
    fun onAuthCap(caps:List<CapEntity>, callback: Callback<CapProgressEntity>)
}