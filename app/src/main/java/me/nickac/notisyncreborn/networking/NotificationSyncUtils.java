package me.nickac.notisyncreborn.networking;

import android.graphics.Bitmap;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;

import java.lang.reflect.Modifier;

import me.nickac.notisyncreborn.model.RemoteDevice;
import me.nickac.notisyncreborn.model.RemoteNotification;
import me.nickac.notisyncreborn.storage.DeviceStorage;
import me.nickac.notisyncreborn.utils.BitmapTypeAdapter;
import me.nickac.notisyncreborn.utils.RetrofitUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static me.nickac.notisyncreborn.utils.RetrofitUtils.prepareBitmaps;

public class NotificationSyncUtils {

    public NotificationSyncUtils() {
    }

    private YaGsonBuilder getYaGsonBuilder() {
        return new YaGsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setTypeInfoPolicy(TypeInfoPolicy.DISABLED);
    }


    public void sendNotification(RemoteNotification notif) {
        BitmapTypeAdapter typeAdapter = new BitmapTypeAdapter();
        YaGson yaGson = getYaGsonBuilder()
                .registerTypeAdapter(Bitmap.class, typeAdapter)
                .create();
        String json = yaGson.toJson(notif);

        for (RemoteDevice device : DeviceStorage.getInstance().getDevices()) {
            Call<ResponseBody> call = device.getNotificationService()
                    .sendNotificationRaw(RetrofitUtils.createPartFromString(json),
                            prepareBitmaps(typeAdapter.getImages()));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //TODO: Handle this and warn the user
                }
            });
        }
    }

    public void removeNotification(int id, String appPackage) {

        for (RemoteDevice device : DeviceStorage.getInstance().getDevices()) {
            Call<ResponseBody> call = device.getNotificationService()
                    .removeNotificationRaw(id, appPackage);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }
}
