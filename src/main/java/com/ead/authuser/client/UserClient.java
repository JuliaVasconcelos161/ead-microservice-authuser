package com.ead.authuser.client;

import com.ead.authuser.model.dto.CourseDto;
import com.ead.authuser.model.dto.ResponsePageDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Log4j2
@Component
public class UserClient {

    private final RestTemplate restTemplate;

    StringBuilder REQUEST_URI = new StringBuilder("http://localhost:8082");

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDto> searchResult = null;
        ResponseEntity<ResponsePageDto<CourseDto>> result = null;
        StringBuilder url = REQUEST_URI;
        url.append("/courses?userId=");
        url.append(userId);
        url.append("&page=");
        url.append(pageable.getPageNumber());
        url.append("&size=");
        url.append(pageable.getPageSize());
        url.append("&sort=");
        url.append(pageable.getSort().toString().replaceAll(": ", ","));
        String urlString = url.toString();
        log.debug("Request Url: {} ", urlString);
        log.info("Request Url: {} ", urlString);
        try{
            ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                    new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
            result = restTemplate.exchange(urlString, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {} ", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return new PageImpl<>(searchResult);
    }
}
