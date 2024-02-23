package com.example.cloudvr


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cloudvr.module.appContext
import com.example.cloudvr.ui.theme.CloudVRTheme
import com.example.cloudvr.viewModel.UserViewModel

open class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        初始值
        val prefs: SharedPreferences =
            appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val savedServerUrl = prefs.getString("serverUrl", "")
        val savedServerPort = prefs.getString("serverPort", "")
        if (savedServerUrl.isNullOrEmpty() || savedServerPort.isNullOrEmpty()) {
            prefs.edit().putString("serverUrl", "test.ourbim.com").apply()
            prefs.edit().putString("serverIP", "172.16.100.102").apply()
            prefs.edit().putString("serverPort", "11011").apply()
            prefs.edit().putString("serverProtocol", "http").apply()
//            prefs.edit().putString("serverUrl", "api.ourbim.com").apply()
//            prefs.edit().putString("serverIP", "42.81.206.139").apply()
//            prefs.edit().putString("serverPort", "11023").apply()
        }



        //TODO  写死进入B作为校验
//        startActivity(Intent(this,MainActivity::class.java))
//        receiverIntent()
        setContent {
            CloudVRTheme {
//                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingPreview()
                }
            }
        }
    }
}



// 定义 LocalNavController 键
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found") }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = "Login"){
//            是否登录
            val userViewModel = UserViewModel()
            if(userViewModel.userId === ""){
                composable("Login"){ Login() }
            }
            composable("Login"){ Login() }
            composable("projectList"){ MyComposablePreview() }
            composable("setting"){ MySetting() }
        }
    }
}

//fun receiverIntent(){
//    //    接收
//    println("接收1")
//    val receiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            println("接收$intent")
//            // 在这里处理接收到的广播内容
//            val data = intent?.getStringExtra("CONTENT")
//            println("接收$data")
//            // 处理接收到的数据
//            if(data == "99"){
//
//            }
//        }
//    }
//    val filter = IntentFilter("com.picovr.cloudxr.action.CLOUDXR_STATUS_CHANGE")
//    println("接收1$receiver$filter")
//    MyApplication.instance.registerReceiver(receiver, filter)
//}
