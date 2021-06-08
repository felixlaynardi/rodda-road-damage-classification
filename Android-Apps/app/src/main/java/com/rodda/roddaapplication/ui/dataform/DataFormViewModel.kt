package com.rodda.roddaapplication.ui.dataform

import android.util.Log
import androidx.lifecycle.ViewModel
import com.rodda.roddaapplication.model.network.ApiConfig
import com.rodda.roddaapplication.model.response.ReportResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataFormViewModel : ViewModel() {
    fun postReport (fullName : String, location : String, time : String, image : List<String>) {

        val responseBody = ReportResponse(
            image,fullName,location, time
        )

        val response = ApiConfig().getApiServices().postReport(responseBody)

        response.enqueue(object : Callback<ReportResponse> {
            override fun onResponse(
                call: Call<ReportResponse>,
                response: Response<ReportResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("json",response.body().toString())
                    Log.d("success","Berhasil dikirim")
                } else {
                    Log.d("onFailure",response.message())
                }
            }

            override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                Log.d("onFailure",t.message.toString())
            }

        })
    }
}