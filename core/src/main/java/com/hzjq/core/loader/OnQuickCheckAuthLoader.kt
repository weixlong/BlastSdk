package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

interface OnQuickCheckAuthLoader : OnCancelLoader {


    /**
     * 一键组网
     */
    fun onQuickCheckAuth(caps:List<CapEntity>, callback: Callback<CapProgressEntity>)
}