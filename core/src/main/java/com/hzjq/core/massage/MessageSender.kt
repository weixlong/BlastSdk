package com.hzjq.core.massage

import android.text.TextUtils
import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.LogBean
import com.hzjq.core.callback.Callback
import com.hzjq.core.loader.OnSendMessageLoader
import com.hzjq.core.receive.Receives
import com.hzjq.core.serial.BlastSerial
import com.hzjq.core.serial.SerialManager
import com.vi.vioserial.listener.OnSerialDataListener
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus

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
        if (openPower) {
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
                    if (!TextUtils.isEmpty(hexData)) {
                        val log = LogBean(hexData!!,1)
                        EventBus.getDefault().post(log)
                    }
                }
            }

            override fun onReceive(hexData: String?) {
                // val data = SerialDataUtils.hexStringToString(hexData)
                // Loog.e("onReceive:$hexData")
            }

            override fun onReceiveFullData(hexData: String?) {
                if (!hexData.isNullOrEmpty()) {
                    val log = LogBean(hexData,2)
                    EventBus.getDefault().post(log)
                    Receives.getInstance().findReceiverDoWork(hexData)
                }
            }

        })
    }
}