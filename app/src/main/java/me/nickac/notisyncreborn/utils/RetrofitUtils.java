package me.nickac.notisyncreborn.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetrofitUtils {


    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    public static MultipartBody.Part prepareBitmap(String partName, byte[] contents) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/png"),
                        contents

                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, partName, requestFile);
    }

    private static byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @NonNull
    public static List<MultipartBody.Part> prepareBitmaps(Map<UUID, Bitmap> map) {
        List<MultipartBody.Part> list = new ArrayList<>();

        map.forEach((uuid, bitmap) -> list.add(prepareBitmap(uuid.toString(), bitmapToByteArray(bitmap))));

        return list;
    }

}
