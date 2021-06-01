package com.lin.missyou.exception.http;

public class NotFoundEcxeption extends HttpException {

    public NotFoundEcxeption(int code) {
        this.httpStatusCode = 404;
        this.code = code;
    }
}
