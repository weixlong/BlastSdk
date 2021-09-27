package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.ProgressCallback
import io.reactivex.functions.Consumer

interface OnQuickRegisterLoader : OnCancelLoader {


    /**
     * 一键注册
     * 
     * 操作充电后可直接操作起爆
     * @param underErrorCallback 下传失败回调
     */
    fun onQuickRegister(caps:MutableList<CapEntity>, callback: ProgressCallback<CapProgressEntity>,underErrorCallback:Consumer<CapResultEntity>)
}