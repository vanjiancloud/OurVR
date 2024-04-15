package com.example.cloudvr




import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cloudvr.entity.ProjectListEntity
import com.example.cloudvr.extension.OnBottomReached
import com.example.cloudvr.module.appContext
import com.example.cloudvr.ui.theme.Bg
import com.example.cloudvr.ui.theme.ButtonGreen
import com.example.cloudvr.ui.theme.GrayBtn
import com.example.cloudvr.viewModel.ProjectListViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import com.picovr.cloudxr.MainActivity
import com.picovr.cloudxr.VjMD5Tool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


var publicIp = ""
var hostId = ""
var token = ""


@Preview
@OptIn(com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun MyComposablePreview() {
    val prefs = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    val navController = LocalNavController.current
    var showDialog by remember { mutableStateOf(false) }//退出弹窗显示
    var projectListViewModel: ProjectListViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    lazyListState.OnBottomReached(buffer = 1) {
        coroutineScope.launch {
            projectListViewModel.loadMore()
        }
    }
    LaunchedEffect(Unit) {
        //获取列表数据
        projectListViewModel.fetchList()
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.width(85.dp)){
                Box(
                    modifier = Modifier
                        .background(Bg, shape = MaterialTheme.shapes.small)
                        .width(70.dp)
                        .height(70.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .background(Bg, shape = MaterialTheme.shapes.small)
                        .width(70.dp)
                        .height(70.dp)
                        .clickable {
                            showDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.quit),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    if(showDialog){
                        ExitDialog(showDialog=true,onConfirmExit={
                            showDialog = false
                            prefs.edit().remove("userId").apply()
                            navController.navigate(
                                route = "Login",
                                builder = {
                                    popUpTo("projectList") {
                                        inclusive = true
                                    }
                                }
                            )
                        },onCancelExit={
                            showDialog = false
                        })
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)){
                SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing = projectListViewModel.refreshing), onRefresh = {
                    coroutineScope.launch {
                        projectListViewModel.refresh()
                    }
                }) {
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .background(Bg),state = lazyListState
                    ) {
                        items(projectListViewModel.list) {item ->
                            ListItemRow(item)
                        }
                        items(1) {
                            println("加载中" +projectListViewModel.listLoaded)
                            if (projectListViewModel.listLoaded) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "加载中...",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            if (!projectListViewModel.listLoaded && !projectListViewModel.hasMore) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "暂无更多数据",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListItemRow(item: ProjectListEntity) {
    Row(modifier = Modifier.padding(15.dp)) {
        Image(
            painter = painterResource(R.drawable.model),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 20.dp)
                .clickable{
                    startProject(item)
                },
        ) {
            Text(
                text = item.appName, color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = appType(item), color = Color(0xd4EBEBEB), fontSize = 14.sp)
                Row(modifier = Modifier.fillMaxWidth()
                    ,verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    if(item.applidStatus!="1"&&item.applidStatus!="6"){
                        Image(
                            painter = painterResource(R.drawable.finish),
                            contentDescription = null,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "转换完成",color = Color.White,fontSize = 14.sp)
                    }else{
                        val progress = item.progress.toFloat()
                        progressbar(value = progress)
                    }
                }
            }
        }
        Box(contentAlignment = Alignment.Center) {
            Button(
                onClick = { startProject(item) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                ),
                modifier = Modifier
                    .height(41.dp)
                    .width(120.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "启动模型", color = Color.White, fontSize = 14.sp)
            }
        }
    }
}


@Composable
private fun progressbar(modifier: Modifier = Modifier, value: Float) {
    Box(modifier = Modifier
        .width(width = 100.dp)
        .height(19.dp)
        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(20.dp))
        .padding(start = 8.dp, end = 8.dp, top = 0.dp)) {
        Canvas(
            modifier
                .fillMaxWidth()
                .padding(top = 9.5.dp)) {
            val canvasWidth = size.width  // 画布的宽
            val strokeWidth = 40f
            val path = Path()
            path.lineTo(canvasWidth*(value/100), 0f)
            drawPath(
                path = path,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                ),
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF9DC9FF), Color(0xFF6CA0FF))
                )
            )
        }
        Row(modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 0.dp)) {
            Spacer(Modifier.weight(1f))
            Text("$value%",color = Color.White, fontSize = 12.sp)
        }
    }
}

//模型类型
fun appType(item: ProjectListEntity): String {
    if(item.appType == "0" && item.isGis == "false"){
        return "普通模型"
    }else if(item.appType == "0" && item.isGis == "true"){
        return "GIS模型"
    }else if(item.appType == "1"){
        return "漫游模型"
    }else if(item.appType == "3" && item.isGis == "false"){
        return "链接模型"
    }else if(item.appType == "4"){
        return "示例模型"
    }else if(item.appType == "5"){
        return "云应用"
    }else if(item.appType == "3" && item.isGis == "true"){
        return "GIS链接模型"
    }else{
        return "其他模型"
    }
}

