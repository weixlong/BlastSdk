package com.hzjq.core.worker

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.callback.Callback

class UnderInnerScanModeWork : InnerScanModeWork {

    private var caps: MutableList<CapEntity>? = null

    constructor(caps: MutableList<CapEntity>?, callback: Callback<CapEntity>?) : super(
        callback
    ) {
        this.caps = caps
    }
    override fun onDoWorkStart() {
        onProgressChanged(30, "正在进入扫描模式")
    }

    override fun onInnerScanDoNext() {
        onProgressChanged(30, "进入扫描模式成功")
        doNext(0,caps!!)
    }
}