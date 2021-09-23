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


        /**
         * 计算CRC16校验码
         * 逐个求和
         *
         * @param bytes 字节数组
         * @return [String] 校验码
         * @since 1.0
         */
        fun getCRC_16(bytes: ByteArray): String{
            var CRC = 0x0000ffff
            val POLYNOMIAL = 0x0000a001
            var i: Int
            var j: Int
            i = 0
            while (i < bytes.size) {
                CRC = CRC xor (bytes[i].toInt() and 0x000000ff)
                j = 0
                while (j < 8) {
                    if (CRC and 0x00000001 != 0) {
                        CRC = CRC shr 1
                        CRC = CRC xor POLYNOMIAL
                    } else {
                        CRC = CRC shr 1
                    }
                    j++
                }
                i++
            }
            if (Integer.toHexString(CRC).toUpperCase().length == 1) {
                return  /*byteToStr(bytes, bytes.length) +*/"000" + Integer.toHexString(
                    CRC
                ).toUpperCase()
            } else if (Integer.toHexString(CRC).toUpperCase().length == 2) {
                return  /*byteToStr(bytes, bytes.length) +*/"00" + Integer.toHexString(CRC)
                    .toUpperCase()
            } else if (Integer.toHexString(CRC).toUpperCase().length == 3) {
                return  /*byteToStr(bytes, bytes.length) +*/"0" + Integer.toHexString(CRC)
                    .toUpperCase()
            }
            return  /*byteToStr(bytes, bytes.length) + */Integer.toHexString(CRC)
                .toUpperCase()
        }
    }
}