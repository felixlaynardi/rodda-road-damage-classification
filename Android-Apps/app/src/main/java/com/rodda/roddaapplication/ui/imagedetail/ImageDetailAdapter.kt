package com.rodda.roddaapplication.ui.imagedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rodda.roddaapplication.databinding.ItemImageDetailBinding

class ImageDetailAdapter : RecyclerView.Adapter<ImageDetailAdapter.DetailViewHolder>() {

    private val imageList = ArrayList<String>()

    fun addToList (image : String) {
        imageList.add(image)
    }

    class DetailViewHolder(private val binding: ItemImageDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (image: String) {
            Glide.with(itemView.context)
                .load(image)
                .into(binding.itemImageDetail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemImageDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int = imageList.size
}