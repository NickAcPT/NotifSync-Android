package me.nickac.notisyncreborn.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.Person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import me.nickac.notisyncreborn.SyncApplication;

public class MiscUtils {
    private static final HashSet<Class<?>> WRAPPER_TYPES = new HashSet<>(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Void.class));

    public static Map<String, Object> bundleToMap(Bundle bundle) {
        HashMap<String, Object> temp = new HashMap<>();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value != null && (value.getClass().isPrimitive() || isWrapperType(
                    value.getClass()) || value instanceof String)) {
                temp.put(key, value);
            } else if (value instanceof CharSequence) {
                temp.put(key, value.toString());
            }
        }
        return temp;
    }

    private static boolean isWrapperType(Class clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    public static String getDeviceName() {
        return android.os.Build.BRAND + android.os.Build.MODEL;
    }


    /**
     * Converts this compat {@link Person} to the base Android framework {@link android.app.Person}.
     */
    @RequiresApi(28)
    public static android.app.Person toAndroidPerson(Person person) {
        if (person == null)
            return null;
        return new android.app.Person.Builder()
                .setName(person.getName())
                .setIcon((person.getIcon() != null) ? person.getIcon().toIcon() : null)
                .setUri(person.getUri())
                .setKey(person.getKey())
                .setBot(person.isBot())
                .setImportant(person.isImportant())
                .build();
    }

    public static String getNameForPackage(String packageName) {
        final PackageManager pm = SyncApplication.getContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }
}
