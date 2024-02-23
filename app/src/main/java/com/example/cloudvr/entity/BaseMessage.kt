package com.example.cloudvr.entity


open class BaseMessage(
    open val code: Int = -1,
    open val message: String = ""
)

open class BaseMessageRpc(
    open val success: Boolean,
    open val msg: String = ""
)
