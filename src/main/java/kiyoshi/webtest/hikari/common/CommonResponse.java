package kiyoshi.webtest.hikari.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
//springboot Jackson序列化成JSON字符串,解决json数据无法解析的问题
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {//因为不同的返回值类型不同，所以我们需要使用泛型
    private Integer code;
    private String message;
    private T data;

    protected CommonResponse(Integer code,String message, T data) {//采用protected完成最基本的封装
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @JsonIgnore//我们不希望客户端看到这个，但是程序员需要
    public boolean isSuccess(){
        return this.code == ResponseCode.SUCCESS.getCode();
    }
    //请求成功无数据返回
    public static <T> CommonResponse<T> createForSuccess(){//用枚举类型来替换本函数的常量，提高可维护性
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDescription(),null);
    }

    //请求成功并返回数据
    public static <T> CommonResponse<T> createForSuccess(T data){
        return new CommonResponse<>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDescription(),data);
    }

    //请求错误，默认错误信息
    public static <T> CommonResponse<T> createForError(int code, String description){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDescription(), null);
    }

    //请求错误，指定错误信息
    public static <T> CommonResponse<T> createForError(String message){
        return new CommonResponse<>(ResponseCode.ERROR.getCode(), message, null);
    }

    //请求错误，指定错误码和错误信息
    public static <T> CommonResponse<T> createForError(Integer code, String message){
        return new CommonResponse<>(code,message,null);
    }

}


