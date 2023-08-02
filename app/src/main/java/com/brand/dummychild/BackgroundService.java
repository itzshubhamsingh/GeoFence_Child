package com.brand.dummychild;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundService extends Service {
    private ExecutorService executorService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newSingleThreadExecutor();
        GeofenceThread thread = new GeofenceThread();
        executorService.submit(thread);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Perform the background work here
        performBackgroundWork();
        // Return START_STICKY to indicate that the service should be restarted if killed by the system
        return START_STICKY;
    }

    private void performBackgroundWork() {
        // Put your background task logic here
    }

    class GeofenceThread implements Runnable{
        public GeofenceThread(){}

        @Override
        public void run() {

        }
    }

    public void onDestroy() {
        super.onDestroy();
        // Cleanup resources or release any acquired data here
    }
}
