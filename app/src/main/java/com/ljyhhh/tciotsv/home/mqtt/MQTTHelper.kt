package com.ljyhhh.tciotsv.home.mqtt


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ljyhhh.tciotsv.home.HomeFragment
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

object MQTTHelper {

    val TAG = "AndroidMqttClient"
    private var _flag=false
    val flag: Boolean get() = _flag
    @SuppressLint("StaticFieldLeak")
    lateinit var mqttClient: MqttAndroidClient

    fun status(c: Context, s:String) = Toast.makeText(c,s, Toast.LENGTH_SHORT).show()


    //A<---->服务器<---->B
    //A,B订阅一个相同主题，当其中某一方发布消息时，服务器会向所有相同订阅的人推送消息
    //连接MQTT服务器
    fun connect(context: Context,autoSub:Boolean=false) {

        val serverURI = "tcp://172.245.205.179:1883"
        val username = "mosquitto"
        val password = "mosquitto"

        mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        //设置消息回调
        mqttClient.setCallback(object : MqttCallback {
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
            mqttClient.connect(options, null, object : IMqttActionListener {
                //连接成功
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    status(context,"Connection success")
                    _flag=true
                    if(autoSub) this@MQTTHelper.subscribe("test",0)
                    Log.d(TAG, "Connection success")
                }
                //连接失败
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    status(context,"Connection failure")
                    _flag=false
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

            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
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
    //退订
    fun unsubscribe(topic: String) {
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                //退订
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
    //发布消息
    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                //发布消息成功
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }



}