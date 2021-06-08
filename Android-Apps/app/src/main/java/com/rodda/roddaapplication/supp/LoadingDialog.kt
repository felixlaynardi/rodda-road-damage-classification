package com.rodda.roddaapplication.supp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.View
import com.rodda.roddaapplication.R
import com.rodda.roddaapplication.databinding.LoadingScreenBinding

class LoadingDialog(private val mActivity: Activity){

    private val loadingScreenBinding = LoadingScreenBinding.inflate(mActivity.layoutInflater)

    private val dialog = AlertDialog.Builder(mActivity)
        .setView(loadingScreenBinding.root)
        .create()


    fun startDialog(){
        dialog.setCancelable(false)
        loadingScreenBinding.btnClear.visibility = View.GONE
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun startReportDialog(text : String){
        loadingScreenBinding.pbLoading.visibility = View.VISIBLE
        loadingScreenBinding.imgState.visibility = View.INVISIBLE
        loadingScreenBinding.btnClear.visibility = View.GONE
        loadingScreenBinding.tvLoading.text = text
        dialog.setCancelable(false)
        dialog.show()
    }

    @SuppressLint("SetTextI18n")
    fun finishDialog(state : Boolean,text: String) {
        loadingScreenBinding.pbLoading.visibility = View.INVISIBLE
        loadingScreenBinding.imgState.visibility = View.VISIBLE
        if (state) {
            loadingScreenBinding.btnClear.visibility = View.VISIBLE
            loadingScreenBinding.imgState.setImageResource(R.drawable.ic_baseline_check_24)
            loadingScreenBinding.tvLoading.text = text
        } else {
            loadingScreenBinding.imgState.setImageResource(R.drawable.ic_baseline_close_24)
            loadingScreenBinding.tvLoading.text = text
        }
        loadingScreenBinding.btnClear.setOnClickListener {
            dialog.dismiss()
            mActivity.finish()
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
    //"Terjadi Kesalahan Saat Mengirim Laporan"
    //,"Terjadi Kesalahan Saat Mengupload Gambar"
    //,,"Mohon Isi Kolom Lokasi"
}