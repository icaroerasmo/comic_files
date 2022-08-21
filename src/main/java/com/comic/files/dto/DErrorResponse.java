package com.comic.files.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DErrorResponse<T> {

    private String message;
    private T content;

    public DErrorResponse(String message) {
        this.message = message;
    }

}
