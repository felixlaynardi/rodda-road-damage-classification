package com.rodda.roddaapplication.ui.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.rodda.roddaapplication.databinding.ActivityDetailBinding
import com.rodda.roddaapplication.model.ResultModel
import com.rodda.roddaapplication.supp.LoadingDialog

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var docID: String
    private lateinit var loadingDialog: LoadingDialog

    companion object{
        const val DOC_ID = "doc_id"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        firestore = FirebaseFirestore.getInstance()

        loadingDialog = LoadingDialog(this)

        loadingDialog.startDialog()


        docID = intent.getStringExtra(DOC_ID).toString()
        Log.d("Doc ID", "Doc Id nya = $docID")

        val documentReference = firestore.collection("results").document(docID)
        documentReference.get()
            .addOnCompleteListener{task ->
                if(task.isSuccessful){
                    val document: DocumentSnapshot = task.result
                    if(document.exists()){
                        val images = document.toObject(ResultModel::class.java)?.images
                        val prediction = document.toObject(ResultModel::class.java)?.predictions
                        val predictionAcc = document.toObject(ResultModel::class.java)?.predictions_acc
                        val location = document.toObject(ResultModel::class.java)?.location
                        Glide.with(this).load(images?.get(1)).into(binding.imgDetails1)
                        binding.tvPredictions1.text = prediction?.get(0)
                        binding.tvPredictionsAcc1.text = "Accuracy ${predictionAcc?.get(0).toString()}"
                        binding.tvLocation.text = location
                        if(images?.size == 3){
                            imageSize3Load(images, prediction, predictionAcc, location)
                        }
                        if(images?.size == 4){
                            imageSize4Load(images, prediction, predictionAcc, location)
                        }
                        if(images?.size == 5){
                            imageSize5Load(images, prediction, predictionAcc, location)
                        }
                        if(images?.size == 6){
                            imageSize6Load(images, prediction, predictionAcc, location)
                        }
                        loadingDialog.dismissDialog()
                    }
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun imageSize3Load(images: ArrayList<String>?, predictions: ArrayList<String>?, predictionsAcc: ArrayList<Double>?, location: String?){
        Glide.with(this).load(images?.get(2)).into(binding.imgDetails2)
        with(binding) {
            tvPredictions2.text = predictions?.get(1)
            tvPredictionsAcc2.text = "Accuracy ${predictionsAcc?.get(1).toString()}"
            imgDetails3.visibility = View.GONE
            imgDetails4.visibility = View.GONE
            imgDetails5.visibility = View.GONE
            tvPredictions3.visibility = View.GONE
            tvPredictions4.visibility = View.GONE
            tvPredictions5.visibility = View.GONE
            tvPredictionsAcc3.visibility = View.GONE
            tvPredictionsAcc4.visibility = View.GONE
            tvPredictionsAcc5.visibility = View.GONE
            tvLocation.text = location
        }
    }

    @SuppressLint("SetTextI18n")
    private fun imageSize4Load(images: ArrayList<String>?, predictions: ArrayList<String>?, predictionsAcc: ArrayList<Double>?, location: String?){
        Glide.with(this).load(images?.get(3)).into(binding.imgDetails3)
        Glide.with(this).load(images?.get(2)).into(binding.imgDetails2)
        with(binding) {
            tvPredictions2.text = predictions?.get(1)
            tvPredictions3.text = predictions?.get(2)
            tvPredictionsAcc2.text = "Accuracy ${predictionsAcc?.get(1).toString()}"
            tvPredictionsAcc3.text = "Accuracy ${predictionsAcc?.get(2).toString()}"
            imgDetails4.visibility = View.GONE
            imgDetails5.visibility = View.GONE
            tvPredictions4.visibility = View.GONE
            tvPredictions5.visibility = View.GONE
            tvPredictionsAcc4.visibility = View.GONE
            tvPredictionsAcc5.visibility = View.GONE
            tvLocation.text = location
        }
    }

    @SuppressLint("SetTextI18n")
    private fun imageSize5Load(images: ArrayList<String>?, predictions: ArrayList<String>?, predictionsAcc: ArrayList<Double>?, location: String?){
        Glide.with(this).load(images?.get(4)).into(binding.imgDetails4)
        Glide.with(this).load(images?.get(3)).into(binding.imgDetails3)
        Glide.with(this).load(images?.get(2)).into(binding.imgDetails2)
        with(binding) {
            tvPredictions2.text = predictions?.get(1)
            tvPredictions3.text = predictions?.get(2)
            tvPredictions4.text = predictions?.get(3)
            tvPredictionsAcc2.text = "Accuracy ${predictionsAcc?.get(1).toString()}"
            tvPredictionsAcc3.text = "Accuracy ${predictionsAcc?.get(2).toString()}"
            tvPredictionsAcc4.text = "Accuracy ${predictionsAcc?.get(3).toString()}"
            imgDetails5.visibility = View.GONE
            tvPredictions5.visibility = View.GONE
            tvPredictionsAcc5.visibility = View.GONE
            tvLocation.text = location
        }
    }

    @SuppressLint("SetTextI18n")
    private fun imageSize6Load(images: ArrayList<String>?, predictions: ArrayList<String>?, predictionsAcc: ArrayList<Double>?, location: String?){
        Glide.with(this).load(images?.get(5)).into(binding.imgDetails5)
        Glide.with(this).load(images?.get(4)).into(binding.imgDetails4)
        Glide.with(this).load(images?.get(3)).into(binding.imgDetails3)
        Glide.with(this).load(images?.get(2)).into(binding.imgDetails2)
        with(binding) {
            tvPredictions2.text = predictions?.get(1)
            tvPredictions3.text = predictions?.get(2)
            tvPredictions4.text = predictions?.get(3)
            tvPredictions5.text = predictions?.get(4)
            tvPredictionsAcc2.text = "Accuracy ${predictionsAcc?.get(1).toString()}"
            tvPredictionsAcc3.text = "Accuracy ${predictionsAcc?.get(2).toString()}"
            tvPredictionsAcc4.text = "Accuracy ${predictionsAcc?.get(3).toString()}"
            tvPredictionsAcc5.text = "Accuracy ${predictionsAcc?.get(4).toString()}"
            tvLocation.text = location
        }
    }
}