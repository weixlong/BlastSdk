package com.hzjq.core.loader

import com.hzjq.core.bean.ChargeProgressEntity
import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.ProgressCallback

interface OnChargeLoader :OnCancelLoader{

    /**
     * 充电
     */
    fun onCharge(callback: ProgressCallback<ChargeProgressEntity>)


    /**
     * 关电，充电之后如无起爆操作，必须对雷管进行关电（释放高压）的操作
     */
    fun onCloseElectric(callback: Callback<Boolean>)
}