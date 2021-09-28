package com.hzjq.core.loader

import android.content.Context
import com.hzjq.core.bean.CapEntity
import io.reactivex.functions.Consumer

interface ScannerLoader {


    /**
     * 打开扫描功能
     */
    fun openScanner(context: Context)

    /**
     * 设置扫描雷管回调
     */
    fun setScannerResultCallback(callback:Consumer<CapEntity>)

    /**
     * 关闭扫描功能
     */
    fun closeScanner(context: Context)
}