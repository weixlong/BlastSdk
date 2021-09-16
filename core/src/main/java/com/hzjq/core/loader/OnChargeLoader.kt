package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import com.hzjq.core.callback.Callback

interface OnChargeLoader :OnCancelLoader{

    /**
     * 充电
     */
    fun onCharge(caps:List<CapEntity>, callback: Callback<CapProgressEntity>)
}