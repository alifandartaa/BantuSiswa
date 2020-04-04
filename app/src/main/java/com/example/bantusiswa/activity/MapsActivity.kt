package com.example.bantusiswa.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bantusiswa.R
import com.google.android.gms.location.*

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private var TAG = "MapsActivity"
    private lateinit var mMap: GoogleMap
    var latitude = 0.0
    var longitude = 0.0

    private lateinit var mLocation : Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.



        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        val myLoc = LatLng(-7.8574295, 113.2331977)
        latitude = intent.getDoubleExtra("extra_lat", 0.0)
        longitude = intent.getDoubleExtra("extra_long", 0.0)
        val myLoc = LatLng(latitude, longitude)
        Log.d(TAG, "maplatitude" + latitude + "maplongitude" + longitude)
        mMap!!.addMarker(MarkerOptions().position(myLoc)
            .title("My Location Marker")
            .snippet("coba")
            .draggable(true))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(myLoc))
    }

}
