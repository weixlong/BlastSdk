package com.hzjq.core.serial

import android.os.Message
import android.text.TextUtils
import com.vi.vioserial.BaseSerial
import com.vi.vioserial.listener.OnNormalDataListener
import com.vi.vioserial.listener.OnSerialDataListener
import com.vi.vioserial.util.Logger
import java.util.*

class BlastSerial  {
    private val TAG = "BlastSerial"

    private var mBaseSerial: BaseSerial? = null

    private var mListener: MutableList<OnNormalDataListener>? = null

    companion object {
        @Volatile
        private var instance: BlastSerial? = null

        fun instance(): BlastSerial? {
            if (instance == null) {
                synchronized(BlastSerial::class.java) {
                    if (instance == null) {
                        instance = BlastSerial()
                    }
                }
            }
            return instance
        }
    }

    private constructor()



    @Synchronized
    fun open(portStr: String?, ibaudRate: Int): Int {
        return open(portStr, ibaudRate, 1, 8, 0, 0)
    }

    @Synchronized
    fun open(
        portStr: String?,
        ibaudRate: Int,
        mStopBits: Int,
        mDataBits: Int,
        mParity: Int,
        mFlowCon: Int
    ): Int {
        require(!(TextUtils.isEmpty(portStr) || ibaudRate == 0)) { "Serial port and baud rate cannot be empty" }

        mBaseSerial = object : BaseSerial(portStr, ibaudRate) {
            override fun onDataBack(data: String) {
                //温度
                if (mListener != null) {
                    for (i in mListener!!.indices.reversed()) {
                        mListener!![i].normalDataBack(data)
                    }
                }
            }

            override fun sendHex(sHex: ByteArray) {
               // val sHex1 = sHex!!.trim { it <= ' ' }.replace(" ".toRegex(), "")
               // val bOutArray = ByteUtil.hex2byte(sHex)
                val msg = Message.obtain()
                msg.obj = sHex
                addWaitMessage(msg)
            }
        }
        mBaseSerial!!.setStopBits(mStopBits)
        mBaseSerial!!.setDataBits(mDataBits)
        mBaseSerial!!.setParity(mParity)
        mBaseSerial!!.setFlowCon(mFlowCon)
        val openStatus: Int = mBaseSerial!!.openSerial()
        if (openStatus != 0) {
            close()
        }
        return openStatus
    }

    /**
     * 添加串口返回数据回调
     * Add callback
     */
    fun addDataListener(dataListener: OnNormalDataListener?) {
        if (mListener == null) {
            mListener = ArrayList()
        }
        mListener!!.add(dataListener!!)
    }

    /**
     * 移除串口返回数据回调
     * Remove callback
     */
    fun removeDataListener(dataListener: OnNormalDataListener?) {
        if (mListener != null) {
            mListener!!.remove(dataListener)
        }
    }

    /**
     * 移除全部回调
     * Remove all
     */
    fun clearAllDataListener() {
        if (mListener != null) {
            mListener!!.clear()
        }
    }

    /**
     * 监听串口数据
     * Listening to serial data
     * 该方法必须在串口打开成功后调用
     * This method must be called after the serial port is successfully opened.
     */
    fun setSerialDataListener(dataListener: OnSerialDataListener?) {
        if (mBaseSerial != null) {
            mBaseSerial!!.setSerialDataListener(dataListener)
        } else {
            Logger.getInstace()
                .e(TAG, "The serial port is closed or not initialized")
            //throw new IllegalArgumentException("The serial port is closed or not initialized");
        }
    }

    /**
     * 串口是否打开
     * Serial port status (open/close)
     *
     * @return true/false
     */
    fun isOpen(): Boolean {
        return if (mBaseSerial != null) {
            mBaseSerial!!.isOpen()
        } else {
            Logger.getInstace()
                .e(TAG, "The serial port is closed or not initialized")
            //throw new IllegalArgumentException("The serial port is closed or not initialized");
            false
        }
    }

    /**
     * Close the serial port
     */
    fun close() {
        if (mBaseSerial != null) {
            mBaseSerial!!.close()
            mBaseSerial = null
        } else {
            Logger.getInstace()
                .e(TAG, "The serial port is closed or not initialized")
            //throw new IllegalArgumentException("The serial port is closed or not initialized");
        }
    }

    /**
     * send data
     *
     * @param hexData
     */
    fun sendData(hexData: ByteArray) {
        if (isOpen()) {
            mBaseSerial?.sendHex(hexData)
        }
    }
}