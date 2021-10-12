package com.hzjq.core

import com.hzjq.core.bean.ErrorResult

class ErrorCode {

    companion object {

        private val errorMap = HashMap<Int, ErrorResult>()
        private val retryError = HashMap<Int,ErrorResult>()

        fun loadErrorCode(){
            /** ====================  提示终止型错误 =======================**/
            errorMap[1] = ErrorResult(-1,"雷管检测，有新增雷管")
            errorMap[2] = ErrorResult(-2,"雷管检测，有雷管复位")
            errorMap[3] = ErrorResult(-3,"雷管检测，未检测到雷管反馈")
            errorMap[4] = ErrorResult(-4,"雷管检测，雷管数量和方案不一致")

            errorMap[20] = ErrorResult(-5,"雷管授权，有新增雷管")
            errorMap[21] = ErrorResult(-6,"雷管授权，存在未授权的雷管")
            errorMap[22] = ErrorResult(-7,"雷管授权，全部雷管授权失败")
            errorMap[23] = ErrorResult(-8,"雷管授权，有雷管授权错误")

            errorMap[30] = ErrorResult(-9,"雷管写延时，有新增雷管")
            errorMap[31] = ErrorResult(-10,"雷管写延时，有雷管复位")
            errorMap[32] = ErrorResult(-11,"雷管写延时，全部雷管写延时失败")
            errorMap[33] = ErrorResult(-12,"雷管写延时，有雷管写延时错误")

            errorMap[40] = ErrorResult(-13,"雷管校时，有新增雷管")
            errorMap[41] = ErrorResult(-14,"雷管校时，有雷管复位")
            errorMap[42] = ErrorResult(-15,"雷管校时，全部雷管校时失败")
            errorMap[43] = ErrorResult(-16,"雷管校时，有雷管校时错误")

            errorMap[50] = ErrorResult(-17,"雷管充电，有新增雷管")
            errorMap[51] = ErrorResult(-18,"雷管充电，有雷管复位")
            errorMap[52] = ErrorResult(-19,"雷管充电，全部雷管充电失败")
            errorMap[53] = ErrorResult(-20,"雷管充电，有雷管充电错误")

            errorMap[60] = ErrorResult(-21,"雷管起爆，雷管状态全部异常")
            errorMap[61] = ErrorResult(-22,"雷管起爆，有雷管状态异常")

            errorMap[101] = ErrorResult(-23,"未检测到雷管")
            errorMap[102] = ErrorResult(-24,"雷管数已满，雷管数量超出范围")
            errorMap[103] = ErrorResult(-25,"起爆器上电失败")
            errorMap[104] = ErrorResult(-26,"起爆器输出短路，请检查线路连接")
            errorMap[105] = ErrorResult(-27,"起爆器输出开路，请检查线路连接")
            errorMap[106] = ErrorResult(-28,"充电高压升压失败")
            errorMap[107] = ErrorResult(-29,"充电降压查询电压失败")
            errorMap[108] = ErrorResult(-30,"充电还原高压失败")
            errorMap[109] = ErrorResult(-31,"起爆降压查询电压失败")
            errorMap[110] = ErrorResult(-32,"起爆器电流异常")
            errorMap[111] = ErrorResult(-33,"电流严重异常，可能造成大规模拒爆")
            errorMap[112] = ErrorResult(-34,"起爆器等待电流稳定超时")
            /** ====================  提示终止型错误 =======================**/


            /** ====================  提示选择型错误 =======================**/
            errorMap[181] = ErrorResult(-35,"电流异常，可能造成拒爆")
            /** ====================  提示选择型错误 =======================**/



            /** ====================  操作失败错误 =======================**/
            errorMap[-1] = ErrorResult(-36,"单发雷管检测失败")
            errorMap[-2] = ErrorResult(-37,"扫描授权信息失败")
            errorMap[-3] = ErrorResult(-38,"授权查询失败")
            errorMap[-4] = ErrorResult(-39,"写入授权信息失败")
            errorMap[-5] = ErrorResult(-40,"查询起爆结果超时")
            errorMap[-6] = ErrorResult(-41,"爆破失败")
            errorMap[-7] = ErrorResult(-42,"充电失败")
            errorMap[-8] = ErrorResult(-43,"进入充电模式失败")
            errorMap[-9] = ErrorResult(-44,"雷管数据为空,请检查")
            errorMap[-10] = ErrorResult(-45,"有雷管密码未设置")
            errorMap[-11] = ErrorResult(-47,"清除底板数据失败")
            errorMap[-12] = ErrorResult(-48,"下电失败")

            errorMap[-13] = ErrorResult(-49,"进入授权模式失败")
            errorMap[-14] = ErrorResult(-50,"进入扫描模式失败")
            errorMap[-15] = ErrorResult(-51,"读取雷管信息失败")
            errorMap[-16] = ErrorResult(-52,"读取雷管信息失败")
            errorMap[-17] = ErrorResult(-53,"文件解析失败,请检查文件后缀与大小是否正确")
            errorMap[-18] = ErrorResult(-54,"文件解析失败,请检查文件内容是否正确")
            errorMap[-19] = ErrorResult(-55,"文件解析失败,退出升级模式失败")
            errorMap[-20] = ErrorResult(-56,"扫描雷管信息失败")
            errorMap[-21] = ErrorResult(-57,"下传雷管信息失败")
            errorMap[-22] = ErrorResult(-58,"退出升级模式失败")
            errorMap[-23] = ErrorResult(-59,"进入升级模式失败")
            errorMap[-24] = ErrorResult(-60,"获取当前版本失败")
            errorMap[-25] = ErrorResult(-61,"报文信息返回错误")
            errorMap[-26] = ErrorResult(-62,"重试次数完成后失败")
            errorMap[-27] = ErrorResult(-68,"打开底板电源失败")
            errorMap[-28] = ErrorResult(-69,"串口没有读取权限，请先申请文件读取权限")
            errorMap[-29] = ErrorResult(-70,"打开串口时，发生未知错误")
            errorMap[-30] = ErrorResult(-71,"打开串口参数不正确，请检查")
            /** ====================  操作失败错误 =======================**/


            /** ====================  底板在重试 =======================**/
            retryError[151] = ErrorResult(-63,"雷管检测重试中，请等待")
            retryError[152] = ErrorResult(-64,"雷管授权重试中，请等待")
            retryError[153] = ErrorResult(-65,"雷管写延时重试中，请等待")
            retryError[154] = ErrorResult(-66,"雷管校时重试中，请等待")
            retryError[155] = ErrorResult(-67,"雷管充电重试中，请等待")
            /** ====================  底板在重试 =======================**/

        }



        fun getErrorResult(code:Int):ErrorResult{
            if(errorMap.containsKey(code)){
                return errorMap[code]!!
            }
            return ErrorResult(code,"未知错误")
        }


        fun getRetryResult(code: Int):ErrorResult?{
            return retryError[code]
        }



    }
}