package com.hzjq.blast

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.hzjq.core.bean.CapEntity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class CapUtils {

    companion object {

        /**
         * 设置测试密码
         */
        fun setTestPassword(caps: MutableList<CapEntity>) {
            caps.forEach {
                it.password = "00000000"
            }
        }

        /**
         * 读资源里的升级文件
         */
        @SuppressLint("CheckResult")
        fun readAssetsBinFile(context: Context, c: Consumer<File>) {
            Observable.create<File> {
                val qbqAssets = findQBQAssets(context)
                if (!TextUtils.isEmpty(qbqAssets)) {
                    val path = context.getCacheDir().getPath() + "/upgrade.bin"
                    val file = File(path)
                    if (file.exists()) {
                        file.delete()
                        file.createNewFile()
                    }
                    val stream = javaClass.classLoader.getResourceAsStream("assets/${qbqAssets}")
                    val fos = FileOutputStream(file)
                    var len = 0
                    val buffer = ByteArray(1024)
                    while (stream.read(buffer).let { len = it; it } != -1) {
                        fos.write(buffer, 0, len)
                        fos.flush()
                    }
                    stream.close()
                    fos.close()
                    it.onNext(file)
                }
            }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(c)
        }

        private fun findQBQAssets(context: Context): String {
            val fileNames = context.assets.list("")
            if (fileNames != null && fileNames.isNotEmpty()) {
                fileNames.forEach {
                    if (it.endsWith(".bin") && it.contains("QBQ")) {
                        return it
                    }
                }
            }
            return ""
        }
    }
}