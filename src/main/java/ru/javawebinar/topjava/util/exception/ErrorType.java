package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("common.appError"),
    DATA_NOT_FOUND("common.dataNotFound"),
    DATA_ERROR("common.dataError"),
    VALIDATION_ERROR("common.validationError");

    private String code;

    ErrorType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
