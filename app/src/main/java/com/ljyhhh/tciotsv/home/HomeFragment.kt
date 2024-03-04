package com.ljyhhh.tciotsv.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ljyhhh.tciotsv.R
import com.ljyhhh.tciotsv.home.adapter.ChannelAdapter
import kotlinx.android.synthetic.main.activity_home_fragment.view.*
import org.eclipse.paho.android.service.MqttAndroidClient
import com.ljyhhh.tciotsv.home.mqtt.MQTTHelper


class HomeFragment : Fragment() {


    private lateinit var mGridView : GridView
    lateinit var channelDec: Array<String>
    private lateinit var channelImg: IntArray

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.activity_home_fragment,container,false)
        context?.let { MQTTHelper.connect(it) }
        mGridView=view.findViewById(R.id.channel1)

        return view


    }

    private fun initChannelView() {
        channelImg = intArrayOf(
            R.drawable.debug,
            R.drawable.library,
            R.drawable.library_ex,
            R.drawable.co2_f,
            R.drawable.fan_l,
            R.drawable.mqtt
        )

        channelDec = arrayOf(
            "串口USB调试",
            "图书馆无线管理",
            "图书馆排风系统",
            "无线C0²排风",
            "烟雾报警器",
            "MQTT云"
        )

        val channels= ArrayList<Channel>()
        for(i in channelDec.indices) {
            channels.add(Channel(channelDec[i],channelImg[i]))

        }
//        mGridView.adapter = ChannelAdapter(channelList.toList() as  ArrayList<Channel>, context)

        mGridView.adapter= ChannelAdapter(channels,context)


        mGridView.onItemClickListener= AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {

                0 -> startActivity(Intent(context, SerialHandle::class.java))

                1 -> startActivity(Intent(context, SerialLibrary::class.java))

                2-> startActivity(Intent(context,SerialLibraryEx::class.java))

                3->startActivity(Intent(context,FanActivity::class.java))

                4->startActivity(Intent(context,SmokeActivity::class.java))

                5->startActivity(Intent(context, MQTTActivity::class.java))

                else -> Toast.makeText(context, "TEST...", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

