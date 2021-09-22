package com.hzjq.core.bean

import java.text.SimpleDateFormat
import java.util.*


class LogBean {
    private val format = SimpleDateFormat("yyyy:MM:dd HH:mm:ss SSS")
    var time:String
    var content:String
    var type:Int

    constructor(content: String, type: Int) {
        this.time =  format.format(Date())
        this.content = content
        this.type = type
    }
}