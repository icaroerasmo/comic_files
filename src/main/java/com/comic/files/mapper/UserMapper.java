package com.comic.files.mapper;

import com.comic.files.dto.DUser;
import com.comic.files.model.EUser;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {

    DUser toDto(EUser eUser);

    List<DUser> toDto(List<EUser> eUser);

    EUser toEntity(DUser dUser);

    List<EUser> toEntity(List<DUser> dUser);

}
