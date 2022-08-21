package com.comic.files.builder;

import com.comic.files.dto.DUser;
import com.comic.files.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserBuilder implements IBuilder<DUser>{

    private final UserService service;

    @Override
    public DUser newEntity() {
        DUser dUser = new DUser();
        dUser.setBirthDate(LocalDate.now());
        dUser.setEmail("user@email.com");
        dUser.setName("Jhon Doe");
        dUser.setUsername("jhondoe");
        dUser.setPassword("password");
        return dUser;
    }

    @Override
    public DUser newAndSaveEntity(){
        DUser dUser = new DUser();
        dUser.setBirthDate(LocalDate.now());
        dUser.setEmail("user@email.com");
        dUser.setName("Jhon Doe");
        dUser.setUsername("jhondoe");
        dUser.setPassword("password");
        return service.insert(dUser);
    }
}
