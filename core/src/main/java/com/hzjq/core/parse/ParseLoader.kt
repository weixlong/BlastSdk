package com.hzjq.core.parse

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.bean.CapProgressEntity
import io.reactivex.functions.Consumer
import java.io.File

interface ParseLoader {

    /**
     * 解析返回数据，获取版本号
     */
    fun parseVersionMsg(msg:String):Int

    /**
     * 解析进入或者退出升级模式是否成功
     */
    fun parseUpgradeModeMsg(msg: String):Boolean

    /**
     * 解析升级文件
     */
    fun parseUpgradeFileData(binFile: File,mSectorDataList:ArrayList<String>,mSectorAddrList:ArrayList<String>,onNext: Consumer<Boolean>)


    /**
     * 解析扫描进度
     */
    fun parseScanProgress(msg: String): CapProgressEntity


    /**
     * 解析雷管信息
     */
    fun parseCap(msg: String):CapEntity

}