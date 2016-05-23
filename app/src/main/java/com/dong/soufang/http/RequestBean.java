package com.dong.soufang.http;

import java.util.HashMap;

/**
 * Description:
 * <p/>
 * Author: dong
 * Date: 16/3/14
 */
public class RequestBean {
    private String tag;
    private HashMap<String, Object> params;
    private String url;

    public RequestBean(String tag, String url, HashMap<String, Object> params) {
        setParams(params);
        setUrl(url);
        setTag(tag);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public HashMap<?, ?> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
