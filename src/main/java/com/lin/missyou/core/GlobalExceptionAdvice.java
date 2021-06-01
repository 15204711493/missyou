package com.lin.missyou.core;

import com.lin.missyou.core.configuration.ExceptionCodeConfiguration;
import com.lin.missyou.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionAdvice {


    @Autowired
    private ExceptionCodeConfiguration exceptionCodeConfiguration;


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        String url = req.getRequestURI();
        String method = req.getMethod();
        UnifyResponse u = new UnifyResponse(9999, "服务器异常", method + ":" + url);
        return u;

    }

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<UnifyResponse> handleKhnowException(HttpServletRequest req, HttpException e) {


        String url = req.getRequestURI();
        String method = req.getMethod();
        UnifyResponse u = new UnifyResponse(e.getCode(), exceptionCodeConfiguration.getMessage(e.getCode()), method + ":" + url);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus status = HttpStatus.resolve(e.getHttpStatusCode());
        ResponseEntity<UnifyResponse> message = new ResponseEntity<UnifyResponse>(u, httpHeaders, status);
        return message;

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handBeanValidation(HttpServletRequest req, MethodArgumentNotValidException e) {
        String url = req.getRequestURI();
        String method = req.getMethod();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = this.formatAllErrorMessagees(allErrors);

        return new UnifyResponse(10001, message, method + ":" + url);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public UnifyResponse constraintViolationException(HttpServletRequest req,ConstraintViolationException e){
        String url = req.getRequestURI();
        String method = req.getMethod();

        StringBuffer errorMsg = new StringBuffer();
        for(ConstraintViolation c:e.getConstraintViolations()){
          errorMsg.append(c.getMessage()).append(";") ;
        }

        return  new UnifyResponse(10001, errorMsg.toString(), method + ":" + url);
    };


    public String formatAllErrorMessagees(List<ObjectError> allErrors) {
        StringBuffer errorMsg = new StringBuffer();
        allErrors.forEach(error -> {
            errorMsg.append(error.getDefaultMessage()).append(";");
        });
        return errorMsg.toString();
    }

}
