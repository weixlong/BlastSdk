package com.hzjq.core.impl

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.device.ScanManager
import android.device.scanner.configuration.PropertyID
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.loader.ScannerLoader
import io.reactivex.functions.Consumer

class ScannerImpl : ScannerLoader {

    private val SCAN_ACTION = ScanManager.ACTION_DECODE //default action
    private var mScanManager: ScanManager? = null
    var idactionbuf =
        intArrayOf(PropertyID.WEDGE_INTENT_ACTION_NAME, PropertyID.WEDGE_INTENT_DATA_STRING_TAG)
    var idbuf = intArrayOf(
        PropertyID.CODE39_ENABLE,
        PropertyID.QRCODE_ENABLE,
        PropertyID.I25_ENABLE,
        PropertyID.EAN13_ENABLE,
        PropertyID.CODE39_LENGTH1,
        PropertyID.CODE39_LENGTH2
    )
    private var action_value_buf = arrayOf(ScanManager.ACTION_DECODE, ScanManager.BARCODE_STRING_TAG)
    private var value_buff: IntArray = IntArray(6)
    private var callback: Consumer<CapEntity>? = null

    private val mScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // TODO Auto-generated method stub
            val barcode = intent.getByteArrayExtra(ScanManager.DECODE_DATA_TAG)
            val barcodelen = intent.getIntExtra(ScanManager.BARCODE_LENGTH_TAG, 0)
            val temp = intent.getByteExtra(ScanManager.BARCODE_TYPE_TAG, 0.toByte())
            val result = intent.getStringExtra(action_value_buf[1])
            /*if(barcodelen != 0)
                barcodeStr = new String(barcode, 0, barcodelen);
            else
                barcodeStr = intent.getStringExtra("barcode_string");*/
            if (result != null) {

            }
        }
    }

    override fun openScanner(context: Context) {
        mScanManager = ScanManager()
        mScanManager?.openScanner()
        action_value_buf = mScanManager!!.getParameterString(idactionbuf)
        value_buff[0] = 1
        value_buff[1] = 1
        value_buff[2] = 1
        value_buff[3] = 1
        value_buff[4] = 6
        value_buff[5] = 200
        mScanManager!!.setParameterInts(idbuf,value_buff)
        val filter = IntentFilter()
        filter.addAction(action_value_buf[0])
        context.registerReceiver(mScanReceiver,filter)
    }

    override fun setScannerResultCallback(callback: Consumer<CapEntity>) {
        this.callback = callback
    }

    override fun closeScanner(context: Context) {
        context.unregisterReceiver(mScanReceiver)
    }
}