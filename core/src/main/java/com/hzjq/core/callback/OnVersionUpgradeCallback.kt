package com.hzjq.core.callback


/**
 * 版本升级回调
 */
interface OnVersionUpgradeCallback : ProgressCallback<Int> {

    /**
     * 进度完成，回调onResult方法，返回升级后的版本号
     */

}