package com.alpesh1.imageanddatafirebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlin.io.encoding.Base64

class ItemAdapter(private val itmlist:ArrayList<ItemDS>):RecyclerView.Adapter<ItemAdapter.ItemHolder>() {

    lateinit var mListener: onItemClickListener

    interface onItemClickListener : OnItemClickListener {

        fun onItemClick(position: Int)

    }

    fun setOnClickListener(listener: onItemClickListener){

        mListener = listener

    }

    class ItemHolder(itemView: View,listener: onItemClickListener) : ViewHolder(itemView){

        val itmName : TextView = itemView.findViewById(R.id.txtName)
        val itmRate : TextView = itemView.findViewById(R.id.txtRate)
        val itmUnit : TextView = itemView.findViewById(R.id.txtUnit)
        val itmImg : ImageView = itemView.findViewById(R.id.cardImg)

        init {

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.itemdata_list,parent,false)
        return ItemHolder(itemView,mListener)

    }

    override fun getItemCount(): Int {

        return itmlist.size

    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val currentItem = itmlist[position]

        holder.itmName.text = currentItem.itemName
        holder.itmRate.text = currentItem.itemRate
        holder.itmUnit.text = currentItem.itemUnit

        val bytes = android.util.Base64.decode(currentItem.itemImage,
            android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.itmImg.setImageBitmap(bitmap)

    }

}