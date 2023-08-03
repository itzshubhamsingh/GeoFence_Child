package com.brand.dummychild;
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class FCMService extends FirebaseMessagingService {
    public void onNewToken(String token) {
        // You can use this method to handle token refresh or save the FCM token to your server.
    }
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Check if the FCM message contains data payload
        try{
            JSONObject root = new JSONObject(remoteMessage.getData());
            String status = root.getString("status");
            if(status.equalsIgnoreCase("activate")){
                String latitude = root.getString("latitude");
                String longitude = root.getString("longitude");
                String radius = root.getString("radius");
                LocationModel location = new LocationModel(latitude, longitude, radius);
                PreferenceUtils.setLocation(location, this);
                PreferenceUtils.setStartGeoFence(this, true);
            }
            else if(status.equalsIgnoreCase("deactivate")){
                PreferenceUtils.setDeactivateGeofence(this, true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
