package com.hzjq.core.bean


/**
 * 充电进度返回实体
 */
class ChargeProgressEntity : CapProgressEntity() {

    var code = 0 //0:成功,-1:表示有错误列表

    var caps = arrayListOf<CapEntity>() //雷管列表

    var notMatchCaps = arrayListOf<CapEntity>()//不匹配的雷管列表

    var chargeErrCaps = arrayListOf<CapEntity>()//充电失败列表

    var notAuthCaps = arrayListOf<CapEntity>() //未授权列表

}