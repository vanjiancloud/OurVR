package com.example.cloudvr.entity

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class ProjectListEntity (
    val appid: String,
    val appName: String,
    val progress: Int,
    val appType: String,
    val isGis: String,
    val applidStatus: String
)

data class ProjectData(
    val list : List<ProjectListEntity>,
    val total:Int
)


data class ProjectListResponse(val data: ProjectData?, override val code: Int,
                               override val message: String
) : BaseMessage(code, message)





data class ProjectTokenData(
    val token:String,
    val url:String
)
data class ProjectTokenDataResponse(val data: ProjectTokenData?, override val code: Int, override val message: String) : BaseMessage(code,
    message
)



data class GetProjectIDAndTaskIdData(
    val hostID:String,
    val publicIp:String?,
    val taskId:String
)
data class GetProjectIDAndTaskIdDataResponse(val data: GetProjectIDAndTaskIdData?,override val code: Int, override val message: String) : BaseMessage(code,
    message
)




data class StartProjectResponse(override val code: Int, override val message: String) : BaseMessage(code,
    message
)

data class StartWebUIResponse(override val success: Boolean, override val msg: String) : BaseMessageRpc(success,
    msg
)
