package com.ead.authuser.controller;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.model.dto.CourseDto;
import com.ead.authuser.model.dto.UserCourseDto;
import com.ead.authuser.service.UserCourseService;
import com.ead.authuser.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;

    private final UserCourseService userCourseService;

    public UserCourseController(CourseClient courseClient, UserService userService, UserCourseService userCourseService) {
        this.courseClient = courseClient;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId) {
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                               @RequestBody @Valid UserCourseDto userCourseDto) {
        Optional<UserModel> userModelOptional = userService.findByid(userId);
        if(userModelOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");

        if(userCourseService.existsByUserAndCourseId(userModelOptional.get(), userCourseDto.getCourseId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");

        UserCourseModel userCourseModel = userCourseService.save(userModelOptional.get()
                .convertToUserCourseModel(userCourseDto.getCourseId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }
}
