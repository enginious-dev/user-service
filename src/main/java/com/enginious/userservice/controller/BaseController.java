package com.enginious.userservice.controller;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    protected HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes) requestAttributes).getRequest();
        }
        throw new RuntimeException("Not called in the context of an HTTP request");
    }

    protected String getCurrentHttpRequestUrl() {
        return getCurrentHttpRequest().getRequestURI();
    }

    protected String getCurrentHttpRequestMethod() {
        return getCurrentHttpRequest().getMethod();
    }

    protected String error() {
        HttpServletRequest request = getCurrentHttpRequest();
        return String.format("error while executing request [%s] {path: %s}:", request.getMethod(), request.getRequestURI());
    }

    protected String executing() {
        HttpServletRequest request = getCurrentHttpRequest();
        return String.format("executing request [%s] {path: %s}:", request.getMethod(), request.getRequestURI());
    }
}
