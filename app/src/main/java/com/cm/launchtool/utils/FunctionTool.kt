package com.cm.launchtool.utils

object FunctionTool {
    fun arrayStrTrimFormat(str:String):String{
        return str.replace("[","").replace("]","").replace(" ","")
    }
}