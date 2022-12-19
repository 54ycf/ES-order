package com.ecnu.esorder.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
 
public class Result<T> implements Serializable {
 
 
    private static final long serialVersionUID = 1L;
    /**
     * 编码：0表示成功，其他值表示失败
     */
    @JsonProperty("Code")
    public int Code = 0;
    /**
     * 消息内容
     */
    @JsonProperty("Message")
    public String Message = "操作成功";
 
    /**
     * 是否成功
     */
    @JsonProperty("Success")
    public boolean Success = true;
    /**
     * 响应数据
     */
    public T Response;
 
    public Result<T> ok(T Response) {
        this.Response = Response;
        return this;
    }
    public Result<T> ok() {
        this.Response = null;
        return this;
    }
    public Result<T> ok(T Response,String message) {
        this.Response = Response;
        this.Message = message;
        return this;
    }
 
    public boolean success() {
        return Code == 0 ? true : false;
    }
 
    public Result<T> error() {
        this.Success = false;
        this.Code = 500;
        this.Message = "系统错误";
        return this;
    }
 
    public Result<T> error(int code) {
        this.Success = false;
        this.Code = code;
        return this;
    }
 
    public Result<T> error(int code, String Message) {
        this.Success = false;
        this.Code = code;
        this.Message = Message;
        return this;
    }
 
    public Result<T> error(String Message) {
        this.Success = false;
        this.Code = 500;
        this.Message = Message;
        return this;
    }
 
    public static Result OK() {
        return new Result(0,"操作成功",null);
    }
    public static Result OK(Object rtnData) {
        return new Result(0,"操作成功",rtnData);
    }
    public static Result TIP(Object rtnData,String message) {
        return new Result(2,message,rtnData);
    }
    public static Result OK(Object rtnData,String message) {
        return new Result(0,message,rtnData);
    }
 
 
    public static Result rtn(Integer code, String Message, Object rtnData) {
        return new Result(1,Message,rtnData);
    }
 
 
    public static Result ERROR(String Message) {
        return new Result(1,Message,null);
    }
    public static Result ERROR(Integer code,String Message) {
        return new Result(code,Message,null);
    }
 
 
    public Result(int code, String message, T response) {
        Code = code;
        Message = message;
        Response = response;
        Success = code == 0?true:false;
    }
 
    public Result() {
    }
}