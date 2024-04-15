package com.example.cloudvr

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log

/**
 * 自定义 Application
 */
class MyApplication : Application() {

    companion object {
        lateinit var instance: Application
        var taskId: String = ""
    }

    init {
        instance = this
    }

}