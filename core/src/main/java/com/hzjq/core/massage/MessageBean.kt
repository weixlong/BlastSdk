package com.hzjq.core.massage

abstract class MessageBean {

   open internal var content:String //消息原始内容

    constructor(content: String) {
        this.content = content
    }


    /**
     * 消息组装
     */
    abstract fun assembly():ByteArray
}