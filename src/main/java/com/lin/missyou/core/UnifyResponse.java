package com.lin.missyou.core;

import com.lin.missyou.exception.CreateSuccess;

public class UnifyResponse {
    private Integer code;
    private String message;
    private String request;

    public UnifyResponse(Integer code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }


    public static void createSuccess(Integer code){
        throw new CreateSuccess(code);
    }
}
