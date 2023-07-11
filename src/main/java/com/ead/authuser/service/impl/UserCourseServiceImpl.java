package com.ead.authuser.service.impl;

import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import org.springframework.stereotype.Service;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository repository;

    public UserCourseServiceImpl(UserCourseRepository repository) {
        this.repository = repository;
    }


}
