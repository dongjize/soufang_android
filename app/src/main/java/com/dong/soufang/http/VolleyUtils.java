package com.dong.soufang.http;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.dong.soufang.GlobalData;
import com.dong.soufang.util.L;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Description: Volley Request
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public class VolleyUtils {
    private static RequestUtils requestUtils;

    public VolleyUtils() {
        requestUtils = new RequestUtils();
    }

    /**
     * GET 基础请求object
     *
     * @param url
     * @param tag
     * @param params
     * @param listener
     * @param errorListener
     */
    public void httpGetRequest(String url, String tag, HashMap<?, ?> params,
                               Listener<JSONObject> listener, ErrorListener errorListener) {
        url = url + getParams(params);
        JsonObjectRequest request = new JsonObjectRequest(url, null, listener, errorListener);
        L.i("HttpHandler", url);
        request.setRetryPolicy(new DefaultRetryPolicy(HttpApi.TIME_OUT, 1, 1.0f));
        requestUtils.addToRequestQueue(request, tag);
    }

    /**
     * POST 基础请求object
     *
     * @param requestBean
     * @param listener
     * @param errorListener
     */
    public void httpPostRequest(RequestBean requestBean, Listener<JSONObject> listener, ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, requestBean.getUrl(), requestBean.getParams() == null ? null : new JSONObject(requestBean.getParams()), listener, errorListener);
        L.i("HttpHandler", requestBean.getUrl());
        L.i("HttpHandler", requestBean.getParams() == null ? "" : requestBean.getParams().toString());
        request.setRetryPolicy(new DefaultRetryPolicy(HttpApi.TIME_OUT, 1, 1.0f));
        requestUtils.addToRequestQueue(request, requestBean.getTag());
    }

    /**
     * GET 基础请求string
     *
     * @param requestBean
     * @param listener
     * @param errorListener
     */
    public void httpGetString(RequestBean requestBean, Listener<String> listener, ErrorListener errorListener) {
        String url = requestBean.getUrl() + getParams(requestBean.getParams());
        StringRequest request = new StringRequest(url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Cookie", GlobalData.rawCookies);
                String LC = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
                String userAgent = GlobalData.OBJECTNO + " " + GlobalData.VERSION + " (" +
                        android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "; Android OS " +
                        android.os.Build.VERSION.RELEASE + "; " + LC + "; Scale/" + GlobalData.DENSITY + ")";
                hashMap.put("User-agent", userAgent);
                return hashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    if (TextUtils.isEmpty(GlobalData.rawCookies)) {
                        GlobalData.rawCookies = responseHeaders.get("Set-Cookie");
                    }
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        L.i("HttpHandler", url);
        request.setRetryPolicy(new DefaultRetryPolicy(HttpApi.TIME_OUT, 1, 1.0f));
        requestUtils.addToRequestQueue(request, requestBean.getTag());
    }

    /**
     * POST 基础请求string
     *
     * @param requestBean
     * @param listener
     * @param errorListener
     */
    public void httpPostString(final RequestBean requestBean, Listener<String> listener, ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.POST, requestBean.getUrl(), listener, errorListener) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return (Map<String, String>) requestBean.getParams();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap localHashMap = new HashMap();
                localHashMap.put("Cookie", GlobalData.rawCookies);
                String LC = Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
                String userAgent = GlobalData.OBJECTNO + " " + GlobalData.VERSION + " (" +
                        android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + "; Android OS " +
                        android.os.Build.VERSION.RELEASE + "; " + LC + "; Scale/" + GlobalData.DENSITY + ")";
                localHashMap.put("User-agent", userAgent);
                return localHashMap;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Map<String, String> responseHeaders = response.headers;
                    if (TextUtils.isEmpty(GlobalData.rawCookies)) {
                        GlobalData.rawCookies = responseHeaders.get("Set-Cookie");
                    }
                    String dataString = new String(response.data, "UTF-8");
                    return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        L.i("HttpHandler", requestBean.getUrl());
        L.i("HttpHandler", requestBean.getParams() == null ? "" : requestBean.getParams().toString());
        request.setRetryPolicy(new DefaultRetryPolicy(HttpApi.TIME_OUT, 1, 1.0f));
        requestUtils.addToRequestQueue(request, requestBean.getTag());
    }

    /**
     * 拼接GET请求的参数
     *
     * @param params
     * @return
     */
    private String getParams(HashMap<?, ?> params) {
        if (params == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        Iterator<?> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iterator.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key + "=" + value + "&");
        }
        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }
}
