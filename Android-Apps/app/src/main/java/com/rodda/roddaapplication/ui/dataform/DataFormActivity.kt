package com.rodda.roddaapplication.ui.dataform

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rodda.roddaapplication.MainActivity
import com.rodda.roddaapplication.R
import com.rodda.roddaapplication.databinding.ActivityDataFormBinding
import com.rodda.roddaapplication.supp.LoadingDialog
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DataFormActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val IMAGE_MAIN = "image_main"
        const val IMAGE_DETAIL = "image_detail"

        private const val PERMISSION_ID = 10
    }

    private lateinit var dataViewModel : DataFormViewModel
    private lateinit var activityDataFormBinding: ActivityDataFormBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var imageMain : String? = ""
    private val imageDetail : ArrayList<String> = ArrayList()
    private var firebaseAuth: FirebaseAuth? = null
    private var storage : FirebaseStorage? = null
    private var storageReference : StorageReference? = null
    private var firestore : FirebaseFirestore? = null
    private val uploadUrl = ArrayList<String>()
    private var fullName : String? = ""
    private var time = SimpleDateFormat("dd:MM:yyyy_HH:mm:ss",Locale("Indonesia")).format(Date())
    private var location = ""

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation : Location = p0.lastLocation
            activityDataFormBinding.etLokasi.setText(getAddressLine(lastLocation.longitude,lastLocation.latitude))
            location = getAddressLine(lastLocation.longitude,lastLocation.latitude)
        }
    }

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDataFormBinding = ActivityDataFormBinding.inflate(layoutInflater)
        setContentView(activityDataFormBinding.root)

        loadingDialog = LoadingDialog(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        dataViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(DataFormViewModel::class.java)

        activityDataFormBinding.btnLocation.setOnClickListener (this)

        firebaseAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storageReference = storage!!.reference

        val id = firebaseAuth?.currentUser?.uid

        val documentReference = firestore?.collection("users")?.document(id.toString())
        documentReference?.get()?.addOnSuccessListener {
            fullName = it.getString("fullName")
        }

        imageMain = intent.getStringExtra(IMAGE_MAIN)
        imageDetail.addAll(intent.getStringArrayListExtra(IMAGE_DETAIL)!!)

        activityDataFormBinding.btnKirim.setOnClickListener(this)
    }

    private fun checkPermission () : Boolean {
        return ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission () {
        ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
        PERMISSION_ID)
    }

    private fun isLocationEnable () : Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getCurrentLocation () {
        if (checkPermission()) {
            if (isLocationEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task ->
                    val location : Location? = task.result
                    if (location == null) {
                        val requestLocation = newLocationData()
                        fusedLocationProviderClient.requestLocationUpdates(requestLocation,locationCallback,
                            Looper.myLooper()!!)
                    } else {
                        activityDataFormBinding.etLokasi.setText(getAddressLine(location.longitude,location.latitude))
                        this.location = getAddressLine(location.longitude,location.latitude)
                    }
                }
            } else {
                Toast.makeText(this,"Please Turn On Your GPS",Toast.LENGTH_SHORT).show()
            }
        }
        else {
            requestPermission()
        }
    }

    private fun newLocationData(): LocationRequest {
        return LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
    }

    private fun getAddressLine(longitude: Double, latitude: Double): String {
        Log.d("Longtitude",longitude.toString())
        Log.d("Latitude",latitude.toString())
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(latitude, longitude, 1)

        activityDataFormBinding.pbLocation.visibility = View.GONE
        return address[0].getAddressLine(0)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
             R.id.btn_kirim-> {
                 if (imageMain != null && imageDetail.isNotEmpty() && location.isNotEmpty()){
                    loadingDialog.startReportDialog("Uploading Pictures...")
                    val mainRef = storageReference!!.child("images/$fullName/$imageMain")
                    mainRef.putFile(Uri.fromFile(File(imageMain!!))).addOnSuccessListener {
                        mainRef.downloadUrl.addOnCompleteListener {
                            uploadUrl.add(it.result.toString())
                        }
                    }
                    for (i in imageDetail) {
                        val imageRef = storageReference!!.child("images/$fullName/$i")
                        imageRef.putFile(Uri.fromFile(File(i))).addOnSuccessListener {
                            imageRef.downloadUrl.addOnCompleteListener {
                                uploadUrl.add(it.result.toString())
                                if (uploadUrl.size == imageDetail.size+1) {
                                    dataViewModel.postReport(fullName!!,location,time,uploadUrl)
                                    loadingDialog.finishDialog(true,"Report Successfully Send")
                                }
                            }.addOnCanceledListener {
                                loadingDialog.finishDialog(false,"Failed to Send Report, Try Again Later")
                            }
                        }.addOnFailureListener{
                            loadingDialog.finishDialog(false,"Failed to Upload Pictures, Try Again Later")
                            Log.d("Gagal upload",it.message!!)
                        }
                    }
                }
                 else if (location.isEmpty()){
                         activityDataFormBinding.etLokasi.error = "This Section Cannot be Blank"
                     }

             }
            R.id.btn_location-> {
                activityDataFormBinding.pbLocation.visibility = View.VISIBLE
                try {
                    requestPermission()
                    getCurrentLocation()
                } catch (e : Exception) {
                    e.printStackTrace()
                    activityDataFormBinding.pbLocation.visibility = View.GONE
                }
            }
        }
    }

    override fun finish() {
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        super.finish()
    }

}