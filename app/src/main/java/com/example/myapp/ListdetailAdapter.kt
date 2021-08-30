package com.example.myapp

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.incredient_item.view.*

class ListdetailAdapter(var items:MutableList<List_IngreItem>) : RecyclerView.Adapter<ListdetailAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListdetailAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.incredient_item,parent,false)
        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
             holder.setItem(item)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun setItem(item: List_IngreItem) {
            itemView.iname.text = item.name
            itemView.start.text = item.low.toString()
            itemView.middle.text = item.value.toString()
            itemView.end.text = item.high.toString()
itemView.sb_storemainoptiondistance_distance.setProgress(item.detection!!.toInt(),false)
            itemView.sb_storemainoptiondistance_distance.max=item.high.toInt()
        }
    }









}