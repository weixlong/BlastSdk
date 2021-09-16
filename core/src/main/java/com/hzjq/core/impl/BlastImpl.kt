package com.hzjq.core.impl

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnBlastLoader

class BlastImpl : OnBlastLoader {
    override fun onBlast(caps: List<CapEntity>, callback: Callback<CapProgressEntity>) {

    }

    override fun cancel() {

    }
}