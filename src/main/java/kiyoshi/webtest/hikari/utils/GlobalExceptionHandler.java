package kiyoshi.webtest.hikari.utils;

import jakarta.validation.ConstraintViolationException;
import kiyoshi.webtest.hikari.common.CommonResponse;
import kiyoshi.webtest.hikari.common.ResponseCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

//捕获全局异常，因为我们在很多地方都无法捕获某些异常，比如在login，我们知道用户名密码没写是个异常，但是我们却没有办法捕获。经典AOP
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)//使这个切面能捕获到这个名字的异常
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)//手动设置一个状态码
    public CommonResponse<Object> handleMissingParameterException(){
//        System.out.println(ResponseCode.ARGUMENT_ILLEGAL.getCode()+ ResponseCode.ARGUMENT_ILLEGAL.getDescription());
        return CommonResponse.createForError(ResponseCode.ARGUMENT_ILLEGAL.getCode(),ResponseCode.ARGUMENT_ILLEGAL.getDescription());
    }

    //非对象参数校验
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse<Object> handleValidationException(ConstraintViolationException exception){
        return CommonResponse.createForError(exception.getMessage());
    }

    //对象参数校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public CommonResponse<Object> handleValidException(MethodArgumentNotValidException exception){
        return CommonResponse.createForError(
                ResponseCode.ARGUMENT_ILLEGAL.getCode(),formatValidErrorsMessage(exception.getAllErrors()));
    }

    //处理MethodArgumentNotValidException异常的message不好用的问题，格式化之后返回
    private String formatValidErrorsMessage(List<ObjectError> errorList){
        StringBuffer errorMessage = new StringBuffer();
        errorList.forEach(error -> errorMessage.append(error.getDefaultMessage()).append(","));
        errorMessage.deleteCharAt(errorMessage.length()-1);
        return errorMessage.toString();
    }
}
