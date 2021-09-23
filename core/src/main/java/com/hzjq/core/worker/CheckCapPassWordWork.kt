package com.hzjq.core.worker

import android.text.TextUtils
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapResultEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.work.Work

/**
 * 检查雷管是否存在密码
 */
class CheckCapPassWordWork : Work<CapResultEntity> {

    private var caps: MutableList<CapEntity>

    constructor(caps: MutableList<CapEntity>, callback: Callback<CapResultEntity>?) : super(callback) {
        this.caps = caps
    }

    override fun doWork(vararg args: Any) {
        onProgressChanged(0,"正在检查雷管密码")
        if(caps.isNullOrEmpty()){
            onProgressChanged(100,"雷管数据为空,请检查")
            callback?.onError(-14)
            return
        }
        caps.forEach {
            if (TextUtils.isEmpty(it.password)) {
                callback?.onError(-14)
                onDestroy()
                return
            }
        }
        onProgressChanged(1,"检查雷管密码通过")
        doNext(caps)
    }

    override fun cancel() {

    }
}