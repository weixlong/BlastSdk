package com.hzjq.core.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import android.widget.EditText
import com.hzjq.core.loader.ScannerLoader
import com.hzjq.core.util.BlastLog

class ScannerImpl : ScannerLoader {

    private val SCAN_ACTION = ScanManager.ACTION_DECODE //default action
    private var mScanManager: ScanManager? = null
    var idbuf =
        intArrayOf(PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG)
    private var action_value_buf =
        arrayOf(ScanManager.ACTION_DECODE, ScanManager.BARCODE_STRING_TAG)
    private var idmodebuf = intArrayOf(
        PropertyID.WEDGE_KEYBOARD_ENABLE,
        PropertyID.TRIGGERING_MODES,
        PropertyID.LABEL_APPEND_ENTER
    )

    private var idmode: IntArray? = null
    private var editText: EditText? = null
    private var mode = 0

    private val mScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
            val barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
            val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())
            val result = intent.getStringExtra(action_value_buf[1])
            if (result != null) {
                convertResult(result)
            }
        }
    }


    private fun convertResult(result: String) {
        if (editText == null) {
            BlastLog.e("please set EditText call result")
        }
        editText?.setText(result)
//        val b = Convert.isHexStr(result)
//        if (b) {
//            if (result.length >= 54) {
//                val cap = BlastDelegate.getDelegate().getParseLoader().parseCap(result)
//                callback?.onScannerCapResult(cap)
//                return
//            }
//        }
//        callback?.onScannerCapFailed(result)
    }

    override fun openScanner(context: Context) {
        mScanManager = ScanManager()
        mScanManager?.openScanner()
        action_value_buf = mScanManager!!.getParameterString(idbuf)
        idmode = mScanManager!!.getParameterInts(idmodebuf)
        idmode!![0] = this.mode
        mScanManager!!.setParameterInts(idmodebuf, idmode)
        registerScanner(context)
    }

    override fun setMode(editText: EditText, mode: Int) {
        if (mScanManager == null) {
            BlastLog.e("please open scanner before")
            return
        }
        this.editText = editText
        if (mode == 0 || mode == 1) {
            this.mode = mode
        }
        idmode!![0] = this.mode
        mScanManager!!.setParameterInts(idmodebuf, idmode)
    }

    override fun startDecode() {
        if (mScanManager == null) {
            BlastLog.e("please open scanner before")
            return
        }
        if (mode == 1) {
            editText?.requestFocus()
            editText?.setText("")
        }
        mScanManager?.startDecode()
    }

    override fun stopDecode() {
        if (mScanManager == null) {
            BlastLog.e("please open scanner before")
            return
        }
        editText?.clearFocus()
        mScanManager?.stopDecode()
    }


    private fun registerScanner(context: Context) {
        if (mScanManager == null) {
            BlastLog.e("please open scanner before")
            return
        }
        if (mScanManager != null) {
            val filter = IntentFilter()
            action_value_buf = mScanManager!!.getParameterString(idbuf)
            filter.addAction(action_value_buf[0])
            context.registerReceiver(mScanReceiver, filter)
        }
    }

    override fun closeScanner(context: Context) {
        context.unregisterReceiver(mScanReceiver)
        mScanManager = null
        editText?.clearFocus()
    }
}