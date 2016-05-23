package com.dong.soufang.http;

/**
 * Description: 网络返回接口
 * <p>
 * Author: dong
 * Date: 16/3/14
 */
public interface Callback {

    void onSuccess(BaseResult result);

    void onFailure(String msg);
}
