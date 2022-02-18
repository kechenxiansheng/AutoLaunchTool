package com.cm.launchtool.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil{
    const val TIME_FORMAT_1:String = "yyyyMMddHHmmss"
    const val TIME_FORMAT_2:String = "yyyyMMddHHmmssSSS"
    const val TIME_FORMAT_3:String = "yyyy-MM-dd HH:mm:ss"
    const val TIME_FORMAT_4:String = "yyyyMMdd"
    const val TIME_FORMAT_5:String = "yyyy/MM/dd"
    const val TIME_FORMAT_6:String = "MM/dd"
    const val TIME_FORMAT_7:String = "yyyy-MM-dd"
    const val TIME_FORMAT_8:String = "yyyy-MM-dd HH:mm"
    const val TIME_FORMAT_9:String = "yyyyMMddHHmm"

    /**
     * Date转为指定格式时间
     * @param format    时间格式
     * @param date      需要转换的时间对象
     * @return          指定格式的时间字符串
     */
    fun convertDate(format: String, date: Date): String {
        if (TextUtils.isEmpty(format)) {
            return ""
        }
        val dateFormat = SimpleDateFormat(format)
        return dateFormat.format(date)
    }

    /**
     * 时间戳转为指定格式时间
     * @param format    时间格式
     * @param time      需要转换的时间戳，long类型（13位）
     * @return          指定格式的时间字符串
     */
    fun convertDate(format: String, time: Long): String {
        if (TextUtils.isEmpty(format)) {
            return ""
        }
        val dateFormat = SimpleDateFormat(format)
        val date = Date(time)
        return dateFormat.format(date)
    }

    /**
     * 获取指定格式的 当前时间 带秒
     * @param format   时间格式，默认 yyyyMMddHHmmss
     */
    fun getTimeSecond(format: String = TIME_FORMAT_1):String{
        val dateFormat = SimpleDateFormat(format)
        val date = Date(System.currentTimeMillis())
        return dateFormat.format(date)
    }
    /**
     * 获取指定格式的 当前时间 只到分
     * @param format   时间格式，默认 yyyyMMddHHmmss
     */
    fun getTime(format: String = TIME_FORMAT_9):String{
        val dateFormat = SimpleDateFormat(format)
        val date = Date(System.currentTimeMillis())
        return dateFormat.format(date)
    }

    /**
     * 计算 某天的前n天或者后n天
     * @param time  yyyy-MM-dd 格式的日期 2021-03-28
     * @param step  间隔天数，前n天传负数，后n天传正数
     * @return 时间戳
     */
    fun stepDay(time: String, step: Int): Long {
//        System.out.println("startTime : " + time);
        val stepDay = (step * 24 * 60 * 60 * 1000).toLong()
        val sf = SimpleDateFormat("yyyy-MM-dd")
        var stepDayTime: Long = 0
        try {
            val date = sf.parse(time)
            val curDay = date.time
            stepDayTime = curDay + stepDay
            //            System.out.println("resultTime : " +stepDayTime);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stepDayTime
    }

    /**
     * 计算 某天的前n天或者后n天
     * @param time  yyyy-MM-dd 格式的日期 2021-03-28
     * @param step  间隔天数，前n天传负数，后n天传正数
     * @return yyyy-MM-dd 格式的日期
     */
    fun stepDayFormatted(time: String, step: Int): String {
//        System.out.println("startTime : " + time);
        val stepDay = (step * 24 * 60 * 60 * 1000).toLong()
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var resultTime = ""
        try {
            val date = sdf.parse(time)
            val curDay = date.time
            val stepDayTime = curDay + stepDay
            resultTime = sdf.format(stepDayTime)
            //            System.out.println("resultTime : " +stepDayTime);
//            System.out.println("resultTime : " +resultTime);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultTime
    }


    /**
     * 计算某月的前n月或者后n月
     * @param startMonth  yyyyMM 格式的日期 202103
     * @param step  间隔月数
     * @return
     */
    fun stepMonth(startMonth: String, step: Int): String {
//        System.out.println("startMonth : " + startMonth);
        val sdf = SimpleDateFormat("yyyyMM")
        var resultMonth = ""
        try {
            val date = sdf.parse(startMonth)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MONTH, step) //日期加减n个月
            val addDate = calendar.time
            resultMonth = sdf.format(addDate)
            //            System.out.println("resultMonth : " + resultMonth);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return resultMonth
    }

    /**
     * 拼接年月日
     * @return 返回 2021-01-01 格式的字符串
     * */
    fun joiningYearMonthDay(year: Int, month: Int, day: Int):String{
        var date = "$year"
        date += if(month < 10) {
            "-0${month + 1}"
        } else  {
            "-${month + 1}"
        }
        date += if(day < 10) {
            "-0$day"
        } else  {
            "-$day"
        }
        return date
    }
    /**
     * 拼接月日
     * @return 返回 01-01 格式的字符串
     * */
    fun joiningMonthDay(month: Int, day: Int):String{
        var date = if(month < 10) {
            "0${month + 1}"
        } else  {
            "${month + 1}"
        }
        date += if(day < 10) {
            "-0$day"
        } else  {
            "-$day"
        }
        return date
    }

    /**
     * 周（本月）
     * 默认当前时间所属周
     */
    fun getWeek(i: Long = System.currentTimeMillis()): Int {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        return c[Calendar.WEEK_OF_MONTH]
    }

    /**
     * 年
     * 默认当前时间所属年
     */
    fun getYear(i: Long = System.currentTimeMillis()): Int {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        return c[Calendar.YEAR]
    }
    /**
     * 月
     * 默认当前时间所属年的月份
     */
    fun getMonth(i: Long = System.currentTimeMillis()): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        val month = c[Calendar.MONTH]
        var m = ""
        m = if(month < 10){
            "0$month"
        }else{
            "$month"
        }
        return m
    }
    /**
     * 日
     * 默认当前时间所属月的日份
     */
    fun getDay(i: Long = System.currentTimeMillis()): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        val day = c[Calendar.DAY_OF_MONTH]
        var d = ""
        d = if(day < 10){
            "0$day"
        }else{
            "$day"
        }
        return d
    }
    //时（当天）
    fun getHour(i: Long = System.currentTimeMillis()): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        val hour = c[Calendar.HOUR_OF_DAY]
        var h = ""
        h = if(hour < 10){
            "0$hour"
        }else{
            "$hour"
        }
        return h
    }
    //分
    fun getMinute(i: Long = System.currentTimeMillis()): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        val minute = c[Calendar.MINUTE]
        var m = ""
        m = if(minute < 10){
            "0$minute"
        }else{
            "$minute"
        }
        return m
    }
    //秒
    fun getSecond(i: Long = System.currentTimeMillis()): String {
        val c = Calendar.getInstance(Locale.CHINA)
        c.timeInMillis = i
        val second = c[Calendar.SECOND]
        var s = ""
        s = if(second < 10){
            "0$second"
        }else{
            "$second"
        }
        return s
    }

    //当前公历 月日
    fun curGongLiTime():String{
        val t = System.currentTimeMillis()
        val monthInt = getMonth(t) as Int + 1
        val month = if(monthInt<10){
            "0$monthInt"
        }else{
            "$monthInt"
        }
        val dayInt = getDay(t) as Int
        val day = if(dayInt<10){
            "0$dayInt"
        }else{
            "$dayInt"
        }

        return "$month-$day"
    }
}

