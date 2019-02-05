package me.nickac.notisyncreborn.networking;

import android.graphics.Bitmap;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;

import java.lang.reflect.Modifier;

import me.nickac.notisyncreborn.model.RemoteNotification;
import me.nickac.notisyncreborn.utils.RetrofitUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static me.nickac.notisyncreborn.utils.RetrofitUtils.prepareBitmaps;

public class NotificationSyncUtils {
    private final RestNotificationService restNotificationService;

    public NotificationSyncUtils() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:11785")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        restNotificationService = retrofit.create(RestNotificationService.class);
    }

    private YaGsonBuilder getYaGsonBuilder() {
        return new YaGsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .setTypeInfoPolicy(TypeInfoPolicy.DISABLED);
    }


    public void sendNotification(RemoteNotification notif) {
        me.nickac.notifsync.utils.BitmapTypeAdapter typeAdapter = new me.nickac.notifsync.utils.BitmapTypeAdapter();
        YaGson yaGson = getYaGsonBuilder()
                .registerTypeAdapter(Bitmap.class, typeAdapter)
                .create();

        String json = yaGson.toJson(notif);


        Call<ResponseBody> call = restNotificationService
                .sendNotificationRaw(RetrofitUtils.createPartFromString(json),
                        prepareBitmaps(typeAdapter.getImages()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public RestNotificationService getRestNotificationService() {
        return restNotificationService;
    }
}
