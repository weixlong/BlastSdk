package com.hzjq.core

import com.hzjq.core.loader.*
import com.hzjq.core.parse.ParseLoader

class Option {

    private var cmdType = "B0" //起爆器类型  C0：采集器类型
    private var debug = false
    private var receiveOutTime = 5L//接收超时ms
    private var isDelayWriteData = false//是否写入延时数据
    private var retryCount = 3//重试次数
    private var maxSupportCapCount = 600 //支持最大雷管数
    private lateinit var authCapLoader: OnAuthCapLoader
    private lateinit var blastLoader: OnBlastLoader
    private lateinit var chargeLoader: OnChargeLoader
    private lateinit var quickCheckAuthLoader: OnQuickCheckAuthLoader
    private lateinit var scanCapLoader: OnScanCapLoader
    private lateinit var underCapLoader: OnUnderCapLoader
    private lateinit var versionLoader: OnVersionLoader
    private lateinit var versionUpgradeLoader: OnVersionUpgradeLoader
    private lateinit var assemblyCmdLoader: AssemblyCmdLoader
    private lateinit var cmdExeLoader: CmdExeLoader
    private lateinit var sendMessageLoader: OnSendMessageLoader
    private lateinit var parseLoader: ParseLoader
    private lateinit var onUpgradeExitLoader: OnUpgradeExitLoader


    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun isDebug(): Boolean {
        return debug
    }

    fun setMaxSupportCapCount(maxSupportCapCount:Int):Option{
        this.maxSupportCapCount = maxSupportCapCount
        return this
    }

    fun getMaxSupportCapCount():Int{
        return maxSupportCapCount
    }

    fun setRetryCount(count:Int):Option{
        this.retryCount = count
        return this
    }

    fun getRetryCount():Int{
        return retryCount
    }

    fun setUpgradeExitLoader(onUpgradeExitLoader: OnUpgradeExitLoader): Option {
        this.onUpgradeExitLoader = onUpgradeExitLoader
        return this
    }

    fun getOnUpgradeExitLoader(): OnUpgradeExitLoader {
        return onUpgradeExitLoader
    }

    fun setDelayWriteData(isDelayWriteData: Boolean): Option {
        this.isDelayWriteData = isDelayWriteData
        return this
    }

    fun isDelayWriteData(): Boolean {
        return isDelayWriteData
    }

    fun setCmdType(type: String): Option {
        this.cmdType = type
        return this
    }

    fun getCmdType(): String {
        return cmdType
    }


    fun getParseLoader(): ParseLoader {
        return parseLoader
    }

    fun setParseLoader(parseLoader: ParseLoader): Option {
        this.parseLoader = parseLoader
        return this
    }

    fun setReceiveOutTime(time: Long): Option {
        receiveOutTime = time
        return this
    }

    fun getReceiveOutTime(): Long {
        return receiveOutTime
    }

    fun setAuthCapLoader(authCapLoader: OnAuthCapLoader): Option {
        this.authCapLoader = authCapLoader
        return this
    }


    fun getAuthCapLoader(): OnAuthCapLoader {
        return authCapLoader
    }


    fun setBlastLoader(blastLoader: OnBlastLoader): Option {
        this.blastLoader = blastLoader
        return this
    }


    fun getBlastLoader(): OnBlastLoader {
        return blastLoader
    }

    fun setChargeLoader(chargeLoader: OnChargeLoader): Option {
        this.chargeLoader = chargeLoader
        return this
    }


    fun getChargeLoader(): OnChargeLoader {
        return chargeLoader
    }


    fun setQuickCheckAuthLoader(quickCheckAuthLoader: OnQuickCheckAuthLoader): Option {
        this.quickCheckAuthLoader = quickCheckAuthLoader
        return this
    }


    fun getQuickCheckAuthLoader(): OnQuickCheckAuthLoader {
        return quickCheckAuthLoader
    }


    fun setScanCapLoader(scanCapLoader: OnScanCapLoader): Option {
        this.scanCapLoader = scanCapLoader
        return this
    }


    fun getScanCapLoader(): OnScanCapLoader {
        return scanCapLoader
    }

    fun setUnderCapLoader(underCapLoader: OnUnderCapLoader): Option {
        this.underCapLoader = underCapLoader
        return this
    }


    fun getUnderCapLoader(): OnUnderCapLoader {
        return underCapLoader
    }

    fun setVersionLoader(versionLoader: OnVersionLoader): Option {
        this.versionLoader = versionLoader
        return this
    }


    fun getVersionLoader(): OnVersionLoader {
        return versionLoader
    }


    fun setVersionUpgradeLoader(versionUpgradeLoader: OnVersionUpgradeLoader): Option {
        this.versionUpgradeLoader = versionUpgradeLoader
        return this
    }


    fun getVersionUpgradeLoader(): OnVersionUpgradeLoader {
        return versionUpgradeLoader
    }

    fun getAssemblyCmdLoader(): AssemblyCmdLoader {
        return assemblyCmdLoader
    }

    fun setAssemblyCmdLoader(assemblyCmdLoader: AssemblyCmdLoader): Option {
        this.assemblyCmdLoader = assemblyCmdLoader
        return this
    }

    fun getCmdExeLoader(): CmdExeLoader {
        return cmdExeLoader
    }

    fun setCmdExeLoader(cmdExeLoader: CmdExeLoader): Option {
        this.cmdExeLoader = cmdExeLoader
        return this
    }

    fun getOnSendMessageLoader(): OnSendMessageLoader {
        return sendMessageLoader
    }

    fun setSendMessageLoader(sendMessageLoader: OnSendMessageLoader): Option {
        this.sendMessageLoader = sendMessageLoader
        return this
    }
}