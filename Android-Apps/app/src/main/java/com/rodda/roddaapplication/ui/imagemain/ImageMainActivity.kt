package com.rodda.roddaapplication.ui.imagemain


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.rodda.roddaapplication.databinding.ImageMainActivityBinding
import com.rodda.roddaapplication.ui.imagedetail.ImageDetailActivity
import com.rodda.roddaapplication.ui.imagedetail.ImageDetailActivity.Companion.imageMainPath
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageMainActivity : AppCompatActivity() {

    private lateinit var imageMainBinding : ImageMainActivityBinding
    private lateinit var currentPhotoPath : String

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Glide.with(this)
                .load(currentPhotoPath)
                .into(imageMainBinding.imgMain)
            imageMainBinding.btnNextDetail.isEnabled = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageMainBinding = ImageMainActivityBinding.inflate(layoutInflater)
        setContentView(imageMainBinding.root)

        imageMainBinding.btnFotoMain.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { imageMain ->
                    val photoFile = try {
                        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",Locale("indonesia")).format(Date())
                        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        File.createTempFile(
                            "JPEG_${timeStamp}",
                            ".jpg",
                            storageDir
                        ).apply {
                            currentPhotoPath = absolutePath
                        }
                    } catch (e : IOException) {
                        Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
                    }
                photoFile?.also {
                    val photoURI : Uri = FileProvider.getUriForFile(
                        this,"com.rodda.roddaapplication", it as File
                    )
                    imageMain.putExtra(MediaStore.EXTRA_OUTPUT,photoURI)
                    startForResult.launch(imageMain)
                }
            }
        }

        imageMainBinding.btnNextDetail.setOnClickListener {
            val detailIntent = Intent(this,ImageDetailActivity::class.java)
            detailIntent.putExtra(imageMainPath,currentPhotoPath)
            startActivity(detailIntent)
        }
    }
}