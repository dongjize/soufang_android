package com.dong.soufang.http;

import java.util.HashMap;

/**
 * Description:
 * <p>
 * Author: dong
 * Date: 16/3/17
 */
public class HttpErrorMsg {
    private HashMap<String, String> errorMsg = new HashMap<>();

    public HttpErrorMsg() {
        errorMsg.put("validate_code_error", "验证码错误");
        errorMsg.put("user_not_login", "用户未登录");
    }
}
