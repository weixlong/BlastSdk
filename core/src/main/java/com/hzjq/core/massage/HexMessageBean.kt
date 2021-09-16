package com.hzjq.core.massage

import com.hzjq.core.util.Convert

class HexMessageBean : MessageBean {

    constructor(content: String) : super(content)

    override fun assembly(): ByteArray {
        return Convert.hex2byte(content)
    }

}