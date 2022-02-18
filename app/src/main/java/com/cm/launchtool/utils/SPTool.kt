package com.cm.launchtool.utils

import android.content.Context
import androidx.core.content.edit
import com.cm.launchtool.MyApp
import com.cm.launchtool.constants.dingding_package
import com.cm.launchtool.constants.refreshTimeSetting
import com.cm.launchtool.data.InstalledApp
import com.cm.launchtool.data.TargetApp

object SPTool {
    //保存app对应的启动时间
    fun save(targetApp: TargetApp){
        sharedPreferences().edit {
            putString(targetApp.appName,targetApp.launchTimes.toString().replace(" ",""))
        }
        refreshTimeSetting()
    }
    //获取app对应的启动时间
    fun get(appName:String):String{
        return sharedPreferences().getString(appName,"").toString().trim()
    }


    //保存当前需启动的app及其包名
    fun saveLaunchApp(appName:String,packageName:String){
        sharedPreferences().edit {
            putString("launchApp",appName)
            putString("launchAppPackage",packageName)
        }
    }
    //获取当前需启动的app
    fun getLaunchApp():String{
        return sharedPreferences().getString("launchApp","钉钉").toString().trim()
    }
    //获取当前需启动的app包名
    fun getLaunchAppPackage():String{
        return sharedPreferences().getString("launchAppPackage",dingding_package).toString().trim()
    }
    
    private fun sharedPreferences() = MyApp.context.getSharedPreferences("launchTool", Context.MODE_PRIVATE)
}