package com.hzjq.core.loader

import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback

interface OnChargeLoader :OnCancelLoader{

    /**
     * 充电
     */
    fun onCharge( callback: Callback<ChargeProgressEntity>)
}