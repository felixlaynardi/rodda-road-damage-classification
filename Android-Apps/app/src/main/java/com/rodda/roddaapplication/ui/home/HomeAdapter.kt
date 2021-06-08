package com.rodda.roddaapplication.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.rodda.roddaapplication.R
import com.rodda.roddaapplication.databinding.ListReportBinding
import com.rodda.roddaapplication.model.ResultModel

class HomeAdapter(options: FirestoreRecyclerOptions<ResultModel>) :
    FirestoreRecyclerAdapter<ResultModel, HomeAdapter.ResultsViewHolder>(options) {
    private lateinit var onItemClickCallback : OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_report, parent, false)
        return ResultsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int, model: ResultModel) {
        holder.bind(model)
    }

    inner class ResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListReportBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(result: ResultModel) {
            Glide.with(itemView.context).load(result.images?.get(0)).into(binding.imgMainPhoto)
            binding.tvLocation.text = result.location
            binding.tvByName.text = "by ${result.fullName}"
            if (result.predictions?.contains("Crack") == true)
                binding.imgCracked.visibility = View.VISIBLE
            if (result.predictions?.contains("Pothole") == true && result.predictions?.contains("Crack") == false) {
                binding.imgCracked.setImageResource(R.mipmap.ic_hole)
                binding.imgCracked.visibility = View.VISIBLE
            }else if(result.predictions?.contains("Pothole") == true){
                binding.imgHole.visibility = View.VISIBLE
            }
            itemView.setOnClickListener{
                onItemClickCallback.onItemClicked(snapshots.getSnapshot(adapterPosition), adapterPosition)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback{
        fun onItemClicked(documentSnapshot: DocumentSnapshot, position: Int)
    }
}
