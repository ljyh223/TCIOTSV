package com.ljyhhh.tciotsv.home.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ljyhhh.tciotsv.R
import com.ljyhhh.tciotsv.home.Utils
import com.ljyhhh.tciotsv.home.base.MQTTData
import com.ljyhhh.tciotsv.home.mqtt.MQTTHelper
import kotlinx.android.synthetic.main.activity_serial_library.co2_value
import kotlinx.android.synthetic.main.activity_serial_library.switch_button_fan
import kotlinx.android.synthetic.main.activity_serial_library.switch_button_lamp
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


//library
class SerialLibrary : AppCompatActivity() {
    lateinit var key:String
    private val name="library"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serial_library)
        //fan
        switch_button_fan.setOnCheckedChangeListener { switchButton, b ->
            MQTTHelper.publish("test",Gson().toJson(MQTTData(name,"write","fan","off")),0)
            key="fan"
        }

        //lamp
        switch_button_lamp.setOnCheckedChangeListener { switchButton, b ->
            MQTTHelper.publish("test",Gson().toJson(MQTTData(name,"write","lamp","off")),0)
            key="lamp"
        }
        //co2
        co2_value.setOnClickListener {
            MQTTHelper.publish("test",Gson().toJson(MQTTData(name,"read","co2","")),0)
            key="co2"
        }
        if(MQTTHelper.flag){
            status(this,"服务器连接失败")
        }else{
            // change callback function
            MQTTHelper.mqttClient.setCallback(object : MqttCallback {
                //收到消息
                override fun messageArrived(topic: String?, message: MqttMessage?) {

                    val resultMap: Map<*, *>? = Gson().fromJson(message.toString(), Map::class.java)
                    if (resultMap != null) {
                        if(resultMap.keys.size==3){
                            switch_button_fan.isChecked=if(resultMap["fan"]=="01")  true else false
                            switch_button_lamp.isChecked=if(resultMap["lamp"]=="01")  true else false
                            co2_value.text=Utils.ppm(resultMap["co2"].toString()).toString()
                        }else{
//                            if(key.isInitialized)
                        }
                    }
                    Log.d(MQTTHelper.TAG, "Receive message: ${message.toString()} from topic: $topic")
                }
                //连接丢失
                override fun connectionLost(cause: Throwable?) {
                    Log.d(MQTTHelper.TAG, "Connection lost ${cause.toString()}")
                }
                //交付完成
                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }
            })

            // get init data
            val json=Gson().toJson(MQTTData("library","read","*",""))
            MQTTHelper.publish("test",json,0)
        }
    }


    fun status(c: Context, s:String) = Toast.makeText(c,s, Toast.LENGTH_SHORT).show()
}