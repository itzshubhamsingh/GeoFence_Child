package com.brand.dummychild;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PreferenceUtils {
    public static void setLocation(LocationModel locationModel, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefeditor = preferences.edit();
        Gson gson = new Gson();
        String jsonObject = gson.toJson(locationModel);
        prefeditor.putString("location", jsonObject);
        prefeditor.apply();
    }

    public static LocationModel getLocation(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("location", null);
        LocationModel getlocationModel =gson.fromJson(json, new TypeToken<LocationModel>(){}.getType());
        return getlocationModel;
    }
    public static void setStartGeoFence(Context context, boolean res){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefeditor = preferences.edit();
        prefeditor.putBoolean("startGeoFence", res);
        prefeditor.apply();
    }
    public static boolean getStartGeoFence(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return preferences.getBoolean("startGeoFence", false);
    }
    public static void setIsGeoFenceRunning(Context context, boolean res){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefeditor = preferences.edit();
        prefeditor.putBoolean("geofenceRunning", res);
        prefeditor.apply();
    }
    public static boolean getIsGeoFenceRunning(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return preferences.getBoolean("geofenceRunning", false);
    }
    public static void setDeactivateGeofence(Context context, boolean res){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefeditor = preferences.edit();
        prefeditor.putBoolean("deactivateGeofence", res);
        prefeditor.apply();
    }
    public static boolean getDeactivateGeofence(Context context){
        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        return preferences.getBoolean("deactivateGeofence", false);
    }
}
