package com.hzjq.blast

import android.annotation.SuppressLint
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
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() ,EasyPermissions.PermissionCallbacks{

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

    private fun reqPermissions(){
        EasyPermissions.requestPermissions(this,"打开串口，需要文件操作权限",111,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasPermission():Boolean{
        return EasyPermissions.hasPermissions(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun addLog(log: LogBean) {
        if(mLogAdapter.count > 499){
            mLogAdapter.remove(mLogAdapter.getItem(0))
        }
        mLogAdapter.add(log)
    }


    fun getVersion(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
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
        if(!hasPermission()) {
            reqPermissions()
            return
        }
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
        if(!hasPermission()) {
            reqPermissions()
            return
        }
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

    @SuppressLint("SetTextI18n")
    fun onScanCapClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
        caps.clear()
        Blast.getInstance().scanCap().onScanCap(object : OnScanCapCallback {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
                scanCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapEntity) {
                caps.add(t)
                if (t.isScanEnd) {
                    scanCapProgressView.text = "scan caps count ${caps.size}"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
                scanCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                scanCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onUnderCapClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
        CapUtils.setTestPassword(caps)
        Blast.getInstance().underCap().onUnderCap(caps, object : ProgressCallback<CapResultEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
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
                BlastLog.e("retryCount:${retryCount}  action:${action}")
                underCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                underCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onAuthCapClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
        Blast.getInstance().authCap().onAuthCap(object : ProgressCallback<CapProgressEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
                authCapProgressView.text = "progress:${progress}  action:${action}"
            }

            override fun onResult(t: CapProgressEntity) {
                if (t.isEnd) {
                    authCapProgressView.text = "auth caps success"
                }
            }

            override fun onRetryCountChanged(retryCount: Int, action: String) {
                BlastLog.e("retryCount:${retryCount}  action:${action}")
                authCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                authCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onChargeCapClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
        Blast.getInstance().charge().onCharge(object : ProgressCallback<ChargeProgressEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
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
                BlastLog.e("retryCount:${retryCount}  action:${action}")
                chargeCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                chargeCapProgressView.text = "onError:${errorCode}"
            }

        })
    }


    fun onBlastCapClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
        Blast.getInstance().blast().onBlast(object : ProgressCallback<CapResultEntity> {
            override fun onProgressChanged(progress: Int, total: Int, action: String) {
                BlastLog.e("progress:${progress}  action:${action}")
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
                BlastLog.e("retryCount:${retryCount}  action:${action}")
                blastCapProgressView.text = "retryCount:${retryCount}  action:${action}"
            }

            override fun onError(errorCode: Int) {
                blastCapProgressView.text = "onError:${errorCode}"
            }

        })

    }


    fun onQuickUnderAuthClick(view: View) {
        if(!hasPermission()) {
            reqPermissions()
            return
        }
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }
}
