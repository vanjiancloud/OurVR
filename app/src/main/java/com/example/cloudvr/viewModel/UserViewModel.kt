package com.example.cloudvr.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.cloudvr.api.service.LoginService
import com.example.cloudvr.entity.UserResponse
import com.example.cloudvr.module.appContext

class UserViewModel:ViewModel() {

    //    是否已登录,获取用户id
    val userId:String
        get() {
            val prefs = appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
            return prefs.getString("userId", "") ?: ""
        }

    //    登录操作
    suspend fun login(loginName: String, password: String): UserResponse {
        val userInfoService = LoginService.instance()
        return userInfoService.login(loginName, password)
    }
}