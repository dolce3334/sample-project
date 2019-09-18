package com.waykichain.sample.webapi.controller

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback



/**
 * @ClassName: PublishSample
 * @Created by ch
 * @Date: 2019/9/18 17:25
 */
fun main() {
    val topic = "mqtt/test"
    val content = "hello world"

    val qos = 1
    val broker = "ws://mqtt.p2hp.com:8083"

    val clientId = "pubClient"

    val me = MemoryPersistence()

    try {
        val sampleClient = MqttClient(broker, clientId, me)
        val connOpts = MqttConnectOptions()
        sampleClient.connect(connOpts)

        val mqttMsg = MqttMessage(content.toByteArray())
        mqttMsg.qos = qos
        sampleClient.publish(topic, mqttMsg)
        sampleClient.disconnect()
        sampleClient.close()

    } catch (me: MqttException) {
        me.printStackTrace()
    }

}

object PublishSample {
    @JvmStatic
    fun main(args: Array<String>) {

        val topic = "mqtt/test"
        val content = "hello 哈哈"
        val qos = 1
        val broker = "ws://mqtt.p2hp.com:8083"
//        val userName = "test"
//        val password = "test"
        val clientId = "pubClient"
        // 内存存储
        val persistence = MemoryPersistence()

        try {
            // 创建客户端
            val sampleClient = MqttClient(broker, clientId, persistence)
            // 创建链接参数
            val connOpts = MqttConnectOptions()
            // 在重新启动和重新连接时记住状态
//            connOpts.isCleanSession = false
            // 设置连接的用户名
//            connOpts.userName = userName
//            connOpts.password = password.toCharArray()
            // 建立连接
            sampleClient.connect(connOpts)
            // 创建消息
            val message = MqttMessage(content.toByteArray())
            // 设置消息的服务质量
            message.qos = qos
            // 发布消息
            sampleClient.publish(topic, message)
            // 断开连接
            sampleClient.disconnect()
            // 关闭客户端
            sampleClient.close()
        } catch (me: MqttException) {
            println("reason " + me.reasonCode)
            println("msg " + me.message)
            println("loc " + me.localizedMessage)
            println("cause " + me.cause)
            println("excep $me")
            me.printStackTrace()
        }

    }
}

object SubscribeSample {

    @Throws(MqttException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val HOST = "ws://mqtt.p2hp.com:8083"
        val TOPIC = "mqtt/test"
        val qos = 1
        val clientid = "subClient"
        val userName = "test"
        val passWord = "test"
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            val client = MqttClient(HOST, clientid, MemoryPersistence())
            // MQTT的连接设置
            val options = MqttConnectOptions()
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.isCleanSession = true
            // 设置连接的用户名
//            options.userName = userName
            // 设置连接的密码
//            options.password = passWord.toCharArray()
            // 设置超时时间 单位为秒
            options.connectionTimeout = 10
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.keepAliveInterval = 20
            // 设置回调函数
            client.setCallback(object : MqttCallback {

                override fun connectionLost(cause: Throwable) {
                    println("connectionLost")
                }

                @Throws(Exception::class)
                override fun messageArrived(topic: String, message: MqttMessage) {
                    println("topic:$topic")
                    println("Qos:" + message.qos)
                    println("message content:" + String(message.payload))

                }

                override fun deliveryComplete(token: IMqttDeliveryToken) {
                    println("deliveryComplete---------" + token.isComplete)
                }

            })
            client.connect(options)
            //订阅消息
            client.subscribe(TOPIC, qos)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}