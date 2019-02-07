package me.nickac.notisyncreborn.networking;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestNotificationService {

    @Multipart
    @POST("handlenotification")
    Call<ResponseBody> sendNotificationRaw(
            @Part("json") RequestBody json,
            @Part List<MultipartBody.Part> files);

    @POST("removenotification/{id}/{appPackage}")
    Call<ResponseBody> removeNotificationRaw(@Path("id") int id, @Path("appPackage") String appPackage);


}
