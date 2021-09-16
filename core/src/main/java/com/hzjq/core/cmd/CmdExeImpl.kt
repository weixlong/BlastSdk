package com.hzjq.core.cmd

import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.CmdExeLoader
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CmdExeImpl : CmdExeLoader {

    private var pollDisposable: Disposable? = null
    private val openPortCallback = OpenPortCallback()

    override fun <T> exeOnceCmd(cmd: ByteArray, callback: Callback<T>?) {
        cancel()
        openPortCallback.isPoll = false
        openPortCallback.cmd = cmd
        BlastDelegate.getDelegate().getOnSendMessageLoader()
            .makeSurePortOpen(openPortCallback, callback)
    }


    override fun <T> exePollResultCmd(cmd: ByteArray, callback: Callback<T>?) {
        cancel()
        openPortCallback.isPoll = true
        openPortCallback.cmd = cmd
        BlastDelegate.getDelegate().getOnSendMessageLoader()
            .makeSurePortOpen(openPortCallback, callback)
    }


    /**
     * 开始轮询结果
     */
    private fun startInterval(cmd: ByteArray){
        pollDisposable = Observable.interval(
            BlastDelegate.getDelegate().getReceiveOutTime(),
            TimeUnit.MILLISECONDS
        ).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                BlastDelegate.getDelegate().getOnSendMessageLoader().sendData(cmd)
            }
    }


    private inner class OpenPortCallback : Callback<Boolean> {
        var isPoll = false
        lateinit var cmd: ByteArray
        override fun onResult(t: Boolean) {
            if(t){
                if(!isPoll){
                    BlastDelegate.getDelegate().getOnSendMessageLoader().sendData(cmd)
                } else {
                    startInterval(cmd)
                }
            }
        }

        override fun onError(errorCode: Int) {

        }
    }

    override fun cancel() {
        pollDisposable?.dispose()
    }
}