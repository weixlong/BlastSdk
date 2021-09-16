package com.hzjq.core.massage

import com.hzjq.core.util.Convert

class DataMessageBean : MessageBean {

    constructor(content: String) : super(content)

    override fun assembly(): ByteArray {
        val mByte = Convert.hexString2Bytes(content)
        if(mByte != null) {
            val str = Convert.getSum8(mByte, mByte.size)
            return Convert.hex2byte(str)
        }
        return Convert.hex2byte(content)
    }
}