package com.example.gpstracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSListener implements LocationListener {
    public native void onLocationChangedNative(Location location);

    @Override
    public void onLocationChanged(Location location) {
        onLocationChangedNative(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}