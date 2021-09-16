package com.hzjq.core.callback

import com.hzjq.core.bean.CapEntity


/**
 * 扫描雷管回调
 */
interface OnScanCapCallback : ProgressCallback<CapEntity> {

    /**
     * 每扫描到一发雷管，都会回调onResult方法
     */

}