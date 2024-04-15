package com.example.cloudvr



import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cloudvr.module.appContext
import com.example.cloudvr.ui.theme.CloudVRTheme
import com.example.cloudvr.viewModel.UserViewModel
import com.picovr.cloudxr.MainActivity

open class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        初始值
        val prefs: SharedPreferences =
            appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val savedServerUrl = prefs.getString("serverUrl", "")
        val savedServerPort = prefs.getString("serverPort", "")
        if (savedServerUrl.isNullOrEmpty() || savedServerPort.isNullOrEmpty()) {
            prefs.edit().putString("serverUrl", "api.ourbim.com").apply()
            prefs.edit().putString("serverIP", "42.81.206.139").apply()
            prefs.edit().putString("serverPort", "11023").apply()
            prefs.edit().putString("serverProtocol", "https").apply()
        }

        // 请求获取内存权限
        requestStoragePermission()

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


        // 添加应用程序生命周期监听器
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Activity 创建时的回调
                println("Activity创建时的回调")
            }

            override fun onActivityStarted(activity: Activity) {
                // Activity 启动时的回调
                println("Activity启动时的回调")
            }

            override fun onActivityResumed(activity: Activity) {
                // Activity 恢复时的回调
                println("Activity恢复时的回调")
            }

            override fun onActivityPaused(activity: Activity) {
                // Activity 暂停时的回调
                println("Activity暂停时的回调")
            }

            override fun onActivityStopped(activity: Activity) {
                // Activity 停止时的回调
                println("Activity停止时的回调")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // Activity 保存状态时的回调
                println("Activity保存状态时的回调")
            }

            override fun onActivityDestroyed(activity: Activity) {
                // Activity 销毁时的回调
                println("Activity销毁时的回调")
            }
        })
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // 用户同意了权限请求，可以执行相关操作
            // 可以在这里进行文件操作等
        } else {
            // 用户拒绝了权限请求，可以向用户解释为何需要这个权限
        }
    }

    private fun requestStoragePermission() {
        // 请求获取内存权限
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}



// 定义 LocalNavController 键
val LocalNavController = compositionLocalOf<NavController> { error("No NavController found") }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    //            是否登录
    val userViewModel = UserViewModel()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = if (!userViewModel.userId.isNullOrEmpty()) "projectList" else "Login"){
            composable("Login"){ Login() }
            composable("projectList"){ MyComposablePreview() }
            composable("setting"){ MySetting() }
        }
    }
}

