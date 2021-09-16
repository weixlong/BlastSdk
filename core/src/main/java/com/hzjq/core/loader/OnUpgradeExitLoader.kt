package com.hzjq.core.loader

import com.hzjq.core.callback.Callback

interface OnUpgradeExitLoader {

    /**
     * 退出升级模式
     */
    fun onUpgradeExit(callback:Callback<Boolean>)

}