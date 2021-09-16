package com.hzjq.core.work

import android.text.TextUtils

class Works private constructor(works: HashMap<String, Work<Any>>) {

    private val works = HashMap<String, Work<Any>>()
    private var position = 0

    init {
        this.works.clear()
        this.works.putAll(works)
    }


    /**
     * 执行任务
     */
    fun queue(): Works {
        works.forEach {
            it.value.setWorks(this)
        }
        works.values
        if (works.size > position) {
            works.values.toMutableList()[position].doWork()
        }
        return this
    }


    /**
     * 执行下一个任务
     */
    internal fun doNextWork(vararg args: Any) {
        position++
        if (works.size > position) {
            works.values.toMutableList()[position].doWork(*args)
        }
    }

    /**
     * 执行指定目标的任务
     */
    internal fun <work : Work<Any>> doTargetWork(clazz: Class<work>, vararg args: Any) {
        val mutableList = works.values.toMutableList()
        mutableList.forEachIndexed { index, work ->
            if (TextUtils.equals(clazz.name, work::class.java.name)) {
                position = index
                if (works.size > position) {
                    mutableList[position].doWork(*args)
                }
                return@forEachIndexed
            }
        }
    }

    /**
     * 重新执行当前任务
     */
    fun retryCurWork(vararg args: Any) {
        val mutableList = works.values.toMutableList()
        if (works.size > position) {
            mutableList[position].doWork(*args)
        }
    }

    /**
     * 销毁
     */
    fun onDestroy() {
        works.forEach {
            it.value.cancel()
        }
        works.clear()
        position = 0
    }


    class Builder {

        private val works = HashMap<String, Work<Any>>()

        private constructor() {
            works.clear()
        }

        companion object {
            fun newBuilder(): Builder {
                return Builder()
            }
        }

        /**
         * 添加一个待执行任务
         */
        fun <T : Any> addWork(work: Work<T>): Builder {
            works[work::class.java.name] = work as Work<Any>
            return this
        }

        /**
         * 结束添加任务
         */
        fun build(): Works {
            return Works(works)
        }

    }


}