package com.hzjq.blast

import com.hzjq.core.bean.CapEntity

class CapUtils {

    companion object {

        /**
         * 设置测试密码
         */
        fun setTestPassword(caps:MutableList<CapEntity>){
            caps.forEach {
                it.password = "00000000"
            }
        }
    }
}