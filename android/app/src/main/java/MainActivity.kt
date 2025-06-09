package com.seuprojeto

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MainActivity : Activity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        init {
            System.loadLibrary("caminho_gps") // nome da lib Rust compilada
        }

        external fun startRust()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startRust()
    }

    // Função chamada por Rust via JNI
    fun getLocation(): DoubleArray {
        var coords = doubleArrayOf(0.0, 0.0)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão se necessário
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        coords = doubleArrayOf(it.latitude, it.longitude)
                    }
                }
        }
        return coords
    }
}
