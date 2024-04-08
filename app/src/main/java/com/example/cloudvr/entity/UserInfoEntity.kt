package com.example.cloudvr.entity

data class UserData(
    val id:String,
    val note:String?,
    val userid:String,
    val imgUrl:String?,
    val mobile:String?,
    val email:String?,
    val name:String?,
)

data class UserResponse(val data: UserData?, override val code: Int, override val message: String) : BaseMessage(code,
    message
)
