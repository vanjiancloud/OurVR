package com.example.cloudvr


import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cloudvr.api.Network.setBaseUrl
import com.example.cloudvr.module.appContext
import com.example.cloudvr.ui.theme.Bg
import com.example.cloudvr.ui.theme.ButtonGreen
import com.example.cloudvr.ui.theme.GrayBtn




private lateinit var prefs: SharedPreferences



@Preview
@Composable
fun MySetting() {
    val navController = LocalNavController.current
    var serverUrl by remember {
        mutableStateOf("")
    }
    var serverIP by remember {
        mutableStateOf("")
    }
    var serverPort by remember {
        mutableStateOf("")
    }
    val protocolList = listOf("http", "https")
    var expanded by remember { mutableStateOf(false) }


//                获取保存的服务器信息
    prefs = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    val savedServerUrl = prefs.getString("serverUrl", "")
    val savedServerIP = prefs.getString("serverIP", "")
    val savedServerPort = prefs.getString("serverPort", "")

    if (!savedServerUrl.isNullOrEmpty() && !savedServerIP.isNullOrEmpty() && !savedServerPort.isNullOrEmpty()) {
        serverUrl = savedServerUrl
        serverIP = savedServerIP
        serverPort = savedServerPort
    }

    Box(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ){
        Card(
            modifier = Modifier.padding(15.dp),
            shape = MaterialTheme.shapes.medium
        ){
            Column(
                modifier = Modifier
                    .background(Bg)
                    .padding(33.dp,22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),contentAlignment = Alignment.CenterStart){
                    Image(
                        painter = painterResource(R.drawable.settinggreen),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )
                    Text(
                        text = "服务器设置",
                        color = Color.White,
                        fontSize = 27.sp,
                        modifier = Modifier.padding(50.dp,0.dp)
                    )
                }

//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    protocolList.forEachIndexed { index, text ->
//                        DropdownMenuItem(onClick = {
//                            println("Menu item clicked: index=$index, text=$text")
//                            expanded = false
//                        }) {
//                            Text(text = text) // 请确保传递了选项文本
//                        }
//                    }
//                }

                LabeledInput("中心服务器URL", serverUrl, onValueChange = {
                    serverUrl = it
                })
                LabeledInput("中心服务器IP", serverIP, onValueChange = {
                    serverIP = it
                })
                LabeledInput("端口号", serverPort, onValueChange = {
                    serverPort = it
                })

                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                navController.navigateUp()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GrayBtn,
                                disabledContainerColor = GrayBtn.copy(0.15f),
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .width(100.dp),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Text(text = "取消", color = Color.White, fontSize = 14.sp)
                        }
                        Button(
                            onClick = {
                                prefs.edit().putString("serverUrl", serverUrl).apply()
                                prefs.edit().putString("serverIP", serverIP).apply()
                                prefs.edit().putString("serverPort", serverPort).apply()
                                setBaseUrl()
                                Toast.makeText(appContext, "修改成功！", Toast.LENGTH_SHORT).show();
                                prefs.edit().remove("userId").apply()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonGreen,
                                disabledContainerColor = ButtonGreen.copy(0.15f),
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .width(120.dp).padding(start = 20.dp),
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Text(text = "确定", color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LabeledInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().background(Bg)) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(0.dp)
        )
        Input(
            text = "",
            value = value,
            placeholder = "请输入$label",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = onValueChange,
        )
    }
}