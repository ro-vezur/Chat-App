package com.example.chatapp

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initCloudinary()
    }

    private fun initCloudinary() {
        val config = hashMapOf<String,String>()
        config["cloud_name"] = cloudName
        config["api_key"] = cloudinaryApiKey
        MediaManager.init(this,config)
    }
}

