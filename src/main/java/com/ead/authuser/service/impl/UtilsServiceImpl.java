package com.ead.authuser.service.impl;

import com.ead.authuser.service.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Override
    public String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable) {
        StringBuilder url =  new StringBuilder();
        url.append("/courses?userId=");
        url.append(userId);
        url.append("&page=");
        url.append(pageable.getPageNumber());
        url.append("&size=");
        url.append(pageable.getPageSize());
        url.append("&sort=");
        url.append(pageable.getSort().toString().replaceAll(": ", ","));
        return url.toString();
    }
}
