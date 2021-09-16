package com.hzjq.core.impl

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnAuthCapLoader

class AuthCapImpl : OnAuthCapLoader{

    override fun onAuthCap(caps: List<CapEntity>, callback: Callback<CapProgressEntity>) {

    }

    override fun cancel() {

    }
}