package com.hzjq.blast

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.hzjq.core.bean.LogBean
import kotlinx.android.synthetic.main.item_log_reciver_layout.view.*
import kotlinx.android.synthetic.main.item_log_send_layout.view.*

class LogAdapter : ArrayAdapter<LogBean> {

    constructor(context: Context) : super(context, R.layout.item_log_reciver_layout)

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.type
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewType = getItemViewType(position)
        if (viewType == 1) {
            val view = LayoutInflater.from(context).inflate(
                R.layout.item_log_send_layout, parent, false
            )
            view.logSendContent.text = getItem(position)!!.content
            view.logSendTime.text = getItem(position)!!.time
            view.setId(R.id.logSendLayout)
            return view

        } else {
            val view = LayoutInflater.from(context).inflate(
                R.layout.item_log_reciver_layout, parent, false
            )
            view.logReceiverTime.text = getItem(position)!!.time
            view.logReceiverContent.text = getItem(position)!!.content
            view.setId(R.id.logReceiverLayout)
            return view

        }

    }
}