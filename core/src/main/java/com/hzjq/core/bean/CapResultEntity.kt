package com.hzjq.core.bean

class CapResultEntity {
    var errorCode = 0 //0：表示成功，-1：表示雷管不匹配
    var caps: MutableList<CapEntity>? = null //原雷管数据
    var resultCaps: MutableList<CapEntity>? = null //失败时，失败数据
}