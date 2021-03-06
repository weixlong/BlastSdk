package com.hzjq.core

import com.hzjq.core.callback.Callback
import com.hzjq.core.callback.OnVersionCallback
import com.hzjq.core.callback.OnVersionUpgradeCallback
import com.hzjq.core.loader.*
import com.hzjq.core.receive.ReceiverInterceptor
import java.io.File


/**
 * 起爆操作入口类
 */
class Blast {

    private constructor()

    private object B {
        val b = Blast()
    }

    companion object {

        /**
         * 获取起爆入口类，单例
         */
        fun getInstance(): Blast {
            return B.b
        }
    }

    /**
     * 是否测试模式
     */
    fun setDebug(debug:Boolean){
        BlastDelegate.getDelegate().setDebug(debug)
    }


    /**
     * 获取单发检测器
     */
    fun getAlongCapCheckLoader():AlongCapCheckLoader{
        return BlastDelegate.getDelegate().getAlongCapCheckLoader()
    }

    /**
     * 获取扫描加载器
     */
    fun getScannerLoader():ScannerLoader{
        return BlastDelegate.getDelegate().getScannerLoader()
    }


    /**
     * 获取软件版本
     */
    fun getVersion(callback: OnVersionCallback){
        BlastDelegate.getDelegate().getVersionLoader().getVersion(callback)
    }

    /**
     * 升级版本，文件件必须以.bin结尾，否则将不做升级
     * 版本升级中断，将会导致升级失败，芯片无法运行
     */
    fun upgrade(targetVersion:Int,binFile: File,callback: OnVersionUpgradeCallback){
        BlastDelegate.getDelegate().getVersionUpgradeLoader().onVersionUpgrade(targetVersion,binFile, callback)
    }

    /**
     * 退出升级模式
     * 如升级意外中断，必须调用退出升级才能继续正常使用
     * 正常升级完成后，不需要调用此方法，框架已处理升级成功或失败的情况
     */
    fun exitUpgradeMode(targetVersion:Int,callback: Callback<Boolean>){
        BlastDelegate.getDelegate().getUpgradeExitLoader().onUpgradeExit(targetVersion,callback)
    }

    /**
     * 扫描雷管数据
     */
    fun scanCap(): OnScanCapLoader {
       return BlastDelegate.getDelegate().getScanCapLoader()
    }


    /**
     * 下传雷管信息
     * 将雷管数据写入芯片
     * 下传前请从民爆服务器中获取密码，并设置到password属性中
     */
    fun underCap(): OnUnderCapLoader {
      return  BlastDelegate.getDelegate().getUnderCapLoader()
    }

    /**
     * 雷管授权
     * onResult返回授权雷管结果
     * 授权之前请先对雷管进行下传，否则会授权失败
     */
    fun authCap(): OnAuthCapLoader {
        return BlastDelegate.getDelegate().getAuthCapLoader()
    }


    /**
     * 雷管充电
     */
    fun charge(): OnChargeLoader {
       return BlastDelegate.getDelegate().getChargeLoader()
    }


    /**
     * 爆破
     * 爆破前请先完成充电操作
     */
    fun blast(): OnBlastLoader {
      return  BlastDelegate.getDelegate().getBlastLoader()
    }


    /**
     * 一键下传并授权
     */
    fun quickRegister():OnQuickRegisterLoader{
       return BlastDelegate.getDelegate().getQuickRegisterLoader()
    }

    /**
     * 清除占用加载器
     */
    fun getClearOccupyLoader():ClearOccupyLoader{
        return BlastDelegate.getDelegate().getClearOccupyLoader()
    }

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: ReceiverInterceptor){
        BlastDelegate.getDelegate().addInterceptor(interceptor)
    }
}