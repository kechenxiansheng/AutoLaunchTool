package com.cm.launchtool.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.cm.launchtool.R
import com.cm.launchtool.adapter.ListViewAdapter
import com.cm.launchtool.adapter.SpinnerAdapter
import com.cm.launchtool.constants.*
import com.cm.launchtool.data.InstalledApp
import com.cm.launchtool.data.TargetApp
import com.cm.launchtool.utils.FunctionTool.arrayStrTrimFormat
import com.cm.launchtool.utils.InStalledAppTool
import com.cm.launchtool.utils.SPTool
import com.cm.launchtool.utils.tip
import kotlinx.android.synthetic.main.activity_setting.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 设置页面
 */
class SettingActivity :AppCompatActivity(){
    private val tag = "LaunchTool@${javaClass.simpleName}"
    private lateinit var adapter:ListViewAdapter
    private lateinit var list:ArrayList<String>
    private var chooseApp = getChooseApp()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        //禁止息屏
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting)
        progress_bar.visibility = View.VISIBLE
        //设置下拉列表
        val observable = InStalledAppTool.getInStalledAppInfoWithObservable(this)
        //观察者观察数据
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Subscriber<ArrayList<InstalledApp>>() {
                override fun onCompleted() {
                    progress_bar.visibility = View.GONE
                    app_spinner.visibility = View.VISIBLE
                    initView(getChooseApp())
                }

                override fun onError(e: Throwable) {
                }

                override fun onNext(appList: ArrayList<InstalledApp>) {
                    initSpinner(appList)
                }
            })
    }
    /** 初始化spinner */
    private fun initSpinner(appList:ArrayList<InstalledApp>){
        val adapter = SpinnerAdapter(this,appList)
        app_spinner.adapter = adapter
        app_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                //选择的app，选择了新的app才去加载listview数据
                val installedApp = app_spinner.getItemAtPosition(position) as InstalledApp
                val app = installedApp.name
                if(app != getChooseApp()){
                    chooseApp = app
                    saveChooseApp(app,installedApp.packageName)
                    initView(app)
                }
                Log.d(tag, "选择的app: ${getChooseApp()}")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /** 保存新增的启动时间 */
    fun saveSetting(time :String){
        val appSettings = SPTool.get(chooseApp)
        val targetApp = if(appSettings.isEmpty()){
            val launchTimes = ArrayList<String>()
            launchTimes.add(time)
            TargetApp(chooseApp,launchTimes)
        }else{
            //有数据，则转换为可编辑list进行操作
            val splitList = arrayStrTrimFormat(appSettings).split(",")
            val launchTimes = ArrayList<String>()
            launchTimes.addAll(splitList)
            if(launchTimes.contains(time)){
                "请勿重复添加".tip()
                return
            }
            launchTimes.add(time)
            TargetApp(chooseApp,launchTimes)
        }
        SPTool.save(targetApp)
        "添加成功".tip()
        refreshAdApter()
    }

    /** 显示当前app的启动时间 */
    private fun initView(app:String){
        //获取时间添加结果
        add_time_btn.setOnClickListener {
            TimeDialog(this, object : TimeDialog.SettingResultListener {
                override fun result(result: String) {
                    Log.d(tag, "添加设置 : $result")
                    saveSetting(result)
                    refreshAdApter()
                }
            }).show()
        }

        list = getTimeSettingsWithApp(app)
        adapter = ListViewAdapter(this,
            list,
            R.layout.item_listview_layout,
            object : ListViewAdapter.DeleteListener {
                override fun delete(choose: String) {
                    Log.d(tag, "删除: $choose")
                    list.remove(choose)
                    SPTool.save(TargetApp(chooseApp,list))
                }
            })
        show_time.adapter = adapter
    }


    private fun refreshAdApter(){
        list.clear()
        list = getTimeSettingsWithApp(chooseApp)
        adapter.refreshData(list)
    }
}