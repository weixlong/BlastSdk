package com.hzjq.core.loader

import android.content.Context
import android.widget.EditText
import androidx.annotation.IntRange

interface ScannerLoader {


    /**
     * 打开扫描功能
     */
    fun openScanner(context: Context)

    /**
     * @param editText 当前页面中的EditText,扫描结果将会设置在editText里
     * @param mode 只能设置 1 和 0(默认)，设置其他值时保留原有值。1，表示有声音，0，表示无声音
     */
    fun setMode(editText: EditText,@IntRange(from = 0,to = 1) mode: Int)


    /**
     * 开始扫描
     */
    fun startDecode()


    /**
     * 停止扫描
     */
    fun stopDecode()


    /**
     * 关闭扫描功能
     */
    fun closeScanner(context: Context)
}