//退出弹窗
@Composable
fun ExitDialog(
    showDialog: Boolean,
    onConfirmExit: () -> Unit,
    onCancelExit: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onCancelExit,
            containerColor = Bg,
            textContentColor = Color.White,
            titleContentColor = Color.White,
            title = { Text(text = "退出",fontSize = 24.sp) },
            text = {
                Box(modifier = Modifier.fillMaxWidth(),contentAlignment = Alignment.TopCenter) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                    Text("请确认是否要退出应用？",fontSize = 18.sp,modifier = Modifier.padding(top = 55.dp))
                }
            },
            confirmButton = {
                Button(onClick = onConfirmExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonGreen,
                    ),
                    modifier = Modifier.width(126.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.quitwhite),
                        contentDescription = null,
                        modifier = Modifier.size(27.dp)
                    )
                    Text("退出",fontSize = 20.sp)
                }
            },
            dismissButton = {
                Button(onClick = onCancelExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayBtn,
                    ),
                    modifier = Modifier.width(126.dp)
                ) {
                    Text("取消",fontSize = 20.sp)
                }
            }
        )
    }
}


//启动项目
var projectId = ""
fun startProject(item: ProjectListEntity){
    projectId = item.appid

    startVrPre()
}


fun receiverIntent(){
    //    接收
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // 在这里处理接收到的广播内容
            val data = intent?.getIntExtra("CONTENT",-1)
            val msg = intent?.getStringExtra("MSG")
            println("vjSendBroadcast---------------$data,msg=$msg")
            // 加载模型
            if(data == 3){
                val nonce = UUID.randomUUID().toString()
                if (!MyApplication.taskId.isNullOrEmpty()&&!projectId.isNullOrEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
//                        delay(10000)
                        val res = ProjectListViewModel().startVr(appliId=projectId,token,senderId= VjMD5Tool.GetHardId(),nonce,hostId,mode="reboot",MyApplication.taskId,accessMode="1")
                        if(res.code == 0){
                            val url = "wss://api.ourbim.com:11023/vjapi/websocket/${MyApplication.taskId}"
                            val webSocketClient = WebSocketClient(url)
                            webSocketClient.connect()
                        }
                        println("启动成功！$res----$projectId---$MyApplication.taskId------$nonce---$hostId")
                    }
                }
            }
//            退出
//            else if(data == 99){
//                CoroutineScope(Dispatchers.Main).launch {
//                    closeStream()
//                    ProjectListViewModel().closeVr("-taskid=$taskId",taskId)
//                    println("关闭")
//                }
//            }
        }
    }
    val filter = IntentFilter(MainActivity.EVENT_CLOUDXR_STATUS_CHANGE)
    MyApplication.instance.registerReceiver(receiver, filter)
}


//开启vr
fun startVrPre(){
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val res = ProjectListViewModel().getToken(projectId)
            if(res.code==0){
                token = res.data?.token ?: ""
                var projectMess = ProjectListViewModel().getIPAndTaskId(appliId=projectId,token)
                if(projectMess.code==0){
                    val data = projectMess.data
                    hostId = data?.hostID.toString()
                    publicIp = data?.publicIp.toString()
                    MyApplication.taskId = data?.taskId.toString()


                    MainActivity.writeIP(publicIp)//写入ip地址

                    toModel()
                    receiverIntent()
                }else{
                    Toast.makeText(appContext, projectMess.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(appContext, res.message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 处理异常
            println("异常$e")
            Toast.makeText(appContext, "网络错误或网络地址错误$e", Toast.LENGTH_SHORT).show()
        }
    }
}

//启动webui
//fun startWebUI(){
//    val prefs: SharedPreferences = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
//    val savedServerUrl = prefs.getString("serverUrl", "")
//    var url = "https://$savedServerUrl:14041/api/v1/RpcReq"
//
//}

//跳去模型
fun toModel(){


    val prefs: SharedPreferences = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
    val savedServerUrl = prefs.getString("serverUrl", "")
//    val savedServerIP = prefs.getString("serverIP", "")

    val intent = Intent(MyApplication.instance, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra("serverUrl", savedServerUrl)
    intent.putExtra("hostId", hostId)
    intent.putExtra("taskId", MyApplication.taskId)

    MyApplication.instance.startActivity(intent)
}

//fun closeStream(){
//    CoroutineScope(Dispatchers.Main).launch {
//        val prefs: SharedPreferences = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
//        val savedServerIP = prefs.getString("serverIP", "")
//        val nonce = UUID.randomUUID().toString()
//        ProjectListViewModel().closeStream(VjMD5Tool.GetHardId(),savedServerIP,nonce,"vr","reboot",taskId)
//    }
//}

