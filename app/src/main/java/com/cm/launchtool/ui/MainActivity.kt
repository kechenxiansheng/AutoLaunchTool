package com.cm.launchtool.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cm.launchtool.R
import com.cm.launchtool.service.AutoLaunchService
import com.cm.launchtool.utils.AppTool
import com.cm.launchtool.utils.AppTool.checkBluetooth
import com.cm.launchtool.utils.AppTool.isRunService
import com.cm.launchtool.utils.tip
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val tag = "LaunchTool@${javaClass.simpleName}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //禁止息屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main)
        startService()
        setting_btn.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }
        checkFloatTip()
        //检查蓝牙
//        checkBluetooth(this)
    }

    private fun startService(){
        val serviceIsRun = isRunService(this,"$packageName.service.AutoLaunchService")
        Log.d(tag, "serviceIsRun : $serviceIsRun")
        if(!serviceIsRun){
            val intent = Intent(this, AutoLaunchService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent);
            } else {
                startService(intent);
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        stopService(Intent(this, AutoLaunchService::class.java))
    }


    private fun checkFloatTip(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = AppTool.checkFloatWindowPermission(this)
            Log.d(tag, "悬浮窗权限结果: $permission")
            if (!permission) {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("悬浮窗权限提醒")
                    .setMessage("工具切至后台时，需打开悬浮窗权限，才能启动目标应用！是否跳转至设置页手动开启？")
                    .setPositiveButton("确认") { dialog, swich ->
                        dialog.dismiss()
                        note_view.setTextColor(resources.getColor(R.color.gray4))
                        //跳转至设置页
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        startActivityForResult(intent,1)
                    }
                    .setNegativeButton("拒绝"){ dialog,swich ->
                        dialog.dismiss()
                        note_view.setTextColor(resources.getColor(R.color.red_gray))
                    }
                    .create()
                dialog.show()
            }else{
                Log.d(tag, "已授权悬浮窗权限 ")
                //检查蓝牙
                checkBluetooth(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(tag, "onActivityResult: requestCode=$requestCode,resultCode=$resultCode,data is null ? ${data==null}")
        //等悬浮窗权限打开后在申请蓝牙
        if(requestCode == 1){
            //检查蓝牙
            checkBluetooth(this)
        }
    }
}