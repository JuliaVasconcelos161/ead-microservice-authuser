package com.ead.authuser.client;

import com.ead.authuser.model.dto.CourseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

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
        String url =
                REQUEST_URI.append("/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(":", ","))
                        .toString();
        log.debug("Request Url: {} ", url);
        log.info("Request Url: {} ", url);
        try{

            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {} ", e);
        }
        log.info("Ending request /courses userId {} ", userId);
        return ;
    }
}
