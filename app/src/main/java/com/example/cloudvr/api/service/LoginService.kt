package com.example.cloudvr.api.service

import com.example.cloudvr.api.Network
import com.example.cloudvr.entity.UserResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

//    登录
    @POST("UserCenter/login")
    suspend fun login(
        @Query("loginName") loginName: String,
        @Query("password") password: String
    ): UserResponse

    companion object {
        fun instance(): LoginService {
            return Network.createService(LoginService::class.java)
        }
    }
}

