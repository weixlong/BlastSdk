package com.hzjq.core.callback

import com.hzjq.core.bean.CapEntity

interface OnScannerCapCallback {


    /**
     * 扫描到雷管信息
     */
    fun onScannerCapResult(cap:CapEntity)

    /**
     * 扫描失败，返回扫描到的数据
     */
    fun onScannerCapFailed(result:String)
}