package com.example.cloudvr.compositionLocal

import androidx.compose.runtime.compositionLocalOf
import com.example.cloudvr.viewModel.UserViewModel

val LocalUserViewModel = compositionLocalOf<UserViewModel> { error("User View Model Context Not found") }