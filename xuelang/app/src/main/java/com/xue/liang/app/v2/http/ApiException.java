package com.xue.liang.app.v2.http;


public class ApiException extends RuntimeException {

    public String code;

    public ApiException(String code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }
}
