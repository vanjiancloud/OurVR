package com.example.cloudvr.api.service

import com.example.cloudvr.api.Network
import com.example.cloudvr.api.NetworkRpc
import com.example.cloudvr.entity.GetProjectIDAndTaskIdDataResponse
import com.example.cloudvr.entity.ProjectListResponse
import com.example.cloudvr.entity.ProjectTokenDataResponse
import com.example.cloudvr.entity.StartProjectResponse
import com.example.cloudvr.entity.StartWebUIResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ProjectListService {

//    项目列表
    @GET("appli/getApplicationList")
    suspend fun list(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("userid") userid:String
    ): ProjectListResponse

    companion object {
        fun instance(): ProjectListService {
            return Network.createService(ProjectListService::class.java)
        }
    }
}

interface ProjectTokenService {
    //    获取项目信息
    @GET("OurBim/getEnterToken")
    suspend fun getToken(
        @Query("appid") appid: String,
    ): ProjectTokenDataResponse

    companion object {
        fun instance(): ProjectTokenService {
            return Network.createService(ProjectTokenService::class.java)
        }
    }
}


interface ProjectIPService {
    //    获取项目IP和taskId
    @POST("OurBim/requestXr")
    suspend fun getProjectIDAndTaskId(
        @Query("appliId") appliId: String,
        @Query("plateType") plateType: String,//2：vr 3：ar
        @Query("token") token: String,
     ): GetProjectIDAndTaskIdDataResponse

    companion object {
        fun instance(): ProjectIPService {
            return Network.createService(ProjectIPService::class.java)
        }
    }
}

interface StartProjectService {
    //    开启vr
    @POST("OurBim/startXr")
    suspend fun startProject(
        @Query("appliId") appliId: String,
        @Query("plateType") plateType: String,//2：vr 3：ar
        @Query("token") token: String,
        @Query("senderId") senderId: String,
        @Query("nonce") nonce: String,
        @Query("hostId") hostId: String,//上一步返回的hostID（分配的服务器ip）
        @Query("mode") mode: String,//reload、reboot
        @Query("taskId") taskId: String,
        @Query("accessMode") accessMode: String,//1:正常模式 2：协同模式 3：互动模式
    ): StartProjectResponse

    companion object {
        fun instance(): StartProjectService {
            return Network.createService(StartProjectService::class.java)
        }
    }
}

interface StartWebUIService {
    @POST("{url}")
    suspend fun StartWebUI(
        @Url url: String, // 请求 URL
        @Body requestBody: RequestBody // 请求体
    ): StartWebUIResponse

    companion object {
        fun instance(): StartWebUIService {
            return NetworkRpc.createService(StartWebUIService::class.java)
        }
    }
}
