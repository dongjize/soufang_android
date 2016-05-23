package com.dong.soufang.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/4/17
 */
public class IoUtils {
    public static String readFromAssets(Context context, String fileName) {
        InputStream inputStream = null;
        try {
            inputStream =context.getResources().getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return readFromInputStream(inputStream);
    }

    public static String readFromInputStream(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            for (String str; (str = reader.readLine()) != null;) {
                sb.append(str);
            }

            reader.close();
            inputStream.close();
            return sb.toString();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int length = -1;
//            while ((length = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, length);
//            }
//            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
