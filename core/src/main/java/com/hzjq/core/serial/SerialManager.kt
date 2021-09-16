package com.hzjq.core.serial

import com.sdk.DeviceManager_LXR5000.Detonator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class SerialManager {

    private constructor()

    private object B {
        val sm = SerialManager()
    }

    companion object {

        /**
         * 获取串口入管理口类，单例
         */
        fun getInstance(): SerialManager {
            return B.sm
        }
    }

    private var powerOn = -1
    private var portOpenState = -1


    /**
     * 串口是否已打开
     */
    fun isOpenPort(): Boolean {
        return portOpenState == 0
    }

    /**
     * 底板电源是否已打开
     */
    fun isOpenPower():Boolean{
        return powerOn == 0
    }

    /**
     * 上电，返回值小于0 则打开是失败
     */
    fun openPower(onNext: Consumer<Int>): Disposable {
        return Observable.create<Int> {
            val powerOn = Detonator.poweron(5, 1)
            this.powerOn = powerOn
            it.onNext(powerOn)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

    /**
     * 下电,如需要低耗电，则在使用完成时调用改方法下电
     * 返回值为0时下电成功
     */
    fun closePower(onNext: Consumer<Int>): Disposable {
        return Observable.create<Int> {
            closePort()
            val powerOn = Detonator.poweron(5, 0)
            portOpenState = -1
            this.powerOn = -1
            it.onNext(powerOn)
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

    /**
     * 打开串口
     * @param portStr   串口号
     * @param ibaudRate 波特率
     *
     * @return 0：打开串口成功
     *        -1：无法打开串口：没有串口读/写权限！
     *        -2：无法打开串口：未知错误！
     *        -3：无法打开串口：参数错误！
     */
    fun openPort(): Int {
        portOpenState = BlastSerial.instance()!!.open("/dev/ttyS6", 57600)
        return portOpenState
    }

    /**
     * 关闭串口
     */
    fun closePort() {
        BlastSerial.instance()?.clearAllDataListener()
        BlastSerial.instance()!!.close()
        portOpenState = -1
    }

}