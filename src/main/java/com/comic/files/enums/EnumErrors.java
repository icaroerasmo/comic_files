package com.comic.files.enums;

import lombok.Getter;

public enum EnumErrors {

    ERROR_INSERT("error.insert"),
    ERROR_UPDATE("error.update"),
    ERROR_NOT_FOUND("error.id.notfound");

    @Getter
    private String message;

    EnumErrors(String value){
        this.message = value;
    }

}
