package com.hzjq.core.impl

import com.hzjq.core.callback.OnVersionUpgradeCallback
import com.hzjq.core.loader.OnVersionUpgradeLoader
import com.hzjq.core.work.Works
import com.hzjq.core.worker.*
import java.io.File

class VersionUpgradeImpl : OnVersionUpgradeLoader {

    override fun onVersionUpgrade(targetVersion:Int,binFile: File, callback: OnVersionUpgradeCallback) {
        Works.Builder.newBuilder()
            .addWork(UpgradeInnerModeWork(callback))
            .addWork(ReadUpgradeFileWork(targetVersion,binFile,callback))
            .addWork(UpgradeSendAddressWork(targetVersion,callback))
            .addWork(UpgradeWriteSectorWork(callback))
            .addWork(UpgradeWriteSectorEndWork(callback))
            .build()
            .queue()
    }
}