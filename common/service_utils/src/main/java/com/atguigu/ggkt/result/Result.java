package com.atguigu.ggkt.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;

    private String message;

    private T data;


    public Result() {
    }
    public static <T> Result<T> build(T body, Integer code, String message){
        Result<T> result = new Result<T>();
        if (body != null) {
            result.setData(body);
        }
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> ok(T data) {
        return build(data,20000,"成功");
    }

    public static <T> Result<T> fail(T data) {
        return build(data,20001,"失败");
    }

    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }


}
