package com.comic.files.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NotFoundObjectException extends RuntimeException{

    @Getter
    private String object;

    @Getter
    private String id;

}
