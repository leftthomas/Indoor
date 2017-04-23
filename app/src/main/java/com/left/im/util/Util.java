package com.left.im.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.left.im.bean.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author smile
 * @project Util
 * @date 2016-03-01-14:55
 */
public class Util {
    public static boolean checkSdCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public static void cropPhoto(Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, 3);
    }

    /**
     * 保存Bitmap为file
     *
     * @param bmp
     * @return
     */
    public static File saveBitmap2file(Bitmap bmp, User user) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File out = new File(Environment.getExternalStorageDirectory(), "/" + user.getUsername() + "_head.jpg");
        if (!out.exists()) {
            try {
                out.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            stream = new FileOutputStream(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return out;
    }


}
