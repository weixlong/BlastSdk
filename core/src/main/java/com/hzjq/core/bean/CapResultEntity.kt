package com.hzjq.core.bean

class CapResultEntity {
    var errorCode = 0 //0：表示成功，-1：表示雷管不匹配,-2:爆破失败
    var caps: MutableList<CapEntity>? = null //原雷管数据
    var resultCaps: MutableList<CapEntity>? = null //errorCode:-2失败时，失败数据
    var missCaps: MutableList<CapEntity>? = null //errorCode:-1失败时，漏接数据
    var meetCaps: MutableList<CapEntity>? = null //errorCode:-1失败时，多接数据
}