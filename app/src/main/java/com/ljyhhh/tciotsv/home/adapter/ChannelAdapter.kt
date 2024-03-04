package com.ljyhhh.tciotsv.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.ljyhhh.tciotsv.R
import com.ljyhhh.tciotsv.home.Channel

class ChannelAdapter(list: ArrayList<Channel>, context: Context?) : BaseAdapter() {
    private val channelList: ArrayList<Channel>
    private val layoutInflater: LayoutInflater

    init {
        channelList = list
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int = channelList.size

    override fun getItem(position: Int): Any = channelList[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        var holder: ViewHolder?
        if (convertView == null) {
            //加载布局
            convertView = layoutInflater.inflate(R.layout.grid_item, null)
            holder = ViewHolder()
            holder.imgChannel = convertView.findViewById(R.id.channel_img) as ImageView
            holder.decChannel = convertView.findViewById(R.id.channel_dec) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder?
        }


        holder!!.decChannel?.text = channelList[position].dec
        holder.imgChannel?.setImageResource(channelList[position].img)

        return convertView
    }

    internal inner class ViewHolder {
        var imgChannel: ImageView? = null
        var decChannel: TextView? = null
    }
}
