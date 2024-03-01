package com.ead.authuser.client;

import com.ead.authuser.model.dto.CourseDto;
import com.ead.authuser.model.dto.ResponsePageDto;
import com.ead.authuser.service.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Log4j2
@Component
public class CourseClient {

    private final RestTemplate restTemplate;

    private final UtilsService utilsService;

    @Value("${ead.api.url.course}")
    private String REQUEST_URL_COURSE;

    public CourseClient(RestTemplate restTemplate, UtilsService utilsService) {
        this.restTemplate = restTemplate;
        this.utilsService = utilsService;
    }

//    @Retry(name = "retryInstance", fallbackMethod="retryfallback")
    @CircuitBreaker(name = "circuitbreakerInstance", fallbackMethod = "circuitbreakerfallback")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {
        List<CourseDto> searchResult = null;
        ResponseEntity<ResponsePageDto<CourseDto>> result = null;
        String urlString = REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUser(userId, pageable);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> requestEntity = new HttpEntity<>("parameters", headers);
        log.debug("Request Url: {} ", urlString);
        log.info("Request Url: {} ", urlString);
        ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType =
                new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
        result = restTemplate.exchange(urlString, HttpMethod.GET, requestEntity, responseType);
        searchResult = result.getBody().getContent();
        log.debug("Response Number of Elements: {} ", searchResult.size());
        log.info("Ending request /courses userId {} ", userId);
        return result.getBody();
    }

    public Page<CourseDto> circuitBreakerFallback(UUID userId, Pageable pageable, String token, Throwable t) {
        log.error("Inside circuit breaker fallback, cause - {}", t.toString());
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }

    //Deve ter o mesmo retorno e mesmos parametros, além de uma excecao
//    public Page<CourseDto> retryFallback(UUID userId, Pageable pageable, Throwable t) {
//        log.error("Inside retry retryfallback, cause - {}", t.toString());
//        List<CourseDto> searchResult = new ArrayList<>();
//        return new PageImpl<>(searchResult);
//    }
}
