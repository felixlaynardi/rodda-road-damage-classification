package com.rodda.roddaapplication.model

import com.google.firebase.Timestamp

class ResultModel {
    var fullName: String? = null
    var location: String? = null
    var time: String? =  null
    var images: ArrayList<String>? = null
    var predictions: ArrayList<String>? = null
    var predictions_acc: ArrayList<Double>? = null

    constructor()
}