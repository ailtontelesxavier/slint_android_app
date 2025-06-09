package com.example.slintandroidapp

import android.app.Activity
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class MainActivity : Activity() {
    private var lastLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        lastLocation = location
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @Suppress("unused") // chamada por JNI
    fun getLocation(): DoubleArray {
        return if (lastLocation != null) {
            doubleArrayOf(lastLocation!!.latitude, lastLocation!!.longitude)
        } else {
            doubleArrayOf(0.0, 0.0)
        }
    }
}
