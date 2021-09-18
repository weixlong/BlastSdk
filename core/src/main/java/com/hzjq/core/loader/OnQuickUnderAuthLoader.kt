package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.ProgressCallback

interface OnQuickUnderAuthLoader : OnCancelLoader {


    /**
     * 一键组网授权
     * 
     * 操作充电后可直接操作起爆
     */
    fun onQuickUnderAuth(caps:MutableList<CapEntity>, callback: ProgressCallback<CapProgressEntity>,underErrorCallback:ProgressCallback<CapResultEntity>)
}