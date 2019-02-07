package me.nickac.notisyncreborn.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class IconUtils {

    public static byte[] getByteArrayFromBitmap(Bitmap bmp) {
        if (bmp == null || bmp.isRecycled()) {
            return new byte[0];
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //bmp.recycle();
        try {
            stream.close();
        } catch (IOException ignored) {
        }
        return byteArray;
    }

    public static Bitmap getBitmapFromIcon(Context ctx, Icon ico) {
        return getBitmapFromIcon(ctx, ico, "");
    }

    public static int getType(Icon ico) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ico.getType() == Icon.TYPE_BITMAP || ico.getType() == Icon.TYPE_ADAPTIVE_BITMAP) {
                return ico.getType();
            }
        } else {
            try {
                return (int) FieldUtils.readDeclaredField(ico, "mType", true);
            } catch (IllegalAccessException e) {
                return -1;
            }
        }
        return -1;
    }

    private static int getResId(@NonNull Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            return icon.getResId();
        } else {
            try {
                return (Integer) icon.getClass().getMethod("getResId").invoke(icon);
            } catch (IllegalAccessException var2) {
                return 0;
            } catch (InvocationTargetException var3) {
                return 0;
            } catch (NoSuchMethodException var4) {
                return 0;
            }
        }
    }

    public static Bitmap getBitmapFromIcon(Context ctx, Icon ico, String fallBackResPackage) {
        if (ico == null)
            return null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (ico.getType() == Icon.TYPE_BITMAP || ico
                        .getType() == Icon.TYPE_ADAPTIVE_BITMAP) {
                    try {
                        return (Bitmap) MethodUtils.invokeExactMethod(ico, "getBitmap");
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //We're not on Android P. Let's use reflection.
                try {
                    int mType = (int) FieldUtils.readDeclaredField(ico, "mType", true);
                    if (mType == Icon.TYPE_BITMAP || mType == Icon.TYPE_ADAPTIVE_BITMAP) {
                        //We have a bitmap. Try to extract it with reflection

                        Bitmap mObj1 = (Bitmap) FieldUtils.readDeclaredField(ico, "mObj1", true);

                        if (mObj1 != null)
                            return mObj1;
                    }
                } catch (IllegalAccessException e) {
                    //Ignore and move on
                }
            }

            String resPckage = getResPackage(ico);
            if (TextUtils.isEmpty(resPckage)) {
                resPckage = fallBackResPackage;
            }
            if (TextUtils.isEmpty(resPckage)) {
                return null;
            }

            Resources res;
            res = ctx.getPackageManager().getResourcesForApplication(resPckage);
            int resId = getResId(ico);

            try {
                @SuppressLint("ResourceType") Drawable drawable = res.getDrawable(resId, null);
                return drawableToBitmap(drawable);
            } catch (Resources.NotFoundException ignored) {
            }

            return BitmapFactory.decodeResource(res, resId);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {

            Bitmap bitmap = Bitmap
                    .createBitmap(Math.max(drawable.getIntrinsicWidth(), 10), Math.max(drawable.getIntrinsicHeight(), 10),
                            Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }

    @Nullable
    @RequiresApi(23)
    private static String getResPackage(@NonNull Icon icon) {
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                return icon.getResPackage();
            } catch (Exception ignored) {
                return null;
            }
        } else {
            try {
                return (String) icon.getClass().getMethod("getResPackage").invoke(icon);
            } catch (IllegalAccessException var2) {
                return null;
            } catch (InvocationTargetException var3) {
                return null;
            } catch (NoSuchMethodException var4) {
                return null;
            }
        }
    }
}
