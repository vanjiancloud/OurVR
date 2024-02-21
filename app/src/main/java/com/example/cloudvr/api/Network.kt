package com.example.cloudvr.api


import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.example.cloudvr.module.appContext
import android.content.Context


object Network {

    private var retrofit = Retrofit.Builder()
        .baseUrl(getBaseUrl())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        ).build()
//        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())


    fun <T> createService(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }



    fun setBaseUrl() {
        try {
            val newRetrofit = retrofit.newBuilder().baseUrl(getBaseUrl()).build()
            retrofit = newRetrofit
        } catch (e: IllegalArgumentException) {
            // 处理无效地址异常
            // 例如显示错误消息给用户或进行其他操作
            e.printStackTrace()
        }
    }

    private fun getBaseUrl(): String {
        val prefs: SharedPreferences =
            appContext.getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
        val savedServerUrl = prefs.getString("serverUrl", "")
        val savedServerPort = prefs.getString("serverPort", "")
        return "http://$savedServerUrl:$savedServerPort/vjapi/"
    }
}