package com.example.gpstracker

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class LocationListener : LocationListener {
    external fun onLocationChangedNative(location: Location)

    override fun onLocationChanged(location: Location) {
        onLocationChangedNative(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    companion object {
        init {
            System.loadLibrary("slint_android_app")
        }
    }
}