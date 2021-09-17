package com.hzjq.core

import android.os.Handler
import android.os.Looper
import com.hzjq.core.cmd.AssemblyCmd
import com.hzjq.core.cmd.CmdExeImpl
import com.hzjq.core.impl.*
import com.hzjq.core.loader.*
import com.hzjq.core.massage.MessageSender
import com.hzjq.core.parse.ParseLoader
import com.hzjq.core.parse.Parser

class BlastDelegate {

    private val option: Option = Option()

    private var handler: Handler = Handler(Looper.getMainLooper())

    private constructor() {
        option.setAuthCapLoader(AuthCapImpl())
            .setBlastLoader(BlastImpl())
            .setChargeLoader(ChargeImpl())
            .setQuickCheckAuthLoader(QuickCheckAuthImpl())
            .setScanCapLoader(ScanCapImpl())
            .setUnderCapLoader(UnderCapImpl())
            .setVersionLoader(VersionImpl())
            .setVersionUpgradeLoader(VersionUpgradeImpl())
            .setAssemblyCmdLoader(AssemblyCmd())
            .setCmdExeLoader(CmdExeImpl())
            .setSendMessageLoader(MessageSender())
            .setParseLoader(Parser())
            .setUpgradeExitLoader(UpgradeExitImpl())
            .setReceiveOutTime(5)
            .setRetryCount(3)
            .setCmdType("B0")
            .setDelayWriteData(false)
            .setMaxSupportCapCount(600)
    }

    companion object {
        private val delegate = BlastDelegate()
        fun getDelegate(): BlastDelegate {
            return delegate
        }
    }

    /**
     * 是否测试
     */
    fun setDebug(debug:Boolean){
        option.setDebug(debug)
    }

    /**
     * 是否测试
     */
    fun isDebug():Boolean{
        return option.isDebug()
    }

    /**
     * 获得升级对出加载器
     */
    fun getUpgradeExitLoader():OnUpgradeExitLoader{
        return option.getOnUpgradeExitLoader()
    }

    /**
     * 支持的最大雷管数
     */
    fun getMaxSupportCapCount():Int{
        return option.getMaxSupportCapCount()
    }

    /**
     * 是否写人延时
     */
    fun setDelayWriteData(isWrite:Boolean){
        option.setDelayWriteData(isWrite)
    }

    /**
     * 是否写人延时
     */
    fun isDelayWriteData():Boolean{
        return option.isDelayWriteData()
    }


    /**
     * 获取失败重试次数
     */
    fun getRetryCount():Int{
        return option.getRetryCount()
    }

    /**
     * 获取操作的对象
     */
    fun getCmdType(): String {
        return option.getCmdType()
    }

    /**
     * 获得接收超时时间
     */
    fun getReceiveOutTime():Long{
        return option.getReceiveOutTime()
    }

    /**
     * 获得解析器
     */
    fun getParseLoader(): ParseLoader{
        return option.getParseLoader()
    }

    /**
     * 获取命令加载器
     */
    fun getAssemblyCmdLoader(): AssemblyCmdLoader {
        return option.getAssemblyCmdLoader()
    }


    /**
     * 获取命令执行器
     */
    fun getCmdExeLoader(): CmdExeLoader {
        return option.getCmdExeLoader()
    }

    /**
     * 获取消息发送器
     */
    fun getOnSendMessageLoader():OnSendMessageLoader{
        return option.getOnSendMessageLoader()
    }

    /**
     * 获得授权加载器
     */
    fun getAuthCapLoader(): OnAuthCapLoader {
        return option.getAuthCapLoader()
    }


    /**
     * 获取起爆加载器
     */
    fun getBlastLoader(): OnBlastLoader {
        return option.getBlastLoader()
    }


    /**
     * 获取充电加载器
     */
    fun getChargeLoader(): OnChargeLoader {
        return option.getChargeLoader()
    }


    /**
     * 获取一键起爆器
     */
    fun getQuickCheckAuthLoader(): OnQuickCheckAuthLoader {
        return option.getQuickCheckAuthLoader()
    }

    /**
     * 获取扫描器
     */
    fun getScanCapLoader(): OnScanCapLoader {
        return option.getScanCapLoader()
    }


    /**
     * 获取下传器
     */
    fun getUnderCapLoader(): OnUnderCapLoader {
        return option.getUnderCapLoader()
    }


    /**
     * 获取版本器
     */
    fun getVersionLoader(): OnVersionLoader {
        return option.getVersionLoader()
    }


    /**
     * 获取版本升级器
     */
    fun getVersionUpgradeLoader(): OnVersionUpgradeLoader {
        return option.getVersionUpgradeLoader()
    }

    /**
     * 主线程里执行一个任务
     */
    fun post(r: Runnable) {
        handler.post(r)
    }

}