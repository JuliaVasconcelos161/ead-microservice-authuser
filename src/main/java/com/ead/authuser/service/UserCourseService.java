package com.ead.authuser.service;

import com.ead.authuser.model.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);
}
