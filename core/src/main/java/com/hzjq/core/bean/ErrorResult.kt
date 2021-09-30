package com.hzjq.core.bean

class ErrorResult {
    var errorCode = 0 //错误码
    var errorAction = "" //错误描述

    constructor(errorCode: Int, errorAction: String) {
        this.errorCode = errorCode
        this.errorAction = errorAction
    }
}