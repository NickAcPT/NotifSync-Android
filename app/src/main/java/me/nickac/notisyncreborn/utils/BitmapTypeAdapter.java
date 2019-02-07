package me.nickac.notisyncreborn.utils;

import android.graphics.Bitmap;

import com.gilecode.yagson.ReadContext;
import com.gilecode.yagson.WriteContext;
import com.gilecode.yagson.com.google.gson.TypeAdapter;
import com.gilecode.yagson.com.google.gson.stream.JsonReader;
import com.gilecode.yagson.com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BitmapTypeAdapter extends TypeAdapter<Bitmap> {
    @Override

    public void write(JsonWriter out, Bitmap value, WriteContext writeContext) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        String str = getIdForImage(value).toString();
        out.value(str);
    }

    @Override
    public Bitmap read(JsonReader jsonReader, ReadContext readContext) throws IOException {
        return null;
    }

    private Map<Bitmap, UUID> images = new HashMap<>();

    private UUID getIdForImage(Bitmap bmp) {
        return images.computeIfAbsent(bmp, bitmap -> UUID.randomUUID());
    }

    public Map<UUID, Bitmap> getImages() {
        return images.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
