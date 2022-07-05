package com.catfast.safetool.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.catfast.safetool.R
import com.catfast.safetool.basic.exts.getConnImg
import com.catfast.safetool.basic.exts.getConnName
import com.catfast.safetool.bean.ConnItem

class ServersAdapter(private val context: Context, private val onItemClick: (position: Int) -> Unit = {}) :
    RecyclerView.Adapter<ServersViewHolder>() {

    private val data: MutableList<ConnItem> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setListData(list: MutableList<ConnItem>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServersViewHolder {
        return ServersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_server, parent, false))
    }

    override fun onBindViewHolder(holder: ServersViewHolder, position: Int) {
        val connItem = data[position]
        holder.itemImgConn.setImageResource(getConnImg(connItem.name))
        holder.itemConnName.text = getConnName(connItem.name, connItem.city)
        if (connItem.selected) {
            holder.itemBg.setBackgroundResource(R.drawable.shape_ffe6d0_r12_s2)
            holder.itemChecked.visibility = View.VISIBLE
        } else {
            holder.itemBg.setBackgroundResource(R.drawable.shape_ffc491_r12)
            holder.itemChecked.visibility = View.GONE
        }
        holder.itemView.setOnClickListener { onItemClick.invoke(position) }
    }

    override fun getItemCount(): Int = data.size
}

class ServersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val itemBg: ConstraintLayout = itemView.findViewById(R.id.itemBg)
    val itemImgConn: AppCompatImageView = itemView.findViewById(R.id.itemImgConn)
    val itemConnName: AppCompatTextView = itemView.findViewById(R.id.itemConnName)
    val itemChecked: AppCompatImageView = itemView.findViewById(R.id.itemChecked)
}
