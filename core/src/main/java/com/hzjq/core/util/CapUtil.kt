package com.hzjq.core.util

import com.hzjq.core.bean.CapEntity

class CapUtil {

    companion object {


        /**
         * 获取组装数据
         */
        fun getMakeData(num: Int, list: List<CapEntity>): String {
            val sb = StringBuffer()
            val number: String = Convert.getCurrentDetonatorNum(num)
            val total: String = Convert.getCurrentDetonatorNum(list.size)
            sb.append(number).append(total).append(list[num].uid)
            sb.append(if (!list[num].areaNumber.isNullOrEmpty()) list[num].password else "00000000")
            sb.append(Convert.getCurrentDetonatorNum(list[num].delay.toInt()))
            sb.append(Convert.getCurrentDetonatorNum(Integer.valueOf(list[num].holeNumber, 16)))
            sb.append("00")
            return sb.toString()
        }

    }
}