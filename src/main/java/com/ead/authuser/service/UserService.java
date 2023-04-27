package com.ead.authuser.service;

import com.ead.authuser.model.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findByid(UUID userId);

    void delete(UserModel userModel);
}
