package com.hzjq.core.loader

import com.hzjq.core.callback.OnVersionCallback

interface OnVersionLoader {


    /**
     * 获取版本
     */
    fun getVersion(callback: OnVersionCallback)
}