package com.hzjq.core.util;

import android.text.TextUtils;

/**
 * 数据转换工具类
 * Data conversion tool class
 *
 * @author Vi
 * @date 2019-07-17 17:43
 * @e-mail cfop_f2l@163.com
 */

public class Convert {

    /**
     * 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
     * Judging odd or even numbers, bit operations, the last bit is 1 is odd, 0 is even
     */
    public static int isOdd(int num) {
        return num & 1;
    }

    /**
     * Hex字符串转int
     * Hex string to int
     */
    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    /**
     * Hex字符串转byte
     * Hex string to byte
     */
    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    /**
     * 1字节转2个Hex字符
     * 1 byte to 2 Hex characters
     */
    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    /**
     * 字节数组转转hex字符串
     * Byte array to hex string
     */
    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }



    /**
     * 字节数组转转hex字符串，可选长度
     * Byte array to hex string, optional length
     */
    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    /**
     * hex字符串转字节数组
     * Hex string to byte array
     */
    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * hex字符串转字节数组
     * Hex string to byte array
     */
    public static byte[] HexToByteArr2(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 字符串转换为16进制字符串
     * String to a hex string
     *
     * @param s
     * @return
     */
    public static String stringToHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * 16进制字符串转换为字符串
     * Convert a hex string to a string
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replaceAll(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "gbk");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {

        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] b = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return b;
        }

    }

    /*
     * 字符转换为字节
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 向串口发送数据转为字节数组
     */
    public static byte[] hex2byte(String hex) {
        String digital = "0123456789ABCDEF";
        String hex1 = hex.replace(" ", "");
        char[] hex2char = hex1.toCharArray();
        byte[] bytes = new byte[hex1.length() / 2];
        byte temp;
        for (int p = 0; p < bytes.length; p++) {
            temp = (byte) (digital.indexOf(hex2char[2 * p]) * 16);
            temp += digital.indexOf(hex2char[2 * p + 1]);
            bytes[p] = (byte) (temp & 0xff);
        }
        return bytes;
    }

    /**
     * 接收到的字节数组转换16进制字符串
     */
    public static String bytes2HexString(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 接收到的字节数组转换16进制字符串
     */
    public static String byteToStr(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 计算CRC16校验码
     * 逐个求和
     *
     * @param bytes 字节数组
     * @return {@link String} 校验码
     * @since 1.0
     */
    public static String getCRC_16(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        if (Integer.toHexString(CRC).toUpperCase().length() == 1) {
            return /*byteToStr(bytes, bytes.length) +*/ "000" + Integer.toHexString(CRC).toUpperCase();
        } else if (Integer.toHexString(CRC).toUpperCase().length() == 2) {
            return /*byteToStr(bytes, bytes.length) +*/ "00" + Integer.toHexString(CRC).toUpperCase();
        } else if (Integer.toHexString(CRC).toUpperCase().length() == 3) {
            return /*byteToStr(bytes, bytes.length) +*/ "0" + Integer.toHexString(CRC).toUpperCase();
        }
        return /*byteToStr(bytes, bytes.length) + */Integer.toHexString(CRC).toUpperCase();
    }

    /**
     * 指令校验和,并取出后两位字节
     */
    public static String getSum16(byte[] msg, int length) {
        long mSum = 0;
        byte[] mByte = new byte[length];

        /** 逐Byte添加位数和 */
        for (byte byteMsg : msg) {
            long mNum = ((long) byteMsg >= 0) ? (long) byteMsg : ((long) byteMsg + 256);
            mSum += mNum;
        } /** end of for (byte byteMsg : msg) */

        /** 位数和转化为Byte数组 */
        for (int liv_Count = 0; liv_Count < length; liv_Count++) {
            mByte[length - liv_Count - 1] = (byte) (mSum >> (liv_Count * 8) & 0xff);
        } /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */
        return byteToStr(msg, length) + byteToStr(mByte, mByte.length).substring(byteToStr(mByte, mByte.length).length() - 4, byteToStr(mByte, mByte.length).length());
    }

    /**
     * 指令校验和,并取出后两位字节
     */
    public static String getSum8(byte[] msg, int length) {
        byte b = calcCrc8(msg);
        byte[] mByte = new byte[]{b};
        return byteToStr(msg, length) + byteToStr(mByte, mByte.length);
    }

    /**
     * 计算数组的CRC8校验值
     *
     * @param data 需要计算的数组
     *             [url=home.php?mod=space&uid=309376]@return[/url] CRC8校验值
     */
    public static byte calcCrc8(byte[] data) {
        return calcCrc8(data, 0, data.length, (byte) 0);
    }

    /**
     * 计算CRC8校验值
     *
     * @param data   数据
     * @param offset 起始位置
     * @param len    长度
     * @param preval 之前的校验值
     * @return 校验值
     */
    public static byte calcCrc8(byte[] data, int offset, int len, byte preval) {
        byte ret = preval;
        for (int i = offset; i < (offset + len); ++i) {
            ret = crc8_tab[(0x00ff & (ret ^ data[i]))];
        }
        return ret;
    }

    static byte[] crc8_tab = {(byte) 0, (byte) 94, (byte) 188, (byte) 226,
            (byte) 97, (byte) 63, (byte) 221, (byte) 131, (byte) 194,
            (byte) 156, (byte) 126, (byte) 32, (byte) 163, (byte) 253,
            (byte) 31, (byte) 65, (byte) 157, (byte) 195, (byte) 33,
            (byte) 127, (byte) 252, (byte) 162, (byte) 64, (byte) 30,
            (byte) 95, (byte) 1, (byte) 227, (byte) 189, (byte) 62, (byte) 96,
            (byte) 130, (byte) 220, (byte) 35, (byte) 125, (byte) 159,
            (byte) 193, (byte) 66, (byte) 28, (byte) 254, (byte) 160,
            (byte) 225, (byte) 191, (byte) 93, (byte) 3, (byte) 128,
            (byte) 222, (byte) 60, (byte) 98, (byte) 190, (byte) 224, (byte) 2,
            (byte) 92, (byte) 223, (byte) 129, (byte) 99, (byte) 61,
            (byte) 124, (byte) 34, (byte) 192, (byte) 158, (byte) 29,
            (byte) 67, (byte) 161, (byte) 255, (byte) 70, (byte) 24,
            (byte) 250, (byte) 164, (byte) 39, (byte) 121, (byte) 155,
            (byte) 197, (byte) 132, (byte) 218, (byte) 56, (byte) 102,
            (byte) 229, (byte) 187, (byte) 89, (byte) 7, (byte) 219,
            (byte) 133, (byte) 103, (byte) 57, (byte) 186, (byte) 228,
            (byte) 6, (byte) 88, (byte) 25, (byte) 71, (byte) 165, (byte) 251,
            (byte) 120, (byte) 38, (byte) 196, (byte) 154, (byte) 101,
            (byte) 59, (byte) 217, (byte) 135, (byte) 4, (byte) 90, (byte) 184,
            (byte) 230, (byte) 167, (byte) 249, (byte) 27, (byte) 69,
            (byte) 198, (byte) 152, (byte) 122, (byte) 36, (byte) 248,
            (byte) 166, (byte) 68, (byte) 26, (byte) 153, (byte) 199,
            (byte) 37, (byte) 123, (byte) 58, (byte) 100, (byte) 134,
            (byte) 216, (byte) 91, (byte) 5, (byte) 231, (byte) 185,
            (byte) 140, (byte) 210, (byte) 48, (byte) 110, (byte) 237,
            (byte) 179, (byte) 81, (byte) 15, (byte) 78, (byte) 16, (byte) 242,
            (byte) 172, (byte) 47, (byte) 113, (byte) 147, (byte) 205,
            (byte) 17, (byte) 79, (byte) 173, (byte) 243, (byte) 112,
            (byte) 46, (byte) 204, (byte) 146, (byte) 211, (byte) 141,
            (byte) 111, (byte) 49, (byte) 178, (byte) 236, (byte) 14,
            (byte) 80, (byte) 175, (byte) 241, (byte) 19, (byte) 77,
            (byte) 206, (byte) 144, (byte) 114, (byte) 44, (byte) 109,
            (byte) 51, (byte) 209, (byte) 143, (byte) 12, (byte) 82,
            (byte) 176, (byte) 238, (byte) 50, (byte) 108, (byte) 142,
            (byte) 208, (byte) 83, (byte) 13, (byte) 239, (byte) 177,
            (byte) 240, (byte) 174, (byte) 76, (byte) 18, (byte) 145,
            (byte) 207, (byte) 45, (byte) 115, (byte) 202, (byte) 148,
            (byte) 118, (byte) 40, (byte) 171, (byte) 245, (byte) 23,
            (byte) 73, (byte) 8, (byte) 86, (byte) 180, (byte) 234, (byte) 105,
            (byte) 55, (byte) 213, (byte) 139, (byte) 87, (byte) 9, (byte) 235,
            (byte) 181, (byte) 54, (byte) 104, (byte) 138, (byte) 212,
            (byte) 149, (byte) 203, (byte) 41, (byte) 119, (byte) 244,
            (byte) 170, (byte) 72, (byte) 22, (byte) 233, (byte) 183,
            (byte) 85, (byte) 11, (byte) 136, (byte) 214, (byte) 52,
            (byte) 106, (byte) 43, (byte) 117, (byte) 151, (byte) 201,
            (byte) 74, (byte) 20, (byte) 246, (byte) 168, (byte) 116,
            (byte) 42, (byte) 200, (byte) 150, (byte) 21, (byte) 75,
            (byte) 169, (byte) 247, (byte) 182, (byte) 232, (byte) 10,
            (byte) 84, (byte) 215, (byte) 137, (byte) 107, 53};


    /**
     * 将雷管输入的UID进行处理
     */
    public static String getUid2(String uid) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(uid)) {
            String str = "";
            if (uid.length() == 13) {
                sb.append(uid.substring(0,2) + "000");
                str =  uid.substring(2, uid.length());
            } else if (uid.length() == 15) {
                sb.append(uid.substring(0,4) + 0);
                str =  uid.substring(4, uid.length());
            }
            int year = Integer.parseInt(str.substring(0, 1));
            String sixYear = Integer.toHexString(year);
            sb.append(sixYear);
            int month = Integer.parseInt(str.substring(1, 3));
            String sixMonth = Integer.toHexString(month);
            if (sixMonth.length() == 1)
                sb.append("0");
            sb.append(sixMonth);
            int day = Integer.parseInt(str.substring(3, 5));
            String sixDay = Integer.toHexString(day);
            if (sixDay.length() == 1)
                sb.append("0");
            sb.append(sixDay);
            sb.append(str.substring(5, str.length()));
        }
        return sb.toString();
    }

    /**
     * 拼接当前雷管数量并转成十六进制
     */
    public static String getCurrentDetonatorNum(int num) {
        String number = Integer.toHexString(num);
        if (number.length() == 1)
            number = "000" + number;
        else if (number.length() == 2)
            number = "00" + number;
        else if (number.length() == 3)
            number = "0" + number;
        return number;
    }


    /**
     * 将十六进制串转换为二进制
     */
    public static String HexToBin8(String hexStr) {
        int ten = Integer.parseInt(hexStr, 16);
        String binStr = Integer.toBinaryString(ten);
        if (binStr.length() < 8) {
            for (int i = binStr.length(); i < 8; i++) {
                binStr = "0" + binStr;
            }
        }
        return binStr;
    }
}
