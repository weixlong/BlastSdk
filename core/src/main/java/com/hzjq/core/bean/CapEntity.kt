package com.hzjq.core.bean

/**
 * 雷管实体
 */
open class CapEntity {
    var uid = "0" //雷管UID
    var capNumber = "0" // 序号
    var delay = "0" // 延时
    var areaNumber = "0" // 区号
    var holeNumber = "0" // 孔号
    var rowNumber = "0"// 排号
    var status = "0"// 状态
    var total = 0 //雷管总数
    var mVoltage = 0.0//电压值
    var mElectric = 0.0//电流值
    var password = ""//授权密码
}