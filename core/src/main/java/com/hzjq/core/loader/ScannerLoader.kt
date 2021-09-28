package com.hzjq.core.loader

import android.content.Context
import com.hzjq.core.callback.OnScannerCapCallback

interface ScannerLoader {


    /**
     * 打开扫描功能
     */
    fun openScanner(context: Context)


    fun startDecode()

    fun stopDecode()

    /**
     * 设置扫描雷管回调
     */
    fun setScannerResultCallback(callback: OnScannerCapCallback)

    /**
     * 关闭扫描功能
     */
    fun closeScanner(context: Context)
}