package com.comic.files.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AcceptedObjects {

    private Class object;
    private List<String> ignoreMethods;

}
