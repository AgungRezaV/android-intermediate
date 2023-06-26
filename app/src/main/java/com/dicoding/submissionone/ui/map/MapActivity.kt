package com.dicoding.submissionone.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submissionone.R
import com.dicoding.submissionone.databinding.ActivityMapBinding
import com.dicoding.submissionone.ui.story.ListStory
import com.dicoding.submissionone.ui.story.add.AddStoryViewModel
import com.dicoding.submissionone.utils.ViewModelFactory
import com.dicoding.submissionone.utils.Result
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var viewModel: MapViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    private lateinit var userVieModel: AddStoryViewModel
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModelFactory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapViewModel::class.java]
        userVieModel = ViewModelProvider(this, viewModelFactory)[AddStoryViewModel::class.java]

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setDatamaps()
    }

    private fun setDatamaps() {
        userVieModel.getUser().observe(this) { it ->
            val token = "Bearer ${it.token}"
            viewModel.getMapLocation(token).observe(this) {
                when (it) {
                    is Result.Loading -> {}
                    is Result.Success -> showMarker(it.data.listStory)
                    is Result.Error -> Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showMarker(listStory: List<ListStory>) {
        for (story in listStory) {
            val latlng = LatLng(story.lat, story.lon)
            googleMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .snippet(
                        story.description + " / " + story.createdAt
                            .removeRange(16, story.createdAt.length)
                    )
                    .title(story.name)
            )
        }
    }

    override fun onMapReady(context: GoogleMap) {
        googleMap = context
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isIndoorLevelPickerEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        getLocation()
        setMapStyle()
    }

    private fun setMapStyle() {
        try {
            val success =
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }


    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            reqPermissionLoc.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val reqPermissionLoc =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getLocation()
            }
        }

    companion object {
        private const val TAG = "StoryMapsActivity"
    }
}