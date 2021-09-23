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

class MainActivity : AppCompatActivity() {

    private lateinit var mLogAdapter: LogAdapter
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
    fun addLog(log: LogBean) {
        mLogAdapter.add(log)
    }

    fun getVersion(view: View) {
        Blast.getInstance().getVersion(object : OnVersionCallback {
            override fun onResult(t: Int) {
                versionView.text = "版本号:${t}"
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                versionView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                versionView.text = "onError:${errorCode}"
            }

        })
    }

    fun upgradeVersion(view: View) {
        CapUtils.readAssetsBinFile(this, Consumer {
            Blast.getInstance().upgrade(35, it, object : OnVersionUpgradeCallback {
                override fun onProgressChanged(progress: Int, total: Int, action: String) {
                    BlastLog.e("progress:${progress}  action:${action}")
                    upgradeVersionView.text = "progress:${progress}  action:${action}"
                }

                override fun onResult(t: Int) {
                    upgradeVersionView.text = "升级操作后的版本号:${t}"
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    upgradeVersionView.text = "retryCount:${retryCount}  action:${action}"
                }

                override fun onError(errorCode: Int) {
                    upgradeVersionView.text = "onError:${errorCode}"
                }

            })
        })

    }

    fun exitUpgradeMode(view: View) {
        Blast.getInstance().exitUpgradeMode(35, object : Callback<Boolean> {
            override fun onResult(t: Boolean) {
                exitUpgradeProgressView.text = if (t) "退出成功" else "退出失败"
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                exitUpgradeProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                exitUpgradeProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onScanCapClick(view: View) {
        Blast.getInstance().scanCap().onScanCap(object : OnScanCapCallback {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                scanCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapEntity) {
                caps.add(t)
                if (t.isScanEnd) {
                    scanCapProgressView.text = "scan caps count ${caps.size}"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                scanCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                scanCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onUnderCapClick(view: View) {
        CapUtils.setTestPassword(caps)
        Blast.getInstance().underCap().onUnderCap(caps, object : ProgressCallback<CapResultEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                underCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapResultEntity) {
                if (t.errorCode == 0) {
                    underCapProgressView.text = "under caps success"
                } else {
                    underCapProgressView.text =
                        "under caps missCaps count ${t.missCaps?.size} under caps meetCaps count ${t.meetCaps?.size}"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                underCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                underCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onAuthCapClick(view: View) {
        Blast.getInstance().authCap().onAuthCap(object : ProgressCallback<CapProgressEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                authCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapProgressEntity) {
                if (t.isEnd) {
                    authCapProgressView.text = "auth caps success"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                authCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                authCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onChargeCapClick(view: View) {
        Blast.getInstance().charge().onCharge(object : ProgressCallback<ChargeProgressEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                chargeCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: ChargeProgressEntity) {
                if (t.isEnd) {
                    if (t.code == 0) {
                        chargeCapProgressView.text = "charge caps count ${t.caps.size} success"
                    } else {
                        chargeCapProgressView.text =
                            "charge caps failed . charge error count ${t.chargeErrCaps.size} , not match count ${t.notMatchCaps.size} , not auth count ${t.notAuthCaps.size}"
                    }
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                chargeCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                chargeCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onBlastCapClick(view: View) {
        Blast.getInstance().blast().onBlast(object : ProgressCallback<CapResultEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                blastCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapResultEntity) {
                if (t.errorCode == 0) {
                    blastCapProgressView.text = "blast cap count ${t.caps?.size} success"
                } else {
                    blastCapProgressView.text = "blast cap count ${t.resultCaps?.size} failed"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                blastCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                blastCapProgressView.text = "onError:${errorCode}"
            }

        })

    }


    fun onQuickUnderAuthClick(view: View) {
        CapUtils.setTestPassword(caps)
        Blast.getInstance().quickUnderAuth()
            .onQuickUnderAuth(caps, object : ProgressCallback<CapProgressEntity> {
                override fun onProgressChanged(progress: Int, total: Int, action: String) {
                    BlastLog.e("progress:${progress}  action:${action}")
                    quickCapProgressView.text = "progress:${progress}  action:${action}"
                }

                override fun onResult(t: CapProgressEntity) {
                    if (t.isEnd) {
                        quickCapProgressView.text = "under auth caps success"
                    }
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    quickCapProgressView.text = "retryCount:${retryCount}  action:${action}"
                }

                override fun onError(errorCode: Int) {
                    quickCapProgressView.text = "onError:${errorCode}"
                }
            }, Consumer {
                quickCapProgressView.text = "onError:${it.errorCode}"
            })

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
