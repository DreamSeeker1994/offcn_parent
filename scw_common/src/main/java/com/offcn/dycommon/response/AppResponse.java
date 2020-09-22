package com.offcn.dycommon.response;

import com.offcn.dycommon.enums.ResponseCodeEnum;

public class AppResponse<T> {

    private Integer code;

    private String msg;

    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> AppResponse<T> ok(T data){
        AppResponse<T> appResponse = new AppResponse<>();
        appResponse.setCode(ResponseCodeEnum.SUCCESS.getCode());
        appResponse.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
        appResponse.setData(data);
        return appResponse;
    }

    public static <T> AppResponse<T> fail(T data){
        AppResponse<T> appResponse = new AppResponse<T>();
        appResponse.setCode(ResponseCodeEnum.FAIL.getCode());
        appResponse.setMsg(ResponseCodeEnum.FAIL.getMsg());
        appResponse.setData(data);

        return appResponse;
    }
}


