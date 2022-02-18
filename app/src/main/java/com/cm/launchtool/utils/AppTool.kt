package com.cm.launchtool.utils

import android.app.ActivityManager
import android.app.AppOpsManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import com.cm.launchtool.constants.getChooseApp
import java.lang.reflect.Field
import java.lang.reflect.Method


object AppTool {
    private val tag = "LaunchTool@${javaClass.simpleName}"


    /**
     * 是否亮屏
     */
    fun isScreenOn(context: Context) :Boolean{
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isScreenOn
    }


    /**
     * 通过包名启动activity
     */
    fun launchAppForPackage(context: Context, pkName: String = ""){
        try {
            var packageName = pkName
            if(packageName.isEmpty()){
                packageName = SPTool.getLaunchAppPackage()
            }

            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            intent?.apply {
                action = Intent.ACTION_SCREEN_ON
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
            Log.d(tag, "启动成功（${getChooseApp()}-${SPTool.getLaunchAppPackage()}）")
        } catch (e: Exception) {
            Log.e(
                tag,
                "启动失败（${getChooseApp()}-${SPTool.getLaunchAppPackage()}: ${e.printStackTrace()}"
            )
        }
    }

    /**
     * 检查悬浮窗权限
     * 用于解决android10及以上，系统不允许后台启动activity
     */
    fun checkFloatWindowPermission(context: Context):Boolean{
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //6.0
            try {
                var cls = Class.forName("android.content.Context")
                val declaredField: Field = cls.getDeclaredField("APP_OPS_SERVICE")
                declaredField.isAccessible = true
                var obj: Any? = declaredField.get(cls) as? String ?: return false
                val str2 = obj as String
                obj = cls.getMethod("getSystemService", String::class.java).invoke(context, str2)
                cls = Class.forName("android.app.AppOpsManager")
                val declaredField2: Field = cls.getDeclaredField("MODE_ALLOWED")
                declaredField2.isAccessible = true
                val checkOp: Method = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String::class.java)
                val result = checkOp.invoke(obj, 24, Binder.getCallingUid(), context.packageName) as Int
                result == declaredField2.getInt(cls)
            } catch (e: java.lang.Exception) {
                false
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //8
                val appOpsMgr = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode = appOpsMgr.checkOpNoThrow(
                    "android:system_alert_window",
                    Process.myUid(),
                    context.packageName
                )
                Settings.canDrawOverlays(context) || mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED
            } else {
                Settings.canDrawOverlays(context)
            }
        }
    }

    /**
     * 蓝牙检测
     */
    fun checkBluetooth(context: Context){
//        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()     //过时，使用下面的方式获取蓝牙适配器
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if(bluetoothAdapter == null){
            "当前设备不支持蓝牙".tip()
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(intent)
            bluetoothAdapter.enable()
            return
        }
//        "蓝牙已开启".tip()
    }

    /**
     * 判断服务是否在运行
     * @param context
     * @param serviceName
     * @return
     * 服务名称为全路径 例如com.ghost.WidgetUpdateService
     */
    fun isRunService(context: Context, serviceName: String): Boolean {
        Log.d("LaunchTool@", "serviceName: $serviceName")
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            Log.d("LaunchTool@", "runService: ${service.service.className}")
            if (serviceName == service.service.className) {
                return true
            }
        }
        return false
    }

    /**
     * 判断某个应用是否在运行
     */
    fun isRunning(context: Context, pkName: String):Boolean{
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.getRunningTasks(100)
        var isAppRunning = false
        //100表示取的最大的任务数，info.topActivity表示当前正在运行的Activity，info.baseActivity表系统后台有此进程在运行
        for (info in tasks) {
            if (info.topActivity!!.packageName == pkName || info.baseActivity!!.packageName == pkName) {
                isAppRunning = true
                break
            }
        }
        return isAppRunning
    }
    /**
     * 判断当前应用是否在前台
     */
    fun isForeground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (tasks != null && tasks.isNotEmpty()) {
            val topActivity = tasks[0].topActivity ?: return false
            if (topActivity.packageName == context.packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 将当前应用移动到前台
     */
    fun moveTaskToFront(context: Context) {
        val mAm = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        //获得当前运行的task
        val taskList = mAm.getRunningTasks(100)
        for (rti in taskList) {
            //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
            if (rti.topActivity?.packageName == context.packageName) {
                mAm.moveTaskToFront(rti.id, 0)
                return
            }
        }
    }
}