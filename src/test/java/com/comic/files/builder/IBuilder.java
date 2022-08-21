package com.comic.files.builder;

public interface IBuilder<T> {

    T newEntity();

    T newAndSaveEntity();

}
