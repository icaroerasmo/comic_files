package com.comic.files.service;

import com.comic.files.dto.DUser;
import com.comic.files.exception.InvalidInsertException;
import com.comic.files.exception.NotFoundObjectException;
import com.comic.files.mapper.UserMapper;
import com.comic.files.model.EUser;
import com.comic.files.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper mapper;

    private final UserRepository repository;

    public List<DUser> list() {
        List<EUser> users = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return mapper.toDto(users);
    }

    public DUser findById(Long id) {
        EUser eUser = repository.findById(id).orElseThrow(() -> new NotFoundObjectException(EUser.ENTITY_NAME,id.toString()));
        return mapper.toDto(eUser);
    }

    public DUser update(DUser dUser) {
        if(Objects.nonNull(dUser.getId())) {
            return save(dUser);
        } else {
            throw new InvalidInsertException(null);
        }
    }

    public DUser insert(DUser dUser) {
        if(Objects.isNull(dUser.getId())) {
            return save(dUser);
        } else {
            throw new InvalidInsertException(dUser.getId());
        }
    }

    private DUser save(DUser dUser) {
        EUser eUser = mapper.toEntity(dUser);
        eUser = repository.save(eUser);
        return mapper.toDto(eUser);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}
