package com.brand.dummychild;


import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundService extends Service {
    private GeoFenceHelper geoFenceHelper;
    GeofencingClient geofencingClient;
    private ExecutorService executorService;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        geofencingClient = LocationServices.getGeofencingClient(this);
        geoFenceHelper = new GeoFenceHelper(this);
        executorService = Executors.newSingleThreadExecutor();
        GeoFenceThread thread = new GeoFenceThread();
        executorService.submit(thread);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Return START_STICKY to indicate that the service should be restarted if killed by the system
        return START_STICKY;
    }


    class GeoFenceThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (PreferenceUtils.getStartGeoFence(BackgroundService.this)) {
                    PreferenceUtils.setStartGeoFence(BackgroundService.this, false);
                    LocationModel locationModel = PreferenceUtils.getLocation(BackgroundService.this);
                    LatLng latLng = new LatLng(Float.parseFloat(locationModel.getLatitude()), Float.parseFloat(locationModel.getLongitude()));
                    addGeofence(latLng, Float.parseFloat(locationModel.getRadius()));
                } else if (PreferenceUtils.getDeactivateGeofence(BackgroundService.this)) {
                    PreferenceUtils.setDeactivateGeofence(BackgroundService.this, false);
                    removeGeoFence();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addGeofence(LatLng latLng, float radius) {

        Geofence geofence = geoFenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geoFenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geoFenceHelper.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geoFenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }

    private void removeGeoFence() {
        PendingIntent pendingIntent = geoFenceHelper.getPendingIntent();
        geofencingClient.removeGeofences(pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences removed
                        Toast.makeText(BackgroundService.this, "GeoFence Removed", Toast.LENGTH_SHORT).show();
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to remove geofences
                        // ...
                    }
                });
    }


    private void performBackgroundWork() {
        // Put your background task logic here
    }

    public void onDestroy() {
        super.onDestroy();
        // Cleanup resources or release any acquired data here
    }
}
