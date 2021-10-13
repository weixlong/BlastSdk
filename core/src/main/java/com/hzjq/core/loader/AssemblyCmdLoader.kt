package com.hzjq.core.loader

import com.hzjq.core.bean.CapEntity
import com.hzjq.core.cmd.Cmd

interface AssemblyCmdLoader {

    /**
     * 获取循环查询指令
     */
    fun getCycleQueryCmd(): Cmd


    /**
     * 获取清除指令
     */
    fun getClearCmd(): Cmd


    /**
     * 获取扫描指令
     */
    fun getScanCmd(): Cmd

    /**
     * 获取读雷管数据指令
     */
    fun getReadCapCmd(num:Int): Cmd

    /**
     * 获取下传方案指令
     */
    fun getUnderCapCmd(position:Int,caps:MutableList<CapEntity>): Cmd


    /**
     * 获取写入延时方案指令
     */
    fun getWriteCapDelayCmd(isDelayWriteData:Boolean): Cmd


    /**
     * 获取授权指令
     */
    fun getAuthCmd(): Cmd

    /**
     * 获取充电指令
     */
    fun getChargeCmd(): Cmd

    /**
     * 获取起爆指令
     */
    fun getBlastCmd(): Cmd

    /**
     * 获取下电指令
     */
    fun getClosePowerCmd(): Cmd


    /**
     * 获取设置电压指令
     */
    fun getModifyVoltageCmd(): Cmd

    /**
     * 获取版本指令
     */
    fun getVersionCmd(): Cmd


    /**
     * 获取进入升级模式指令
     */
    fun getInnerUpgradeModeCmd(): Cmd


    /**
     * 获取升级扇区命令
     */
    fun getUpgradeSectorCmd(position:Int,address:String): Cmd


    /**
     * 获取升级写入扇区指令
     */
    fun getUpgradeWriteSectorCmd(): Cmd


    /**
     * 获取退出升级模式命令
     */
    fun getExitUpgradeModeCmd(targetVersion:Int): Cmd

    /**
     * 获取充电时循环查询指令
     */
    fun getChargeCycleQueryCmd(): Cmd


    /**
     * 获取授权时循环查询指令
     */
    fun getAuthCycleQueryCmd(): Cmd

    /**
     * 获取写入延时时循环查询指令
     */
    fun getWriteDelayCycleQueryCmd(): Cmd

    /**
     * 获取起爆后的循环查询指令
     */
    fun getBlastCycleQueryCmd(): Cmd

    /**
     * 单发查询指令
     */
    fun getAlongCheckCmd():Cmd


    /**
     * 清除占用
     */
    fun sginOut0ccupy():Cmd
}