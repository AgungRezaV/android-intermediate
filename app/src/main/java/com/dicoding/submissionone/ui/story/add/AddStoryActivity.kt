package com.dicoding.submissionone.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.submissionone.utils.Result.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionone.R
import com.dicoding.submissionone.utils.*
import com.dicoding.submissionone.databinding.ActivityAddStoryBinding
import com.dicoding.submissionone.ui.main.MainActivity
import com.dicoding.submissionone.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var photoPath: String
    private var getFile: File? = null
    private var lat: Float = 0.0F
    private var lon: Float = 0.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"

        viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddStoryViewModel::class.java]

        if (!permissionCheck()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        val btnGallery: Button = binding.btnAddGaleri
        btnGallery.setOnClickListener(this)

        val btnKamera: Button = binding.btnAddKamera
        btnKamera.setOnClickListener(this)

        val btnUpload: Button = binding.btnUploadStory
        btnUpload.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAddKamera -> {
                startCamera()
            }
            R.id.btnAddGaleri -> {
                startGallery()
            }
            R.id.btnUploadStory -> {
                uploadStory()
            }
        }
    }

    private fun permissionCheck() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val imageUri: Uri = FileProvider.getUriForFile(this, "com.dicoding.submissionone", it)

            photoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            launchCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val pick = Intent.createChooser(intent, "Pilih Gambar")
        launchGallery.launch(pick)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!permissionCheck()) {
                Toast.makeText(this, "Authorize Camera", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private val launchCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val myFile = File(photoPath)
                getFile = myFile
                val result = BitmapFactory.decodeFile(getFile?.path)
                binding.ivAddImage.setImageBitmap(result)
            }
        }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivAddImage.setImageURI(selectedImg)
        }
    }

    private fun uploadStory() {
        if (getFile != null) {
            viewModel.getUser().observe(this) {
                val token = "Bearer ${it.token}"
                val file = reduceImageSize(getFile as File)
                val desc =
                    "${binding.etDeskripsi.text}".toRequestBody("text/plain".toMediaTypeOrNull())
                val reqImgFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo", file.name, reqImgFile
                )
                viewModel.getUser().observe(this) {
                    viewModel.addStory(token, imageMultipart, desc, lat, lon)
                        .observe(this@AddStoryActivity) {
                            when (it) {
                                is Success -> {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(this, "Upload Berhasil !", Toast.LENGTH_SHORT)
                                        .show()
                                    finish()
                                }
                                is Loading -> {
                                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                                }
                                is Error -> {
                                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                                    Toast.makeText(this, "Upload Gagal !", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                }
            }
        } else {
            Toast.makeText(this, "Gambar Masih Kosong !", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}