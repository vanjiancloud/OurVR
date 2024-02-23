package com.example.cloudvr.viewModel

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cloudvr.api.service.ProjectIPService
import com.example.cloudvr.api.service.ProjectListService
import com.example.cloudvr.api.service.ProjectTokenService
import com.example.cloudvr.api.service.StartProjectService
import com.example.cloudvr.api.service.StartWebUIService
import com.example.cloudvr.entity.GetProjectIDAndTaskIdDataResponse
import com.example.cloudvr.entity.ProjectListEntity
import com.example.cloudvr.entity.ProjectTokenDataResponse
import com.example.cloudvr.entity.StartProjectResponse
import com.example.cloudvr.entity.StartWebUIResponse
import com.example.cloudvr.module.appContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.http2.Http2Reader.Companion.logger

class ProjectListViewModel : ViewModel() {


//    var list by mutableStateOf(listOf(ProjectListEntity(appName="test",progress=10,appType="2",isGis="false",applidStatus="1")))
    var list by mutableStateOf<List<ProjectListEntity>>(emptyList())

    var refreshing by mutableStateOf(false)//是否正在刷新
    var listLoaded by mutableStateOf(false)//是否正在加载

    //是否还有更多
    var hasMore = false

    private val userViewModel: UserViewModel = UserViewModel()

    private var pageNo = 1//分页
    private var pageSize = 15//数量
    private var userid =  userViewModel.userId//用户id


    suspend fun fetchList() {
        val projectListService = ProjectListService.instance()
        listLoaded = true
        try {
            val res = projectListService.list(pageNo = pageNo, pageSize= pageSize, userid = userid)
            if (res.code == 0 && res.data != null) {
                val tmpList = mutableListOf<ProjectListEntity>()
                if (pageNo != 1) {
                    tmpList.addAll(list)
                }
                tmpList.addAll(res.data.list!!)
                list = tmpList
                //是否还有更多数据
                hasMore = list!!.size < res.data.total
                listLoaded = false
                refreshing = false
            } else {
                pageNo--
                if (pageNo <= 1) {
                    pageNo = 1
                }
                Toast.makeText(appContext, res.message, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // 处理异常
            Toast.makeText(appContext, "网络错误或网络地址错误", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun refresh(){
        pageNo = 1
        fetchList()
    }

    suspend fun loadMore() {
        logger.info("加载更多")
        if (hasMore) {
            pageNo++
            fetchList()
        }
    }

    //    获取token和url
    suspend fun getToken(appid: String): ProjectTokenDataResponse {
        val tokenService = ProjectTokenService.instance()
        return tokenService.getToken(appid)
    }
    //    获取项目ip和taskid
    suspend fun getIPAndTaskId(appliId: String, token: String): GetProjectIDAndTaskIdDataResponse {
        val service = ProjectIPService.instance()
        return service.getProjectIDAndTaskId(appliId,"2",token)
    }

    //    启动vr
    suspend fun startVr(appliId: String, token: String,senderId: String,nonce: String,hostId: String,mode: String,taskId: String,accessMode: String): StartProjectResponse {
        val service = StartProjectService.instance()
        return service.startProject(appliId, plateType = "2",token,senderId,nonce,hostId,mode,taskId,accessMode)
    }

    suspend fun startWebUI(taskid: String, nonce: String, sender_id: String): StartWebUIResponse {
        val service = StartWebUIService.instance()
        val json = """
            {
                taskid:$taskid,
                nonce:$nonce,
                sender_id:$sender_id,
                "ReqData": {
                    "data": "北京",
                    "msgType": "WebUiUrl",
                }
            }
        """.trimIndent()
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        return service.StartWebUI("RpcReq",requestBody)
    }
}