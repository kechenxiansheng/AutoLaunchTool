package com.cm.launchtool.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.SparseIntArray
import com.cm.launchtool.constants.getChooseApp
import com.cm.launchtool.data.InstalledApp
import rx.Observable
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


object InStalledAppTool {
    /**
     * 获取已安装的应用信息队列
     * @param context 上下文
     * @param type app类型  0:全部应用 1:联网应用
     */
    private fun getInStalledAppInfo(context: Context, type: Int = 1): ArrayList<InstalledApp> {
        val appList: ArrayList<InstalledApp> = ArrayList()
        val siArray = SparseIntArray()
        // 获得应用包管理器
        val packageManager: PackageManager = context.packageManager
        // 获取系统中已经安装的应用列表
        @SuppressLint("WrongConstant")
        val installList = packageManager.getInstalledApplications(
            PackageManager.PERMISSION_GRANTED
        )
        //遍历所有目录（indices）
        for (i in installList.indices) {
            val item = installList[i]
            // 去掉重复的应用信息
            if (siArray.indexOfKey(item.uid) >= 0) {
                continue
            }
            // 往siArray中添加一个应用编号，以便后续的去重校验
            siArray.put(item.uid, 1)
            try {
                // 获取该应用的权限列表
                val permissions = packageManager.getPackageInfo(
                    item.packageName,
                    PackageManager.GET_PERMISSIONS
                ).requestedPermissions ?: continue
                var isQueryNetwork = false
                for (permission in permissions) {
                    // 过滤那些具备上网权限的应用
                    if (permission == "android.permission.INTERNET") {
                        isQueryNetwork = true
                        break
                    }
                }
                // 类型为0表示所有应用，为1表示只要联网应用
                if (type == 0 || (type == 1 && isQueryNetwork)) {
                    val app = InstalledApp()
                    app.uid = item.uid.toString()                           // 获取应用的编号
                    app.name = item.loadLabel(packageManager).toString()    // 获取应用的名称
                    app.packageName = item.packageName                      // 获取应用的包名
                    app.icon = item.loadIcon(packageManager)                // 获取应用的图标
                    appList.add(app)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }
        }
        return appList // 返回 去重后的应用包队列
    }

    /**
     * 获取安装的所有应用信息集合
     */
    fun getInStalledAppInfoWithObservable(context: Context):Observable<ArrayList<InstalledApp>>{
        //被观察者
        return Observable.create { subscriber ->
            subscriber?.apply {
                onNext(getAppListWithSort(context))
                onCompleted()
            }
        }
    }

    /**
     * 将上次选中的app放在第一位显示
     * Collections.swap(list,元素当前位置索引，需要放置的位置索引)
     * */
    private fun getAppListWithSort(context:Context): ArrayList<InstalledApp> {
        val appList = getInStalledAppInfo(context,1)
        listSort(appList)
        var targetIndex = 0
        for(i in 0 until appList.size){
            if(appList[i].name == getChooseApp()){
                targetIndex = i
                break
            }
        }
        //归位：将当前选择的app放在第一位
        Collections.swap(appList,targetIndex,0)
        return appList
    }

    /**
     * 排序
     */
    private fun listSort(list: ArrayList<InstalledApp>){
        list.sortWith(compareBy{
            it.name
        })
        //java写法
//        Collections.sort(list,object :Comparator<InstalledApp>{
//            override fun compare(p0: InstalledApp, p1: InstalledApp): Int {
//                return p0.name.compareTo(p1.name)
//            }
//
//        })
    }
}