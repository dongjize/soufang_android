package com.dong.soufang.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/15
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 检车SD卡是否挂载
     *
     * @return
     */
    public static boolean isSDCardMounted() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            L.d(TAG, "SDCard is not MOUNTED!");
            return false;
        }
    }

    /**
     * 获取可用的SD卡路径
     *
     * @return
     */
    public static String gainSDCardPath() {
        if (isSDCardMounted()) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (!sdcardDir.canWrite()) {
                L.d(TAG, "SDCard cannot write!");
            }
            return sdcardDir.getPath();
        }
        return "";
    }

    /**
     * 删除目录下的文件
     *
     * @param root 目录
     */
    public static void deleteAllFiles(File root) {
        File[] files = root.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {

                    }
                } else {
                    if (f.exists()) {
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {

                        }
                    }
                }
            }
        }
    }

    /**
     * @param bitmap
     * @param filePath
     */
    public static void saveAsJpeg(Bitmap bitmap, String filePath) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param bitmap
     * @param path
     * @param fileName
     */
    public static void saveAsPng(Bitmap bitmap, String path, String fileName) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            File temp = new File(path + fileName);
            fos = new FileOutputStream(temp);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取文件名
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf("/") + 1);
        } else {
            return "";
        }
    }

    /**
     * 复制文件
     *
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) {

        try {
            int byteSum = 0;
            int byteRead = 0;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {
                InputStream is = new FileInputStream(oldFile);
                FileOutputStream fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                while ((byteRead = is.read(buffer)) != -1) {
                    byteSum += byteRead;
                    System.out.println(byteSum);
                    fos.write(buffer, 0, byteRead);
                }
                is.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    public static Uri addImageAsApplication(ContentResolver resolver, String name, long dateToken,
                                            String directory, String fileName, Bitmap source, byte[] jpegData) {
        //TODO
        return null;
    }
}
