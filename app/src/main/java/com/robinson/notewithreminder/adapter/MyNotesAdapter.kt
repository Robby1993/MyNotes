package com.robinson.notewithreminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.robinson.notewithreminder.R
import com.robinson.notewithreminder.model.NoteItemModel

class MyNotesAdapter(private val context: Context, private val itemList: List<NoteItemModel>) :
    RecyclerView.Adapter<MyNotesAdapter.ViewHolder>() {
    private var itemClickListener: MyItemClickListener? = null
    fun setItemClickListener(listener: MyItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_ticket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  NoteItemModel item = itemList.get(position);
        val s = itemList[position]
        val rem_DateTime = s.time + " " + s.date
        if (s.time.equals("notset", ignoreCase = true)) {
            holder.txt_datetime_rem.visibility = View.GONE
        } else {
            holder.txt_datetime_rem.visibility = View.VISIBLE
            holder.txt_datetime_rem.text = rem_DateTime
        }
        holder.txt_title.text = s.title
        holder.txt_title.isSelected = true
        holder.txt_desc.text = s.description
        holder.itemView.setOnClickListener { v: View? ->
            if (itemClickListener != null) {
                itemClickListener!!.onItemClick(s)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_datetime_rem: TextView
        var txt_title: TextView
        var txt_desc: TextView

        init {
            txt_title = itemView.findViewById<View>(R.id.title_tv2) as TextView
            txt_desc = itemView.findViewById<View>(R.id.desc_tv2) as TextView
            txt_datetime_rem = itemView.findViewById<View>(R.id.date_time_id_rem) as TextView
        }
    }

    interface MyItemClickListener {
        fun onItemClick(data: NoteItemModel?)
    }
}