package com.example.thr.starwarsencyclopedia.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.thr.starwarsencyclopedia.R
import com.example.thr.starwarsencyclopedia.common.Utils
import com.example.thr.starwarsencyclopedia.mvp.models.gson.ItemBaseDetails
import java.text.SimpleDateFormat

class CardsAdapter(private var onBottomReachedListener: OnBottomReachedListener,
                   private var onItemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    private var dataList: MutableList<ItemBaseDetails>

    init {
        dataList = mutableListOf()
    }

    fun setData(data: ArrayList<ItemBaseDetails>) {
        dataList = data
        notifyDataSetChanged()
    }

    fun addMoreData(moreData: List<ItemBaseDetails>) {
        dataList.addAll(moreData)
        notifyDataSetChanged()
    }

    fun clear(){
        dataList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.data_card, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == dataList.size - 1) {
            onBottomReachedListener.onBottomReached()
        }

        val swData = dataList[position]
        val id = Utils.idFromUrl(swData.url)
        val formatter = SimpleDateFormat("dd MMM yyyy 'at' HH:mm")

        holder.cardName.text = swData.name
        holder.cardDescription.text = formatter.format(swData.created)
        holder.cardId.text = "#$id"
        holder.setOnItemClickListener(swData, onItemClickListener)
    }

    override fun getItemCount() = dataList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardName: TextView = itemView.findViewById(R.id.nameTextView)
        var cardDescription: TextView = itemView.findViewById(R.id.descriptionTextView)
        var cardId: TextView = itemView.findViewById(R.id.idTextView)

        fun setOnItemClickListener(item: ItemBaseDetails, listener: OnItemClickListener) {
            itemView.setOnClickListener{ listener.onItemClick(item)}
        }
    }

    interface OnBottomReachedListener {
        fun onBottomReached()
    }

    interface OnItemClickListener {
        fun onItemClick(item: ItemBaseDetails)
    }
}
