package com.hzjq.blast

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

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

        Blast.getInstance().getScannerLoader()
            .openScanner(this)

        Blast.getInstance().getScannerLoader()
            .setMode(mainScanResult,1)

        onScannerCap()
    }

    private fun reqPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "打开串口，需要文件操作权限",
            111,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) !== PackageManager.PERMISSION_GRANTED
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun addLog(log: LogBean) {
        if (mLogAdapter.count > 499) {
            mLogAdapter.remove(mLogAdapter.getItem(0))
        }
        mLogAdapter.add(log)
    }


    private fun onScannerCap() {
        onScannerCap.setOnTouchListener { v, event ->
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    Blast.getInstance().getScannerLoader().startDecode()
                }
                MotionEvent.ACTION_UP -> {
                    Blast.getInstance().getScannerLoader().stopDecode()
                }
            }
            true
        }

        mainScanResult.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                BlastLog.e(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

    }


    fun onCheckAlongCap(view: View) {
        if (!hasPermission()) {
            reqPermissions()
            return
        }
        Blast.getInstance().getAlongCapCheckLoader()
            .start(object : Callback<AlongCapResultEntity>{

                override fun onResult(t: AlongCapResultEntity) {
                    alongCapView.text = "onResult (mVoltage:${t.mVoltage} mElectric:${t.mElectric} uid:${t.uid})"
                }

                override fun onRetryCountChanged(retryCount: Int, action: String) {
                    alongCapView.text = "onRetryCountChanged retryCount:${retryCount}  action:${action}"
                }

                override fun onError(errorCode: ErrorResult) {
                    alongCapView.text = "onError errorCode:${errorCode.errorAction}"
                }

            })

    }

    fun getVersion(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                versionView.text = "onError:${errorCode.errorAction}"
            }

        })
    }

    fun upgradeVersion(view: View) {
        if (!hasPermission()) {
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

                override fun onError(errorCode: ErrorResult) {
                    upgradeVersionView.text = "onError:${errorCode.errorAction}"
                }

            })
        })

    }

    fun exitUpgradeMode(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                exitUpgradeProgressView.text = "onError:${errorCode.errorAction}"
            }

        })
    }

    @SuppressLint("SetTextI18n")
    fun onScanCapClick(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                scanCapProgressView.text = "onError:${errorCode.errorAction}"
            }

        })
    }


    fun onUnderCapClick(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                underCapProgressView.text = "onError:${errorCode.errorAction}"
            }

        })
    }


    fun onAuthCapClick(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                authCapProgressView.text = "onError:${errorCode.errorAction}"
            }

        })
    }


    fun onChargeCapClick(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                chargeCapProgressView.text = "onError:${errorCode.errorAction}"
            }

        })
    }


    fun onBlastCapClick(view: View) {
        if (!hasPermission()) {
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

            override fun onError(errorCode: ErrorResult) {
                blastCapProgressView.text = "onError:${errorCode.errorAction}"
            }

        })

    }


    fun onQuickUnderAuthClick(view: View) {
        if (!hasPermission()) {
            reqPermissions()
            return
        }
        CapUtils.setTestPassword(caps)
        Blast.getInstance().quickRegister()
            .onQuickRegister(caps, object : ProgressCallback<CapProgressEntity> {
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

                override fun onError(errorCode: ErrorResult) {
                    quickCapProgressView.text = "onError:${errorCode.errorAction}"
                }
            }, Consumer {
                quickCapProgressView.text = "onError:${it.errorCode}"
            })

    }


    override fun onDestroy() {
        super.onDestroy()
        Blast.getInstance().getScannerLoader().closeScanner(this)
        EventBus.getDefault().unregister(this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }




}
