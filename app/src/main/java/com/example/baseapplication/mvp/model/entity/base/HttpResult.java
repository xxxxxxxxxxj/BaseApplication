package com.example.baseapplication.mvp.model.entity.base;

/**
 * author:  ljy
 * date:    2017/9/27
 * description:
 */

public class HttpResult<T> {
    private int code;
    private String msg;
    private T data;//返回的数据内容，类型不确定，使用泛型T表示

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
}
