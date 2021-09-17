package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback

interface OnUnderCapLoader : OnCancelLoader{


    /**
     * 下传方案
     * 请注意查看返回值CapResultEntity 中的解释，可能包含雷管错误信息
     */
    fun onUnderCap(caps:MutableList<CapEntity>, callback: Callback<CapResultEntity>)
}