package com.hzjq.core.loader

import com.hzjq.core.callback.OnVersionUpgradeCallback
import java.io.File

interface OnVersionUpgradeLoader {


    /**
     * 起爆器升级
     */
    fun onVersionUpgrade(targetVersion:Int,binFile: File, callback: OnVersionUpgradeCallback)
}