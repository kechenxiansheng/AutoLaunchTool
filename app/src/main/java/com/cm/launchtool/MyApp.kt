package com.cm.launchtool

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.cm.launchtool.utils.InStalledAppTool

class MyApp:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context : Context
    }


    override fun onCreate() {
        super.onCreate()
        context = this
    }
}