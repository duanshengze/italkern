package com.victoryze.web.italker.push.bean.api.base;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by dsz on 2018/2/20.
 */
public class ResponseModel<M> implements Serializable {

    public static final int ERROR_ACCOUNT_TOKEN = 2001;

    private static final int ERROR_ACCOUNT_LOGIN = 2002;

    public static final int ERROR_ACCOUNT_REGISTER = 2003;


    public static final int ERROR_CREATE_MESSAGE = 3003;

    public static final int ERROR_PARAMETERS = 4001;

    public static final int ERROR_PARAMETERS_EXIST_ACCOUNT = 4002;

    public static final int ERROR_PARAMETERS_EXIST_NAME = 4003;


    public static final int ERROR_NOT_FOUND_USER = 4041;

    private static final int ERROR_SERVICE = 5001;


    @Expose
    private int code;
    @Expose
    private String message;
    @Expose
    private LocalDateTime time = LocalDateTime.now();
    @Expose
    private M result;

    public ResponseModel() {
        code = 1;
        message = "ok";
    }

    public ResponseModel(M result) {
        this();
        this.result = result;
    }

    public ResponseModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseModel(int code, String message, M result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public M getResult() {
        return result;
    }

    public void setResult(M result) {
        this.result = result;
    }

    public static <M> ResponseModel<M> buildAccountError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_TOKEN,
                "Account Error;you need login.");
    }

    public static <M> ResponseModel<M> buildParameterError() {
        return new ResponseModel<M>(ERROR_PARAMETERS, "Parameters Error.");
    }

    public static <M> ResponseModel<M> buildHaveAccountError() {
        return new ResponseModel<>(ERROR_PARAMETERS_EXIST_ACCOUNT, "Already have this account.");
    }

    public static <M> ResponseModel<M> buildHaveNameError() {

        return new ResponseModel<M>(ERROR_PARAMETERS_EXIST_NAME, "Already have this name.");
    }

    public static <M> ResponseModel<M> buildRegisterError() {

        return new ResponseModel<>(ERROR_ACCOUNT_REGISTER, "Have this account");
    }

    public static <M> ResponseModel<M> buildOK(M result) {
        return new ResponseModel<M>(result);
    }

    public static <M> ResponseModel<M> buildNotFoundUserError(String str) {


        return new ResponseModel<>(ERROR_NOT_FOUND_USER, str != null ? str : "Not Found User.");

    }

    public static <M> ResponseModel<M> buildServiceError() {

        return new ResponseModel<>(ERROR_SERVICE, "Service Error.");
    }

    public static <M> ResponseModel<M> buildLoginError() {
        return new ResponseModel<M>(ERROR_ACCOUNT_LOGIN, "Account or passwprd error.");
    }

    public static <M> ResponseModel<M> buildCreateError(int errorCreateMessage) {
        return new ResponseModel<M>(errorCreateMessage, "Create failed");
    }
}
