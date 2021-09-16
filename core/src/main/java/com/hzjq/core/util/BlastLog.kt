package com.hzjq.core.util

import android.util.Log

class BlastLog {

    companion object {

        private const val TAG = "Blast"

        fun e(msg: String) {
            Log.e(TAG, msg)
        }


        fun d(msg: String) {
            Log.d(TAG, msg)
        }


        fun w(msg: String) {
            Log.w(TAG, msg)
        }
    }
}