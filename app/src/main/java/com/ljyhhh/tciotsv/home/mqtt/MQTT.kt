package com.ljyhhh.tciotsv.home.mqtt

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MQTT {
    lateinit var mqttClient: MqttAndroidClient
    val TAG = "AndroidMqttClient"

    fun status(c: Context, s:String) = Toast.makeText(c,s, Toast.LENGTH_SHORT).show()


    //A<---->服务器<---->B
    //A,B订阅一个相同主题，当其中某一方发布消息时，服务器会向所有相同订阅的人推送消息
    //连接MQTT服务器
    fun connect(context: Context) {

        val serverURI = "tcp://172.245.205.179:1883"
        val username = "mosquitto"
        val password = "mosquitto"

        this.mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        //设置消息回调
        this.mqttClient.setCallback(object : MqttCallback {
            //收到消息
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }
            //连接丢失
            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }
            //交付完成
            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()

        try {
            this.mqttClient.connect(options, null, object : IMqttActionListener {
                //连接成功
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    status(context,"Connection success")
                    Log.d(TAG, "Connection success")
                }
                //连接失败
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    status(context,"Connection failure")
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }
    //订阅
    fun subscribe(topic: String, qos: Int = 1) {
        try {

            this.mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                //订阅成功
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                }
                //订阅失败
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}