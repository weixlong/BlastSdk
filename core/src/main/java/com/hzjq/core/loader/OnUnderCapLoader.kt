package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

interface OnUnderCapLoader : OnCancelLoader{


    /**
     * 下传方案
     */
    fun onUnderCap(caps:List<CapEntity>, callback: Callback<CapProgressEntity>)
}