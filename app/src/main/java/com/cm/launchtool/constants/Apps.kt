package com.cm.launchtool.constants

import com.cm.launchtool.utils.FunctionTool
import com.cm.launchtool.utils.SPTool


fun getChooseApp():String{
    return SPTool.getLaunchApp()
}

fun saveChooseApp(app:String,packageName:String){
    SPTool.saveLaunchApp(app,packageName)
}

//获取app的启动时间，并排序
fun getTimeSettingsWithApp(app:String):ArrayList<String>{
    val settingList = FunctionTool.arrayStrTrimFormat(SPTool.get(app)).split(",")
    val editList = ArrayList<String>()
    editList.addAll(settingList)
    editList.sort()
    return editList
}

var timeSetting = getTimeSettingsWithApp(getChooseApp())
//重新加载
fun refreshTimeSetting(){
    timeSetting = getTimeSettingsWithApp(getChooseApp())
}