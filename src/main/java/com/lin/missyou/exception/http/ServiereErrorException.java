package com.lin.missyou.exception.http;

public class ServiereErrorException extends HttpException {
    public ServiereErrorException(int code) {
        this.httpStatusCode = 500;
        this.code = code;
    }
}
