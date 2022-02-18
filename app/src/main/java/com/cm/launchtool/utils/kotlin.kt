package com.cm.launchtool.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.cm.launchtool.MyApp


/**
 * 知晓了泛型实质化之后，我们可以简化 startActivity 的写法了
 */
//不能传参的startActivity
inline fun <reified T> startAct(context: Context){
    val intent = Intent(context,T::class.java)
    context.startActivity(intent)

}

/**
 * 借用高阶函数，实现 Intent 传参的startActivity
 * 回顾高阶函数基本规则：(String,Int) -> Unit
 * Unit 相当于java的void，表示没有返回值
 */
inline fun <reified T> startAct(context: Context, block : Intent.() -> Unit){
    val intent = Intent(context,T::class.java)
    //调用扩展的高阶函数
    intent.block()
    context.startActivity(intent)
}

fun tips(msg: String?,duration:Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.context, msg, duration).show()
}
//给字符串扩展一个tip函数
fun String.tip(duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(MyApp.context,this,duration).show()
}