package com.hzjq.blast

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hzjq.core.Blast
import com.hzjq.core.bean.*
import com.hzjq.core.callback.*
import com.hzjq.core.util.BlastLog
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var mLogAdapter:LogAdapter
    private val caps = arrayListOf<CapEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Blast.getInstance().setDebug(true)
        mLogAdapter = LogAdapter(this)
        logView.adapter = mLogAdapter
        logView.dividerHeight = 0
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
     fun addLog(log: LogBean){
        mLogAdapter.add(log)
    }

    fun getVersion(view: View) {
        Blast.getInstance().getVersion(object : OnVersionCallback{
            override fun onResult(t: Int) {
                versionView.text = "版本号:${t}"
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })
    }

    fun upgradeVersion(view: View) {
        Blast.getInstance().upgrade(35, File(""),object : OnVersionUpgradeCallback{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {

            }

            override fun onResult(t: Int) {
                upgradeVersionView.text = "升级后的版本号:${t}"
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {

            }

            override fun onError(errorCode: Int) {

            }

        })
    }

    fun exitUpgradeMode(view: View) {
        Blast.getInstance().exitUpgradeMode(35,object : Callback<Boolean>{
            override fun onResult(t: Boolean) {

            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {

            }

            override fun onError(errorCode: Int) {

            }

        })
    }



    fun onScanCapClick(view: View) {
        Blast.getInstance().scanCap().onScanCap(object : OnScanCapCallback{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
            }

            override fun onResult(t: CapEntity) {
                BlastLog.e("cap:${t}")
                caps.add(t)
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })
    }



    fun onUnderCapClick(view: View) {
        CapUtils.setTestPassword(caps)
        Blast.getInstance().underCap().onUnderCap(caps,object : ProgressCallback<CapResultEntity>{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
            }

            override fun onResult(t: CapResultEntity) {
                BlastLog.e("UnderCap:${t.errorCode} ")
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })
    }


    fun onAuthCapClick(view: View) {
        Blast.getInstance().authCap().onAuthCap(object : ProgressCallback<CapProgressEntity>{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
            }

            override fun onResult(t: CapProgressEntity) {
                BlastLog.e("UnderCap:${t.errorCode} ")
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })
    }


    fun onChargeCapClick(view: View) {
        Blast.getInstance().charge().onCharge(object : ProgressCallback<ChargeProgressEntity>{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
            }

            override fun onResult(t: ChargeProgressEntity) {
                BlastLog.e("UnderCap:${t.errorCode} ")
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })
    }


    fun onBlastCapClick(view: View) {
        Blast.getInstance().blast().onBlast(object : ProgressCallback<CapResultEntity>{
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
            }

            override fun onResult(t: CapResultEntity) {
                BlastLog.e("UnderCap:${t.errorCode} ")
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
            }

            override fun onError(errorCode: Int) {
                BlastLog.e("onError:${errorCode}")
            }

        })

    }



    fun onQuickUnderAuthClick(view: View) {
        Blast.getInstance().quickUnderAuth()
            .onQuickUnderAuth(arrayListOf(),object : ProgressCallback<CapProgressEntity>{
                override fun onProgressChanged(progress: Int, total: Int, action: String) {
                    BlastLog.e("progress:${progress}  action:${action}")
                }

                override fun onResult(t: CapProgressEntity) {
                    BlastLog.e("UnderCap:${t.errorCode} ")
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    BlastLog.e("retryCount:${retryCount}  action:${action}")
                }

                override fun onError(errorCode: Int) {
                    BlastLog.e("onError:${errorCode}")
                }
            }, Consumer{
                BlastLog.e("onError:${it.errorCode}")
            })

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
