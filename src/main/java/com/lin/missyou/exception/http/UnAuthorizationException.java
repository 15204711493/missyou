package com.lin.missyou.exception.http;

public class UnAuthorizationException extends HttpException {

    public UnAuthorizationException(int code) {
        this.httpStatusCode = 401;
        this.code = code;
    }
}
