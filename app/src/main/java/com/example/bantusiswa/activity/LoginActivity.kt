package com.example.bantusiswa.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bantusiswa.R
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.ceil

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        val TAG = LoginActivity.javaClass.simpleName
    }

    private lateinit var auth: FirebaseAuth
    private val PERMISSION_ID = 42
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latitude = 0.0
    var longitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissions()

        val tvGoToRegis: TextView = findViewById(R.id.tv_register)
        val btnGoToHome: Button = findViewById(R.id.btn_login_enter)
        tvGoToRegis.setOnClickListener(this)
        btnGoToHome.setOnClickListener(this)

        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            if (currentUser.isEmailVerified) {
//                val homeIntent = Intent(this@LoginActivity, MapsActivity::class.java)
//                startActivity(homeIntent)
//            }
//        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.tv_register -> {
                    val registerIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(registerIntent)
                }
                R.id.btn_login_enter -> {
                    auth.signInWithEmailAndPassword(
                            et_login_email.text.toString(),
                            et_login_password.text.toString()
                        )
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                Log.d(TAG, "signInWithEmail:success")
                                val user = auth.currentUser
                                if (user != null) {
                                    if (user.isEmailVerified) {
                                        val homeIntent =
                                            Intent(this@LoginActivity, MapsActivity::class.java)
                                            homeIntent.putExtra("extra_lat", latitude)
                                            homeIntent.putExtra("extra_long", longitude)
                                            Log.d(TAG, "loginlatitude" + latitude + "longitude" + longitude)
                                        startActivity(homeIntent)
                                    } else {
                                        Toast.makeText(
                                            baseContext,
                                            "Not Verified",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }else{
                                Toast.makeText(baseContext, "Authentication Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onResume() {
        super.onResume()
        if(!checkPermissions()){
            requestPermissions()
        }else{
            getLastLocation()
        }
    }

    private fun getLastLocation() {
//        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        latitude = location.latitude
                        longitude = location.longitude

                        Log.d(TAG, "loginlatitude" + latitude + "longitude" + longitude)

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
//        } else {
//            requestPermissions()
//        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
        }
    }
}
