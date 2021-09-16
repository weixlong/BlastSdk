package com.hzjq.core.loader

import com.hzjq.core.callback.OnScanCapCallback

interface OnScanCapLoader : OnCancelLoader{

    fun onScanCap(callback: OnScanCapCallback)
}