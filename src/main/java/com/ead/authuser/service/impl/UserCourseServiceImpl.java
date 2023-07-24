package com.ead.authuser.service.impl;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository repository;

    public UserCourseServiceImpl(UserCourseRepository repository) {
        this.repository = repository;
    }


    @Override
    public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
        return repository.existsByUserAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        return repository.save(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return repository.existsByCourseId(courseId);
    }

    @Transactional
    @Override
    public void deleteUserCourseByCourse(UUID courseId) {
        repository.deleteAllByCourseId(courseId);
    }

}
