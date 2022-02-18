package com.cm.launchtool.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.NumberPicker
import com.cm.launchtool.R
import kotlinx.android.synthetic.main.dialog_date_time_picker.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 只有时间的选择器
 */
class TimeDialog(context: Context,var listener:SettingResultListener): Dialog(context),NumberPicker.OnValueChangeListener {
    private val tag = "LaunchTool@${javaClass.simpleName}"

    private lateinit var hourPicker:NumberPicker
    private lateinit var minutePicker:NumberPicker
    override fun onStart() {
        super.onStart()
        //dialog窗口属性
        window?.let {
            it.setGravity(Gravity.CENTER)   //显示在中间
            it.setBackgroundDrawableResource(android.R.color.transparent) //背景透明
            val attributes = it.attributes
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT
            it.attributes = attributes
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initPicker()
    }
    private fun initView(){
        setContentView(R.layout.dialog_time_picker)
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        hourPicker = findViewById(R.id.number_picker_hour)
        minutePicker = findViewById(R.id.number_picker_minute)
        //变化监听
        hourPicker.setOnValueChangedListener(this)
        minutePicker.setOnValueChangedListener(this)

        sure_btn.setOnClickListener {
//            Log.d(tag, "选择器时间 : ${hourPicker.value}时${minutePicker.value}分")
            val h = if(hourPicker.value < 10){
                "0${hourPicker.value}"
            }else{
                hourPicker.value
            }
            val m = if(minutePicker.value < 10){
                "0${minutePicker.value}"
            }else{
                minutePicker.value
            }
            val result = "$h:$m"
            //回传结果
            listener.result(result)
            dismiss()
        }
    }

    private fun initPicker(){
        val calendar: Calendar = Calendar.getInstance()
        //24小时制，限制小时数为0~23
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        hourPicker.value = calendar[Calendar.HOUR_OF_DAY]
        hourPicker.wrapSelectorWheel = false

        //限制分钟数为0~59
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.value = calendar[Calendar.MINUTE]
        minutePicker.wrapSelectorWheel = false
    }

    //监听时间选择器的变化
    override fun onValueChange(numberPicker: NumberPicker?, i: Int, i1: Int) {
//        Log.d(tag, "onValueChange: ${hourPicker.value}时${minutePicker.value}分")
    }

    interface SettingResultListener{
        fun result(result: String)
    }
}