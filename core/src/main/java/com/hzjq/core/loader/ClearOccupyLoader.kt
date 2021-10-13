package com.hzjq.core.loader

import com.hzjq.core.callback.Callback

interface ClearOccupyLoader : OnCancelLoader{


    /**
     * 清除占用
     */
    fun clearOccupy(callback: Callback<Boolean>)
}