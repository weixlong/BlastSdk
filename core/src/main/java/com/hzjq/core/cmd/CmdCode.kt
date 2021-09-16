package com.hzjq.core.cmd


/**
 * 通讯代码
 */
class CmdCode {

    companion object {


        /**===================================控制板start==================================**/

        /**
         * 清除控制板状态
         */
        const val KZB_CLEAR_STATE_CMD = "2307FFA031CC"

        /**
         * 清除控制板数据成功
         */
        const val KZB_CLEAR_STATE_OK_ASK = "2308A0FF31CC81"

        /**
         * 清除控制板数据失败
         */
        const val KZB_CLEAR_STATE_E_ASK = "2308A0FF31CC80"

        /**
         * 在线扫描控制板雷管信息
         */
        const val KZB_SCAN_LG_CMD = "2307FFA03105"


        /**
         * 在线扫描雷管信息成功
         */
        const val KZB_SCAN_LG_OK_ASK = "2308A0FF310581"


        /**
         * 在线扫描雷管信息失败
         */
        const val KZB_SCAN_LG_E_ASK = "2308A0FF310580"

        /**
         * 读取控制板雷管数据命令
         */
        const val KZB_READ_LG_INFO_CMD = "2309FFA03004"

        /**
         * 读取控制板雷管数据信息返回
         */
        const val KZB_READ_LG_INFO_ASK = "231CA0FF3104"

        /**
         * 循环查询控制板雷管信息
         */
        const val KZB_QUERY_LG_INFO_CMD = "2307FFA030AA"

        /**
         * 循环查询返回成功
         */
        const val KZB_QUERY_LG_INFO_OK_ASK = "2313A0FF31AA"

        /**===================================控制板end==================================**/


        /**===================================组网start==================================**/
        /**
         * 延时方案
         */
        const val DELAY_PLAN_CMD = "231CFFA03103"


        /**
         * 延时方案返回成功
         */
        const val DELAY_PLAN_OK_ASK = "2308A0FF310381"


        /**
         * 延时方案返回失败
         */
        const val DELAY_PLAN_E_ASK = "2308A0FF310380"


        /**
         * 写入延时
         */
        const val DELAY_INPUT_CMD = "2308FFA03107"

        /**
         * 写入延时成功
         */
        const val DELAY_INPUT_OK_ASK = "2308A0FF310781"


        /**
         * 写入延时失败
         */
        const val DELAY_INPUT_E_ASK = "2308A0FF310780"




        /**===================================组网end==================================**/


        /**===================================爆破start==================================**/

        /**
         * 授权
         */
        const val AUTH_CMD = "2307FFA03106"

        /**
         * 授权成功
         */
        const val AUTH_OK_ASK = "2308A0FF310681"


        /**
         * 授权失败
         */
        const val AUTH_E_ASK = "2308A0FF310680"


        /**
         * 充电
         */
        const val BLAST_CHARGE_CMD = "2307FFA03108"

        /**
         * 充电成功
         */
        const val BLAST_CHARGE_OK_ASK = "2308A0FF310881"

        /**
         * 充电失败
         */
        const val BLAST_CHARGE_E_ASK = "2308A0FF310880"

        /**
         * 爆破
         */
        const val BLAST_CMD = "2308FFA03109"

        /**
         * 爆破成功
         */
        const val BLAST_OK_ASK = "2308A0FF310981"

        /**
         * 爆破失败
         */
        const val BLAST_E_ASK = "2308A0FF310980"


        /**
         * 关电
         */
        const val CLOSE_ELECTRIC_CMD = "2307FFA0310A"


        /**
         * 关电成功
         */
        const val CLOSE_ELECTRIC_OK_ASK = "2308A0FF310A81"


        /**
         * 关电失败
         */
        const val CLOSE_ELECTRIC_E_ASK = "2308A0FF310A80"

        /**
         * 设置电压
         */
        const val SETTING_VOLTAGE_CMD = "2310FFA031C8"

        /**
         * 设置电压成功
         */
        const val SETTING_VOLTAGE_OK_ASK = "2308A0FF31C881"

        /**
         * 设置电压失败
         */
        const val SETTING_VOLTAGE_E_ASK = "2308A0FF31C880"


        /**===================================爆破end==================================**/


        /**=================================升级start=============================**/
        //  读取控制板版本
        const val GET_CONTROL_VERSION = "2307FFA031BB"

        //  进入升级left部分
        const val INTO_UPGRADE_LEFT = "230AFF"

        //  进入升级right部分
        const val INTO_UPGRADE_RIGHT = "41FF010000"

        //  发送升级地址left部分
        const val SEND_UPGRADE_ADDRESS_LEFT = "230DFF"

        //  发送升级地址middle部分
        const val SEND_UPGRADE_ADDRESS_MIDDLE = "41FE"

        //  发送升级地址right部分
        const val SEND_UPGRADE_ADDRESS_RIGHT = "0800"

        //  发送写入指令left部分
        const val SEND_INPUT_LEFT = "2308FF"

        //  发送写入指令right部分
        const val SEND_INPUT_RIGHT = "41FD01"

        //  退出升级left部分
        const val OUT_UPGRADE_LEFT = "230AFF"

        //  退出升级right部分
        const val OUT_UPGRADE_RIGHT = "41FF02"

        const val INTO_UPGRADE_ASK = "230AB0FF81"//起爆器版本进入升级返回

        const val INTO_UPGRADE_E_ASK = "230AB0FF80"//起爆器版本进入升级返回失败

        const val OUT_UPGRADE_ASK = "2308B0FF81"//起爆器版本退出升级返回前段

        const val OUT_UPGRADE_E_ASK = "2308B0FF80"//起爆器版本退出升级返回前段

        const val SEND_UPGRADE_ADDRESS_ASK = "2307B0FF"//起爆器地址返回信息前段

        const val SEND_INPUT_ASK = "2307B0FF"//起爆器写入返回信息前段
        /**=================================升级end=============================**/
    }
}