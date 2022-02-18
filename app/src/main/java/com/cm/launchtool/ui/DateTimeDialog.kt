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
 * 有年月日的时间选择器
 */
class DateTimeDialog(context: Context, var listener:SettingResultListener): Dialog(context),NumberPicker.OnValueChangeListener {
    private val tag = "LaunchTool@${javaClass.simpleName}"

    private lateinit var yearPicker:NumberPicker
    private lateinit var monthPicker:NumberPicker
    private lateinit var datePicker:NumberPicker
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
        setContentView(R.layout.dialog_date_time_picker)
        setCanceledOnTouchOutside(true)
        setCancelable(true)

        yearPicker = findViewById(R.id.number_picker_year)
        monthPicker = findViewById(R.id.number_picker_month)
        datePicker = findViewById(R.id.number_picker_date)
        hourPicker = findViewById(R.id.number_picker_hour)
        minutePicker = findViewById(R.id.number_picker_minute)
        //变化监听
        yearPicker.setOnValueChangedListener(this)
        monthPicker.setOnValueChangedListener(this)
        datePicker.setOnValueChangedListener(this)
        hourPicker.setOnValueChangedListener(this)
        minutePicker.setOnValueChangedListener(this)

        sure_btn.setOnClickListener {
            Log.d(tag, "listener: ${yearPicker.value}年${monthPicker.value}月${datePicker.value}日${hourPicker.value}时${minutePicker.value}分")
            val result = "${yearPicker.value}${monthPicker.value}${datePicker.value}${hourPicker.value}${minutePicker.value}"
            //回传结果
            listener.result(result)
            dismiss()
        }
    }

    private fun initPicker(){
        val calendar: Calendar = Calendar.getInstance()
        //限制年份范围为前后五年
        val yearNow = calendar[Calendar.YEAR]
        yearPicker.minValue = yearNow - 3
        yearPicker.maxValue = yearNow + 3
        yearPicker.value = yearNow
        yearPicker.wrapSelectorWheel = false    //关闭选择器循环

        //设置月份范围为1~12
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = calendar[Calendar.MONTH] + 1
        monthPicker.wrapSelectorWheel = false

        //日期限制存在变化，需要根据当月最大天数来调整
        datePicker.minValue = 1
        datePicker.maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        datePicker.value = calendar[Calendar.DATE]
        datePicker.wrapSelectorWheel = false

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

    override fun onValueChange(numberPicker: NumberPicker?, i: Int, i1: Int) {
//        Log.d(tag, "onValueChange: ${yearPicker.value}年${monthPicker.value}月${datePicker.value}日${hourPicker.value}时${minutePicker.value}分")

        val dateStr = String.format(Locale.CHINA, "%d-%d", yearPicker.value, monthPicker.value)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM", Locale.CHINA)
        val calendar = Calendar.getInstance()
        calendar.time = simpleDateFormat.parse(dateStr)
        val dateValue: Int = datePicker.value
        val maxValue = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        datePicker.maxValue = maxValue
        //重设日期值，防止月份变动时超过最大值
        datePicker.value = Math.min(dateValue, maxValue)
    }

    interface SettingResultListener{
        fun result(result: String)
    }
}