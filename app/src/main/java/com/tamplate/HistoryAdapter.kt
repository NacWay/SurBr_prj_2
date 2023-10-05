package com.tamplate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class HistoryAdapter(
    var listImage: MutableList<Int>,
    val listWins: MutableList<String>,
    val listIcon1: MutableList<String>,
    val listIcon2: MutableList<String>,
    val listIcon3: MutableList<String>
)
    : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iconRes)
        val imageIcon1: ImageView = itemView.findViewById(R.id.icon1)
        val imageIcon2: ImageView = itemView.findViewById(R.id.icon2)
        val imageIcon3: ImageView = itemView.findViewById(R.id.icon3)
    }

    var onItemPosition: ((Int) -> (Unit))? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_layou, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        onItemPosition?.invoke(position)

            if (!listWins.isEmpty()) {
                holder.image.setImageResource(listWins[position].toInt())
                holder.imageIcon1.setImageResource(listImage[listIcon1[position].toInt()])
                holder.imageIcon2.setImageResource(listImage[listIcon2[position].toInt()])
                holder.imageIcon3.setImageResource(listImage[listIcon3[position].toInt()])
            }
    }

    override fun getItemCount()= listWins.size
}