package com.cm.launchtool.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cm.launchtool.R
import com.cm.launchtool.data.InstalledApp

/**
 * Spinner适配器
 * 注：如果使用 ArrayAdapter（BaseAdapter的子类），子布局只能是一个TextView。自定义布局请使用 BaseAdapter
 */
class SpinnerAdapter(
    private val context: Context,
    private var list: ArrayList<InstalledApp>
) :BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): InstalledApp {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val installedApp = getItem(position)
        val viewHolder:ViewHolder
        val view: View
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.appIcon = view.findViewById(R.id.appIcon)
            viewHolder.appName = view.findViewById(R.id.appName)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.apply {
            appIcon?.setImageDrawable(installedApp.icon)
            appName?.text = installedApp.name
        }
        return view
    }

    inner class ViewHolder {
        var appIcon: ImageView? = null
        var appName: TextView? = null
    }

}