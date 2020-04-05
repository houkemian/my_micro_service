package org.hkm.common;

import lombok.Data;

@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;

    private static final int DEFAULT_SUCCESS_CODE = 0;
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    private static final int DEFAULT_FAILURE_CODE = -1;
    private static final String DEFAULT_FAILURE_MESSAGE = "FAILURE";


    public static Result success(){
        return new Result(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static Result success(Object data){
        return new Result(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static Result success(String message, Object data){
        return new Result(DEFAULT_SUCCESS_CODE, message, data);
    }

    public static Result success(int code, String message, Object data){
        return new Result(code, message, data);
    }

    public static Result failure(){
        return new Result(DEFAULT_FAILURE_CODE, DEFAULT_FAILURE_MESSAGE, null);
    }

    public static Result failure(Object data){
        return new Result(DEFAULT_FAILURE_CODE, DEFAULT_FAILURE_MESSAGE, data);
    }

    public static Result failure(String message, Object data){
        return new Result(DEFAULT_FAILURE_CODE, message, data);
    }

    public static Result failure(int code, String message, Object data){
        return new Result(code, message, data);
    }

    public boolean isSuccess(){
        return this.code==DEFAULT_SUCCESS_CODE;
    }

    public Result(){}

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
