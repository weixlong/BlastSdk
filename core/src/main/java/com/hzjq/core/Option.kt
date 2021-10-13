package com.hzjq.core

import com.hzjq.core.loader.*
import com.hzjq.core.parse.ParseLoader

class Option {

    private var cmdType = "B0" //起爆器类型  C0：采集器类型
    private var debug = false
    private var receiveOutTime = 5L//接收超时ms
    private var isDelayWriteData = true//是否写入延时数据
    private var isStandardUid = false//是否使用最新标准的uid
    private var outTimeRetryCount = 3//超时重试次数
    private var failedRetryCount = 3//失败重试次数
    private var upgradeWriteRetryCount = 3 // 升级写入地址失败次数
    private var maxSupportCapCount = 600 //支持最大雷管数
    private var blastOutTime = 8000L//起爆超市时间
    private var serialWriteSleepTime = 5L//写入时休眠时间
    private var serialReadSleepTime = 5L//读取时的休眠时间
    private lateinit var authCapLoader: OnAuthCapLoader
    private lateinit var blastLoader: OnBlastLoader
    private lateinit var chargeLoader: OnChargeLoader
    private lateinit var quickRegisterLoader: OnQuickRegisterLoader
    private lateinit var scanCapLoader: OnScanCapLoader
    private lateinit var underCapLoader: OnUnderCapLoader
    private lateinit var versionLoader: OnVersionLoader
    private lateinit var versionUpgradeLoader: OnVersionUpgradeLoader
    private lateinit var assemblyCmdLoader: AssemblyCmdLoader
    private lateinit var cmdExeLoader: CmdExeLoader
    private lateinit var sendMessageLoader: OnSendMessageLoader
    private lateinit var parseLoader: ParseLoader
    private lateinit var onUpgradeExitLoader: OnUpgradeExitLoader
    private lateinit var scannerLoader: ScannerLoader
    private lateinit var alongCapCheckLoader: AlongCapCheckLoader
    private lateinit var clearOccupyLoader: ClearOccupyLoader


    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    fun isDebug(): Boolean {
        return debug
    }

    fun setStandardUid(isStandard:Boolean):Option{
        this.isStandardUid = isStandard
        return this
    }

    fun isStandardUid():Boolean{
        return isStandardUid
    }

    fun setSerialWriteSleepTime(time: Long): Option {
        if (time < 0) return this
        if (time + serialReadSleepTime < receiveOutTime) {
            this.serialWriteSleepTime = time
        }
        return this
    }

    fun getSerialWriteSleepTime(): Long {
        return serialWriteSleepTime
    }

    fun setSerialReadSleepTime(time: Long): Option {
        if (time < 0) return this
        if (time + serialWriteSleepTime < receiveOutTime) {
            this.serialReadSleepTime = time
        }
        return this
    }

    fun getSerialReadSleepTime(): Long {
        return serialReadSleepTime
    }

    fun setBlastOutTime(time: Long): Option {
        if (time < 0) return this
        this.blastOutTime = time
        return this
    }

    fun getBlastOutTime(): Long {
        return blastOutTime
    }

    fun setAlongCapCheckLoader(alongCapCheckLoader: AlongCapCheckLoader): Option {
        this.alongCapCheckLoader = alongCapCheckLoader
        return this
    }

    fun getAlongCapCheckLoader(): AlongCapCheckLoader {
        return this.alongCapCheckLoader
    }

    fun setMaxSupportCapCount(maxSupportCapCount: Int): Option {
        if (maxSupportCapCount < 0) return this
        this.maxSupportCapCount = maxSupportCapCount
        return this
    }

    fun getMaxSupportCapCount(): Int {
        return maxSupportCapCount
    }

    fun setOutTimeRetryCount(count: Int): Option {
        if (count < 0) return this
        this.outTimeRetryCount = count
        return this
    }

    fun getOutTimeRetryCount(): Int {
        return outTimeRetryCount
    }

    fun setFailedRetryCount(count: Int): Option {
        if (count < 0) return this
        this.failedRetryCount = count
        return this
    }

    fun getFailedRetryCount(): Int {
        return failedRetryCount
    }

    fun setUpgradeWriteRetryCount(count: Int): Option {
        if (count < 0) return this
        this.upgradeWriteRetryCount = count
        return this
    }

    fun getUpgradeWriteRetryCount(): Int {
        return upgradeWriteRetryCount
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
        if (time < 0) return this
        if (time >= serialReadSleepTime + serialWriteSleepTime) {
            receiveOutTime = time
        }
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


    fun setScannerLoader(scannerLoader: ScannerLoader): Option {
        this.scannerLoader = scannerLoader
        return this
    }


    fun getScannerLoader(): ScannerLoader {
        return this.scannerLoader
    }

    fun setChargeLoader(chargeLoader: OnChargeLoader): Option {
        this.chargeLoader = chargeLoader
        return this
    }


    fun getChargeLoader(): OnChargeLoader {
        return chargeLoader
    }


    fun setQuickRegisterLoader(quickRegisterLoader: OnQuickRegisterLoader): Option {
        this.quickRegisterLoader = quickRegisterLoader
        return this
    }


    fun getQuickRegisterLoader(): OnQuickRegisterLoader {
        return quickRegisterLoader
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

    fun setClearOccupyLoader(clearOccupyLoader: ClearOccupyLoader) : Option{
        this.clearOccupyLoader = clearOccupyLoader
        return this
    }

    fun getClearOccupyLoader():ClearOccupyLoader{
        return clearOccupyLoader
    }
}