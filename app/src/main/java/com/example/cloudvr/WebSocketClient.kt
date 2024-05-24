package com.example.cloudvr

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.cloudvr.module.appContext
import com.example.cloudvr.viewModel.ProjectListViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString


class WebSocketClient(url: String): Activity() {

    private val client = OkHttpClient()
    private val request = Request.Builder().url(url).build()
    private var webSocket: WebSocket? = null

    fun connect() {
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, e: String) {
                super.onMessage(webSocket, e)
                println("WebSocket Received message: $e")
                GlobalScope.launch {
                    val gson = Gson()
                    val jsonElement: JsonElement = gson.fromJson(e, JsonElement::class.java)
                    if (jsonElement.isJsonObject){
                        val jsonObject: JsonObject = jsonElement.asJsonObject
//                        加载完模型
                        if (jsonObject.get("id").asString == "8" && jsonObject.get("progress").asString.toDouble().toInt() == 1){
                            println("WebSocket 加载完模型: $jsonObject")
                            try {
                                data class wList(
                                    val bgColor: String,//WebUI的背景颜色 可传RGB颜色参数或者0，0表示“00000000”
                                    val width: String,//WebUI这个构件大小宽度的设置
                                    val hight: String,//WebUI这个构件大小高度的设置，也可理解为长度
                                    val isHide: String,//WebUI构件是否隐藏，true/false
                                    val relativeX: String,//WebUI与手或者头盔在X轴的相对距离
                                    val relativeY: String,//与relativeX同理
                                    val relativeZ: String,//与relativeX同理
                                    val rotation: String,//旋转值Yaw
                                    val rotationP: String,//旋转值pitch
                                    val rotationR: String,//旋转值roll
                                    val url: String,//url
                                    val webuiType: String,//类型：1-跟随左手柄、2-跟随右手柄、3-跟随头盔、4-跟随BIM构件对象；说明：交互页面不可跟随右手柄
                                )
                                val dataArray = arrayOf(
                                    wList("0000aaf1", "1920","1080","true","0","0","0","0","0","0","https://www.baidu.com","1"),
                                    wList("0000aaf1", "1920","1080","false","0","0","0","0","0","0","https://www.ourbim.com/","3"),
                                )
                                val gson = Gson()
                                val json = gson.toJson(dataArray)
                                val res = ProjectListViewModel().startWebUI(MyApplication.taskId,json)
                                println("WebSocket 加载完模型: $res")
                                Handler(Looper.getMainLooper()).post(Runnable {
                                    Toast.makeText(appContext, res.message, Toast.LENGTH_SHORT).show()
                                })
                            } catch (e: Exception) {
                                // 处理异常
                                println("异常$e")
                                Toast.makeText(appContext, "网络错误或网络地址错误$e", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                println("WebSocket Received bytes: ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                println("WebSocket closing: $code / $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                println("WebSocket closed: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                println("WebSocket failure: ${t.message}")
            }
        })
    }

    fun send(text: String) {
        webSocket?.send(text)
    }

    fun close() {
        webSocket?.close(100, "Goodbye!")
    }
    override fun onDestroy() {
        super.onDestroy()
        // 当 onDestroy 被调用时，将标志位设置为 true，表示应用正在退出
        close()
    }
}

//fun main() {
//    val url = "ws://api.ourbim.com:11023/vjapi/websocket/taskId"
//    val webSocketClient = WebSocketClient(url)
//    webSocketClient.connect()
//
//    GlobalScope.launch {
//        // Example usage: send a message every 5 seconds
//        while (true) {
//            webSocketClient.send("Hello, WebSocket!")
//            kotlinx.coroutines.delay(5000) // Delay for 5 seconds
//        }
//    }
//}
