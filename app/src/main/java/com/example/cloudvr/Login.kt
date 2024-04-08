package com.example.cloudvr


import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudvr.module.appContext
import com.example.cloudvr.ui.theme.*
//import com.picovr.cloudxr.VjDevice
import com.example.cloudvr.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private lateinit var prefs: SharedPreferences
private const val KEY_USERNAME = "loginName"
private const val KEY_PASSWORD = "password"


@Preview
@Composable
fun Login(){
    val userViewModel: UserViewModel = viewModel()
    val scope = rememberCoroutineScope()
    val navController = LocalNavController.current
    var isLoading by remember { mutableStateOf(false) }//加载中

    var loginName by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    // 记住密码
    prefs = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    val savedUsername = prefs.getString(KEY_USERNAME, "")
    val savedPassword = prefs.getString(KEY_PASSWORD, "")

    if (!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
        loginName = savedUsername
        password = savedPassword
    }


//    println("设备信息----本地IP:${VjDevice.getIPAddress()}")
//    println("设备信息----网络名称:${VjDevice.getNetworkName()}")
//    println("设备信息----设备型号:${VjDevice.getDeviceModel()}")
//    println("设备信息----内存:${VjDevice.getDeviceMemory(appContext)}")
//    println("设备信息----存储:${VjDevice.getDeviceStorage(appContext,0)}")
//    println("设备信息----SDK版本:${VjDevice.getDeviceSDK()}")
//    println("设备信息----android版本:${VjDevice.getDeviceAndroidVersion()}")
//    println("设备信息----电量百分比:${VjDevice.getDeviceBattery(appContext)}")
//    println("设备信息----设备标识符:${VjDevice.getDeviceId(appContext)}")
//
//    println("设备信息----制造商:${VjDevice.getDeviceManufacturer()}")
//    println("设备信息----设备名:${VjDevice.getDeviceName()}")
//    println("设备信息----产品名:${VjDevice.getProductName()}")
//    println("设备信息----品牌:${VjDevice.getDeviceBrand()}")
//    println("设备信息----硬件:${VjDevice.getDeviceHardware()}")


    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(15.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .background(Bg)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = null,
                            modifier = Modifier.size(27.dp),
                        )
                        Text(
                            text = "登录",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 35.dp)
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.setting),
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable {
                                navController.navigate("setting")
                            }
                    )

                }
                // logo
                Box(
                    modifier = Modifier
                        .background(Color.White, shape = MaterialTheme.shapes.small)
                        .border(
                            width = 1.dp,
                            color = InputFocus,
                            shape = MaterialTheme.shapes.small
                        )
                        .width(46.dp)
                        .height(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Input(
                    text = "账号：",
                    value = loginName,
                    placeholder = "请输入账号",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = {
                        loginName = it
                    }
                )
                Input(
                    text = "密码：",
                    value = password,
                    placeholder = "请输入密码",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = {
                        password = it
                    }
                )

                // 记住密码
                val rememberPassword = remember { mutableStateOf(!savedUsername.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) }
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = rememberPassword.value,
                            onCheckedChange = { rememberPassword.value = it },
                        )
                        Text(text = "记住密码", color = Color.White, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        isLoading = true
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                val res = (userViewModel.login(loginName, password))
                                if(res.code==0){
                                    prefs.edit().putString("userId", res.data?.userid).apply()
                                    if (rememberPassword.value) {
                                        saveLoginInfo(loginName, password)
                                    } else {
                                        saveLoginInfo("", "")
                                    }

                                    scope.launch {
                                        delay(100)
                                        isLoading = false
                                        navController.navigate("projectList")
                                    }
                                }else{
                                    Toast.makeText(appContext, res.message, Toast.LENGTH_SHORT).show()
                                    isLoading = false
                                }
                            } catch (e: Exception) {
                                // 处理异常
                                isLoading = false
                                Toast.makeText(appContext, e.message?:"网络错误或网络地址错误", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonGreen,
                        disabledContainerColor = ButtonGreen.copy(0.15f),
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .width(150.dp),
                    enabled = loginName.isNotBlank() && password.isNotBlank(),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier= Modifier
                            .width(30.dp)
                            .padding(top = 3.dp, end = 10.dp))
                    }
                    Text(text = "登录", color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}

fun saveLoginInfo(loginName: String, password: String) {
    prefs.edit().apply {
        putString(KEY_USERNAME, loginName)
        putString(KEY_PASSWORD, password)
        apply()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    text: String,
    value: String,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    onValueChange:(String) -> Unit
){
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = InputFocus,
        unfocusedBorderColor = InputDefault,
        cursorColor = InputFocus,
        placeholderColor = InputPlaceholder,
        textColor = Color.White
    )
    Row (
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = text,color = Color.White)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .padding(8.dp)
                .height(50.dp),
            colors = colors
        )
    }
}

