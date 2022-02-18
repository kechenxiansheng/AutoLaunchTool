package com.cm.launchtool.data

import android.graphics.drawable.Drawable

data class InstalledApp(
    var uid:String,         //应用编号
    var name:String,        //名称
    var packageName:String, //包名
    var icon:Drawable?      //图标
){
    constructor():this("","","",null)
}
