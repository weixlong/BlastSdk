package com.hzjq.core.cmd

import com.hzjq.core.BlastDelegate
import com.hzjq.core.bean.CapEntity
import com.hzjq.core.loader.AssemblyCmdLoader
import com.hzjq.core.util.CapUtil
import com.hzjq.core.util.Convert

class AssemblyCmd : AssemblyCmdLoader {

    override fun getCycleQueryCmd(): Cmd {
        return Cmd(
            CmdCode.KZB_QUERY_LG_INFO_OK_ASK,
            "",
            CmdCode.KZB_QUERY_LG_INFO_CMD
        )
    }

    override fun getClearCmd(): Cmd {
        return Cmd(
            CmdCode.KZB_CLEAR_STATE_OK_ASK,
            CmdCode.KZB_CLEAR_STATE_E_ASK,
            CmdCode.KZB_CLEAR_STATE_CMD
        )
    }

    override fun getScanCmd(): Cmd {
        return Cmd(
            CmdCode.KZB_SCAN_LG_OK_ASK,
            CmdCode.KZB_SCAN_LG_E_ASK,
            CmdCode.KZB_SCAN_LG_CMD
        )
    }

    override fun getReadCapCmd(num: Int): Cmd {
        return Cmd(
            CmdCode.KZB_READ_LG_INFO_ASK,
            "",
            CmdCode.KZB_READ_LG_INFO_CMD
        )
    }

    override fun getUnderCapCmd(position: Int, caps: MutableList<CapEntity>): Cmd {
        val data = CapUtil.getMakeData(position, caps)
        return Cmd(
            CmdCode.DELAY_PLAN_OK_ASK,
            CmdCode.DELAY_PLAN_E_ASK,
            CmdCode.DELAY_PLAN_CMD + data
        )
    }

    override fun getWriteCapDelayCmd(isDelayWriteData: Boolean): Cmd {
        return Cmd(
            CmdCode.DELAY_INPUT_OK_ASK,
            CmdCode.DELAY_INPUT_E_ASK,
            CmdCode.DELAY_INPUT_CMD + if (isDelayWriteData) "01" else "00"
        )
    }

    override fun getAuthCmd(): Cmd {
        return Cmd(
            CmdCode.AUTH_OK_ASK,
            CmdCode.AUTH_E_ASK,
            CmdCode.AUTH_CMD
        )
    }

    override fun getChargeCmd(): Cmd {
        return Cmd(
            CmdCode.BLAST_CHARGE_OK_ASK,
            CmdCode.BLAST_CHARGE_E_ASK,
            CmdCode.BLAST_CHARGE_CMD
        )
    }

    override fun getBlastCmd(): Cmd {
        return Cmd(
            CmdCode.BLAST_OK_ASK,
            CmdCode.BLAST_E_ASK,
            CmdCode.BLAST_CMD + "00"
        )
    }

    override fun getClosePowerCmd(): Cmd {
        return Cmd(
            CmdCode.CLOSE_ELECTRIC_OK_ASK,
            CmdCode.CLOSE_ELECTRIC_E_ASK,
            CmdCode.CLOSE_ELECTRIC_CMD
        )
    }

    override fun getModifyVoltageCmd(): Cmd {
        return Cmd(
            CmdCode.SETTING_VOLTAGE_OK_ASK,
            CmdCode.SETTING_VOLTAGE_E_ASK,
            CmdCode.SETTING_VOLTAGE_CMD
        )
    }

    override fun getVersionCmd(): Cmd {
        val cmd = Cmd(
            "",
            "",
            CmdCode.GET_CONTROL_VERSION
        )
        cmd.key = Cmd.Key("23", "BB", 10, 12)
        return cmd
    }

    override fun getInnerUpgradeModeCmd(): Cmd {
        return Cmd(
            CmdCode.INTO_UPGRADE_ASK,
            CmdCode.INTO_UPGRADE_E_ASK,
            CmdCode.INTO_UPGRADE_LEFT + BlastDelegate.getDelegate()
                .getCmdType() + CmdCode.INTO_UPGRADE_RIGHT
        )
    }

    override fun getUpgradeSectorCmd(position: Int, address: String): Cmd {
        val number: String = Convert.getCurrentDetonatorNum(position)
        return Cmd(
            CmdCode.SEND_UPGRADE_ADDRESS_ASK,
            "",
            CmdCode.SEND_UPGRADE_ADDRESS_LEFT
                    + BlastDelegate.getDelegate()
                .getCmdType() + CmdCode.SEND_UPGRADE_ADDRESS_MIDDLE + number +
                    CmdCode.SEND_UPGRADE_ADDRESS_RIGHT + address
        )
    }

    override fun getUpgradeWriteSectorCmd(): Cmd {
        return Cmd(
            CmdCode.SEND_INPUT_ASK,
            "",
            CmdCode.SEND_INPUT_LEFT + BlastDelegate.getDelegate()
                .getCmdType() + CmdCode.SEND_INPUT_RIGHT
        )
    }

    override fun getExitUpgradeModeCmd(): Cmd {
        return Cmd(
            CmdCode.OUT_UPGRADE_ASK,
            CmdCode.OUT_UPGRADE_E_ASK,
            CmdCode.OUT_UPGRADE_LEFT + BlastDelegate.getDelegate()
                .getCmdType() + CmdCode.OUT_UPGRADE_RIGHT
        )
    }

    override fun getChargeCycleQueryCmd(): Cmd {
        val cmd = Cmd(
            "",
            "",
            CmdCode.KZB_QUERY_LG_INFO_CMD
        )
        cmd.key = Cmd.Key(
            CmdCode.KZB_QUERY_LG_INFO_OK_ASK,
            "08",
            20,
            22
        )
        return cmd
    }

    override fun getAuthCycleQueryCmd(): Cmd {
        val cmd = Cmd(
            "",
            "",
            CmdCode.KZB_QUERY_LG_INFO_CMD
        )
        cmd.key = Cmd.Key(
            CmdCode.KZB_QUERY_LG_INFO_OK_ASK,
            "06",
            20,
            22
        )
        return cmd
    }

    override fun getWriteDelayCycleQueryCmd(): Cmd {
        val cmd = Cmd(
            "",
            "",
            CmdCode.KZB_QUERY_LG_INFO_CMD
        )
        cmd.key = Cmd.Key(
            CmdCode.KZB_QUERY_LG_INFO_OK_ASK,
            "07",
            20,
            22
        )
        return cmd
    }

    override fun getBlastCycleQueryCmd(): Cmd {
        val cmd = Cmd(
            "",
            "",
            CmdCode.KZB_QUERY_LG_INFO_CMD
        )
        cmd.key = Cmd.Key(
            CmdCode.KZB_QUERY_LG_INFO_OK_ASK,
            "09",
            20,
            22
        )
        return cmd
    }
}