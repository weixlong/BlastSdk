package com.hzjq.core.bean

import org.json.JSONObject
import java.io.Serializable

/**
 * 雷管实体
 */
open class CapEntity :Serializable{
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
    var isScanEnd = false//扫描是否结束，是否是最后一条
    var isStandardUid = false //是否是新标准的Uid,可忽略
    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("uid",uid)
        jsonObject.put("capNumber",capNumber)
        jsonObject.put("delay",delay)
        jsonObject.put("areaNumber",areaNumber)
        jsonObject.put("holeNumber",holeNumber)
        jsonObject.put("rowNumber",rowNumber)
        jsonObject.put("status",status)
        jsonObject.put("total",total)
        jsonObject.put("mVoltage",mVoltage)
        jsonObject.put("mElectric",mElectric)
        jsonObject.put("password",password)
        jsonObject.put("isScanEnd",isScanEnd)
        jsonObject.put("isStandardUid",isStandardUid)
        return jsonObject.toString()
    }


}