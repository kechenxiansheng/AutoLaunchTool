package com.cm.launchtool.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cm.launchtool.R

class ListViewAdapter(
    context: Context,
    private var list: ArrayList<String>,
    private val sourceId: Int,
    private val listener:DeleteListener,
)
    :ArrayAdapter<String>(context, sourceId)
{
    fun refreshData(list:ArrayList<String>){
        this.list.clear()
        this.list.addAll(list)
        this.list.sort()
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): String {
        return list[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val timeInfo = getItem(position)
        val viewHolder:ViewHolder
        val view: View
        if(convertView == null){
            view = LayoutInflater.from(context).inflate(sourceId, parent, false)
            viewHolder = ViewHolder()
            viewHolder.deleteButton = view.findViewById(R.id.delete_btn)
            viewHolder.setTime = view.findViewById(R.id.set_time)
            view.tag = viewHolder
        }else{
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.apply {
            if(timeInfo.isNotEmpty()){
                setTime?.text = timeInfo
                deleteButton?.setOnClickListener {
                    showDeleteDialog(timeInfo)
                }
            }

        }
        return view
    }

    /** 删除时弹窗提示，避免手误 */
    private fun showDeleteDialog(timeInfo:String){
        val dialog = AlertDialog.Builder(context)
            .setTitle("删除提示")
            .setMessage("是否确认删除此时间（$timeInfo）？")
            .setPositiveButton("确认") { dialog, swich ->
                dialog.dismiss()
                listener.delete(timeInfo)
                list.remove(timeInfo)
                notifyDataSetChanged()
            }
            .setNegativeButton("手滑"){ dialog,swich ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    inner class ViewHolder {
        var deleteButton: ImageView? = null
        var setTime: TextView? = null
    }

    interface DeleteListener{
        fun delete(choose:String)
    }
}