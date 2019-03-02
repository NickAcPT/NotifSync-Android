package me.nickac.notisyncreborn.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.gilecode.yagson.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import me.nickac.notisyncreborn.SyncApplication;
import me.nickac.notisyncreborn.model.RemoteDevice;

public class DeviceStorage {
    private static DeviceStorage instance;
    private final SharedPreferences preferences;
    private List<RemoteDevice> devices;

    private DeviceStorage() {
        preferences = SyncApplication.getContext()
                .getSharedPreferences("DeviceStorage", Context.MODE_PRIVATE);
        SharedPreferences prefs = preferences;
        if (prefs != null && prefs.contains("devices")) {
            Type listType = new TypeToken<ArrayList<RemoteDevice>>() {
            }.getType();

            devices = SyncApplication.getGson()
                    .fromJson(prefs.getString("devices", ""), listType);
        }
        devices = new ArrayList<>();
    }

    public void save() {
        preferences.edit().putString("devices", SyncApplication.getGson().toJson(devices)).apply();
    }


    public List<RemoteDevice> getDevices() {
        return devices;
    }

    public static DeviceStorage getInstance() {
        if (instance == null)
            instance = new DeviceStorage();
        return instance;
    }


}
