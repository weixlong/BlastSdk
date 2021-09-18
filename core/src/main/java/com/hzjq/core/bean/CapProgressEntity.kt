package com.hzjq.core.bean

open class CapProgressEntity {
    var errorCode = ""
    var progress = 0
    var total = 0
    var stateCode = 0 //stateCode 在线雷管扫描状态 0 正常，1 上电失败，2 电流异常， 3 雷管数据已满， 4 未扫描到雷管，数量数量为0， 5 通信电压异常
    var mVoltage = 0.0
    var mElectric = 0.0
    var isEnd = false//本次操作是否已结束

    constructor(
        errorCode: String,
        progress: Int,
        total: Int,
        stateCode: Int,
        mVoltage: Double,
        mElectric: Double
    ) {
        this.errorCode = errorCode
        this.progress = progress
        this.total = total
        this.stateCode = stateCode
        this.mVoltage = mVoltage
        this.mElectric = mElectric
    }

    constructor()


    companion object {
        fun convertStateCode(stateCode: Int): Int {
            when (stateCode) {
                1 -> {//起爆器上电失败！
                    return -1
                }
                2 -> {//起爆器通信电压异常
                    return -16
                }
                3 -> {//起爆器上电电流异常
                    return -17
                }
                4 -> {//起爆器输出短路
                    return -18
                }
                5 -> {//雷管复位后电流异常
                    return -19
                }
                6 -> {//第1组低压充电电流异常
                    return -20
                }
                7 -> {//第2组低压充电电流异常
                    return -21
                }
                8 -> {//第3组低压充电电流异常
                    return -22
                }
                9 -> {//第4组低压充电电流异常
                    return -23
                }
                10 -> {//第5组低压充电电流异常
                    return -24
                }
                11 -> {//第6组低压充电电流异常
                    return -25
                }
                12 -> {//充电高压升压失败
                    return -26
                }
                13 -> {//充电高压充电电流异常
                    return -27
                }
                14 -> {//充电低压降压失败
                    return -28
                }
                15 -> {//充电低压降压电流异常
                    return -29
                }
                16 -> {//充电还原高压失败
                    return -30
                }
                17 -> {//充电还原高压充电电流异常
                    return -31
                }
                18 -> {//起爆低压降压失败
                    return -32
                }
                19 -> {//起爆低压降压电流异常
                    return -33
                }
                20 -> {//电流异常, 可能造成拒爆
                    return -34
                }
                21 -> {//电流严重异常, 可能造成大规模拒爆, 禁止起爆
                    return -35
                }
                100 -> {//雷管状态异常 (有雷管未分配到短地址，状态字第0bit位有反馈)
                    return -36
                }
                101 -> {//未检测到雷管，雷管数量为0
                    return -37
                }
                102 -> {//雷管数据已满，数量超出范围, 请检查雷管数量
                    return -38
                }
                103 -> {//雷管数量和方案不一致
                    return -39
                }
                104 -> {//雷管连接异常
                    return -40
                }
                105 -> {//雷管延时设置超出范围
                    return -41
                }
                106 -> {//雷管密码验证出错
                    return -42
                }
                107 -> {//雷管时间校准出错
                    return -43
                }
                108 -> {//雷管写延时出错
                    return -44
                }
                109 -> {//雷管写延时出错+雷管时间校准出错
                    return -45
                }
                110 -> {//雷管放电使能标志异常
                    return -46
                }
                111 -> {//雷管充电标志异常
                    return -47
                }
                112 -> {//起爆状态错误，终止起爆
                    return -48
                }
                113 -> {//起爆时间超时，终止起爆，雷管已充入高压, 30分钟内禁止进入爆破现场
                    return -49
                }

            }

            return stateCode
        }


        fun convertBlastError(stateCode: Int):Int{
            when (stateCode) {
                1 -> {//雷管使能异常
                    return -58
                }
                2 -> {//雷管数量不对
                    return -59
                }
                3 -> {//雷管状态错误
                    return -60
                }
                4 -> {//起爆状态错误
                    return -61
                }
            }
            return stateCode
        }
    }
}