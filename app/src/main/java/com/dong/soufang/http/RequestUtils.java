package com.dong.soufang.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class RequestUtils {
    protected static final String TAG = RequestUtils.class.getSimpleName();
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public static void initRequest(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void addToRequestQueue(Request<?> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        getRequestQueue().add(request);

    }

    public void addToRequestQueue(Request<?> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
