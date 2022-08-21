package com.comic.files.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InvalidInsertException extends RuntimeException {

    @Getter
    private Long id;

}
