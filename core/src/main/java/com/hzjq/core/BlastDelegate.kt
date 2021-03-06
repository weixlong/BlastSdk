package com.hzjq.core

import com.hzjq.core.callback.MainRunnable
import com.hzjq.core.callback.ThreadRunnable
import com.hzjq.core.cmd.AssemblyCmd
import com.hzjq.core.cmd.CmdExeImpl
import com.hzjq.core.impl.*
import com.hzjq.core.loader.*
import com.hzjq.core.massage.MessageSender
import com.hzjq.core.parse.ParseLoader
import com.hzjq.core.parse.Parser
import com.hzjq.core.receive.ReceiverInterceptor
import com.hzjq.core.receive.ReceiverInterceptorPool
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class BlastDelegate {

    private val option: Option = Option()

    private var handlerDisposable: Disposable? = null
    private var delayHandlerDisposable: Disposable? = null

    private constructor() {
        ErrorCode.loadErrorCode()
        option.setAuthCapLoader(AuthCapImpl())
            .setBlastLoader(BlastImpl())
            .setChargeLoader(ChargeImpl())
            .setQuickRegisterLoader(QuickRegisterImpl())
            .setScanCapLoader(ScanCapImpl())
            .setUnderCapLoader(UnderCapImpl())
            .setVersionLoader(VersionImpl())
            .setVersionUpgradeLoader(VersionUpgradeImpl())
            .setAssemblyCmdLoader(AssemblyCmd())
            .setCmdExeLoader(CmdExeImpl())
            .setSendMessageLoader(MessageSender())
            .setParseLoader(Parser())
            .setUpgradeExitLoader(UpgradeExitImpl())
            .setScannerLoader(ScannerImpl())
            .setAlongCapCheckLoader(AlongCapCheckImpl())
            .setClearOccupyLoader(ClearOccupyImpl())
            .setOutTimeRetryCount(3)//接收超时重试次数
            .setFailedRetryCount(3)//接收失败重试次数
            .setCmdType("B0")//起爆器类型
            .setDelayWriteData(true)//开启写入延时方案
            .setStandardUid(true) //是否使用最新标准的uid
            .setMaxSupportCapCount(600)//支持的最大组网雷管数
            .setBlastOutTime(8000)//起爆超时
            .setReceiveOutTime(2000)//接收超时时间,接收超时时间不得小于读取串口时间+写入休眠时间
            .setSerialReadSleepTime(3)//读取串口返回休眠时间,接收超时时间不得小于读取串口时间+写入休眠时间
            .setSerialWriteSleepTime(3)//写入串口数据休眠时间,接收超时时间不得小于读取串口时间+写入休眠时间
            .setUpgradeWriteRetryCount(3)//升级写入地址失败重试次数
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
    fun setDebug(debug: Boolean) {
        option.setDebug(debug)
    }

    /**
     * 是否测试
     */
    fun isDebug(): Boolean {
        return option.isDebug()
    }

    /**
     * 是否使用最新标准的uid
     */
    fun isStandardUid(): Boolean {
        return option.isStandardUid()
    }

    /**
     * 获取单发检测器
     */
    fun getAlongCapCheckLoader(): AlongCapCheckLoader {
        return option.getAlongCapCheckLoader()
    }

    /**
     * 获取扫描器
     */
    fun getScannerLoader(): ScannerLoader {
        return option.getScannerLoader()
    }


    /**
     * 获得升级对出加载器
     */
    fun getUpgradeExitLoader(): OnUpgradeExitLoader {
        return option.getOnUpgradeExitLoader()
    }

    /**
     * 获取串口写入数据的休眠时间（ms）
     */
    fun getSerialWriteSleepTime(): Long {
        return option.getSerialWriteSleepTime()
    }

    /**
     * 获取串口读取数据的休眠时间
     */
    fun getSerialReadSleepTime(): Long {
        return option.getSerialReadSleepTime()
    }

    /**
     * 支持的最大雷管数
     */
    fun getMaxSupportCapCount(): Int {
        return option.getMaxSupportCapCount()
    }

    /**
     * 是否写人延时
     */
    fun setDelayWriteData(isWrite: Boolean) {
        option.setDelayWriteData(isWrite)
    }

    /**
     * 起爆超时时间
     */
    fun getBlastOutTime(): Long {
        return option.getBlastOutTime()
    }

    /**
     * 是否写人延时
     */
    fun isDelayWriteData(): Boolean {
        return option.isDelayWriteData()
    }


    /**
     * 获取超时重试次数
     */
    fun getOutTimeRetryCount(): Int {
        return option.getOutTimeRetryCount()
    }

    /**
     * 获取超时重试次数
     */
    fun getFailedRetryCount(): Int {
        return option.getFailedRetryCount()
    }


    /**
     * 升级时写入地址失败次数
     */
    fun getUpgradeWriteRetryCount(): Int {
        return option.getUpgradeWriteRetryCount()
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
    fun getReceiveOutTime(): Long {
        return option.getReceiveOutTime()
    }

    /**
     * 获得解析器
     */
    fun getParseLoader(): ParseLoader {
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
    fun getOnSendMessageLoader(): OnSendMessageLoader {
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
    fun getQuickRegisterLoader(): OnQuickRegisterLoader {
        return option.getQuickRegisterLoader()
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
     * 清除占用加载器
     */
    fun getClearOccupyLoader(): ClearOccupyLoader {
        return option.getClearOccupyLoader()
    }

    /**
     * 添加拦截器
     */
    fun addInterceptor(interceptor: ReceiverInterceptor) {
        ReceiverInterceptorPool.getInstance().addInterceptor(interceptor)
    }


    /**
     * 切换到主线程里执行一个任务
     */
    fun post(r: Runnable) {
        handlerDisposable?.dispose()
        handlerDisposable = Observable.create<Int> {
            it.onNext(0)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                handlerDisposable?.dispose()
                r.run()
            }
    }

    /**
     * 子线程执行任务切换到主线程执行任务
     */
    fun <T> post(threadRun: ThreadRunnable<T>, mainRun: MainRunnable<T>) {
        handlerDisposable?.dispose()
        handlerDisposable = Observable.create<T> {
            threadRun.run(it)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                handlerDisposable?.dispose()
                mainRun.run(it)
            }
    }

    /**
     * 切换到主线程里执行一个延时任务
     */
    fun postDelay(delay: Long, r: Runnable) {
        delayHandlerDisposable?.dispose()
        delayHandlerDisposable = Observable.timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                delayHandlerDisposable?.dispose()
                r.run()
            }
    }

}