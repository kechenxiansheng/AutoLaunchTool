package com.cm.launchtool.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import com.cm.launchtool.constants.*
import com.cm.launchtool.utils.AppTool
import com.cm.launchtool.utils.AppTool.isForeground
import com.cm.launchtool.utils.AppTool.isScreenOn
import com.cm.launchtool.utils.AppTool.launchAppForPackage
import com.cm.launchtool.utils.AppTool.moveTaskToFront
import com.cm.launchtool.utils.NotificationUtil
import com.cm.launchtool.utils.SPTool
import com.cm.launchtool.utils.TimeUtil
import java.util.*

/**
 * 自动启动应用的服务
 * WakeLock
 * flags:
 *  PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
    SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
    SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
    FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
    ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
    ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
  权限：
    "android.permission.WAKE_LOCK"
    "android.permission.DEVICE_POWER"
 */
class AutoLaunchService : Service(){
    private val tag = "LaunchToolService@${javaClass.simpleName}"
    private lateinit var wakeLock:PowerManager.WakeLock     //锁屏唤醒
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.FULL_WAKE_LOCK,  //指定等级和唤醒类型标志
            "app启动服务"
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        foreground()
        launchTask()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        //回收资源
        wakeLock.release()
    }
    private fun foreground(){
        val notification = NotificationUtil.createNotification(
            this,
            "autoLaunch", "定时启动工具", "app定时启动服务", "正在运行中...")
        startForeground(1,notification)
    }

    /**
     * 自启动的定时任务
     * 2秒执行一次
     */
    private fun launchTask(){
        //轮训设置的时间数据，如果当前时分与设置时间相同则启动对应app
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val currentTime = "${TimeUtil.getHour()}${TimeUtil.getMinute()}"
//                Log.d(tag, "当前时间 : $currentTime")
                val lock = isScreenOn(this@AutoLaunchService)
                //让服务不受锁屏影响
                if (!lock) {
                    Log.w(tag, "当前为锁屏状态")
                    //亮屏（超过10分钟自动释放 WakeLock）
                    wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)
                }
                if (!lock) {
                    Log.w(tag, "仍然是锁屏状态")
                    return
                }
                val mutableSettings = timeSetting
//                Log.d(tag, "缓存中设置的时间 : $mutableSettings ，数量：${mutableSettings.size}")
                //使用迭代器遍历删除，效果：只需达到时间时启动一次目标应用即可
                val iterator = mutableSettings.iterator()
                while (iterator.hasNext()) {
                    val setTime = iterator.next()
                    if (currentTime == setTime.replace(":", "")) {
                        val isForeground = isForeground(this@AutoLaunchService)
//                      Log.w(tag, "工具当前是否在前台 ？ $isForeground")
                        if(!isForeground){
                            moveTaskToFront(this@AutoLaunchService)
                        }
                        launchAppForPackage(baseContext)
                        iterator.remove()
                    }
                }
            }
        }, 2000, 2000)
    }

}