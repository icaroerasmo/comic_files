package com.comic.files.resource;

import com.comic.files.UserService;
import com.comic.files.dto.DUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final UserService service;

    @GetMapping
    public List<DUser> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public DUser findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public DUser insert(@RequestBody DUser dUser) {
        return service.insert(dUser);
    }

    @PutMapping
    public DUser update(@RequestBody DUser dUser) {
        return service.update(dUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}