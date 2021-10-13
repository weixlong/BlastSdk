package com.hzjq.core.callback

import io.reactivex.ObservableEmitter

interface ThreadRunnable<T> {

    fun run(e:ObservableEmitter<T>)
}