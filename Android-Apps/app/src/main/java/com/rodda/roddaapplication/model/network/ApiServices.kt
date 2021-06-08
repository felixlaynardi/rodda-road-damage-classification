package com.rodda.roddaapplication.model.network

import com.rodda.roddaapplication.model.response.ReportResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("/predict")
    @Headers("Content-Type:application/json")
    fun postReport (@Body reportResponse: ReportResponse) : Call<ReportResponse>
}