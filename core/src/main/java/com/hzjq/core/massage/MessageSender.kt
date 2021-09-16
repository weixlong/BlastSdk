package com.hzjq.core.massage

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnSendMessageLoader
import com.hzjq.core.receive.Receives
import com.hzjq.core.serial.BlastSerial
import com.hzjq.core.serial.SerialManager
import com.hzjq.core.util.BlastLog
import com.vi.vioserial.listener.OnSerialDataListener
import com.vi.vioserial.util.SerialDataUtils
import io.reactivex.functions.Consumer

class MessageSender : OnSendMessageLoader {

    override fun <T> makeSurePortOpen(openCallback: Callback<Boolean>, callback: Callback<T>?) {
        val openPower = SerialManager.getInstance().isOpenPower()
        if (!openPower) {
            SerialManager.getInstance().openPower(Consumer {
                if (it == 0) {
                    checkPortState(openCallback, callback)
                } else {
                    SerialManager.getInstance().closePort()
                    openCallback.onResult(false)
                    BlastDelegate.getDelegate().post(Runnable {
                        callback?.onError(it)
                    })
                }
            })
        } else {
            checkPortState(openCallback, callback)
        }
    }

    override fun sendData(msg: ByteArray) {
        val openPower = SerialManager.getInstance().isOpenPower()
        if (!openPower) {
            val openPort = SerialManager.getInstance().isOpenPort()
            if (openPort) {
                BlastSerial.instance()?.sendData(msg)
            }
        }
    }

    /**
     * 检查串口状态
     */
    private fun <T> checkPortState(openCallback: Callback<Boolean>, callback: Callback<T>?) {
        val openPort = SerialManager.getInstance().isOpenPort()
        if (!openPort) {
            val i = SerialManager.getInstance().openPort()
            if (i == 0) {
                observeMessage()
                openCallback.onResult(true)
            } else {
                SerialManager.getInstance().closePort()
                openCallback.onResult(false)
                BlastDelegate.getDelegate().post(Runnable {
                    callback?.onError(i - 1)
                })
            }
        } else {
            openCallback.onResult(true)
        }
    }

    /**
     * 监听串口通讯数据
     */
    private fun observeMessage() {
        BlastSerial.instance()?.clearAllDataListener()
        BlastSerial.instance()?.setSerialDataListener(object : OnSerialDataListener {
            override fun onSend(hexData: String?) {
                if (BlastDelegate.getDelegate().isDebug()) {
                    val data = SerialDataUtils.hexStringToString(hexData)
                    if (!TextUtils.isEmpty(hexData)) {
                        BlastLog.d("receiveData:$data")
                    }
                }
            }

            override fun onReceive(hexData: String?) {
                // val data = SerialDataUtils.hexStringToString(hexData)
                // Loog.e("onReceive:$hexData")
            }

            override fun onReceiveFullData(hexData: String?) {
                if (!hexData.isNullOrEmpty()) {
                    if (BlastDelegate.getDelegate().isDebug()) {
                        BlastLog.d("receiveData:$hexData")
                        Receives.getInstance().findReceiverDoWork(hexData)
                    }
                }
            }

        })
    }
}