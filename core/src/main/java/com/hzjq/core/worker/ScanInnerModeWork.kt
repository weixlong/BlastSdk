package com.hzjq.core.worker

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.callback.Callback

class ScanInnerModeWork : InnerScanModeWork {

    constructor(callback: Callback<CapEntity>?) : super(callback)

    override fun onDoWorkStart() {
        onProgressChanged(2, "正在进入扫描模式")
    }

    override fun onInnerScanDoNext() {
        onProgressChanged(3, "进入扫描模式成功")
        doNext(0)
    }
}