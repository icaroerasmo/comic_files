package com.comic.files.mapper;

import com.comic.files.dto.DUser;
import com.comic.files.model.EUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<DUser, EUser> {

}
