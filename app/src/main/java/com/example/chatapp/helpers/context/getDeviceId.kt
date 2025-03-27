package com.example.chatapp.helpers.context

import android.content.Context
import android.provider.Settings.Secure

fun Context.deviceId() = Secure.getString(this.contentResolver,Secure.ANDROID_ID)