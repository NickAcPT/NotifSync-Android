package me.nickac.notisyncreborn.networking;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestNotificationService {

    @Multipart
    @POST("v2/handlenotification")
    Call<ResponseBody> sendNotificationRaw(
            @Part("json") RequestBody json,
            @Part List<MultipartBody.Part> files);


}
