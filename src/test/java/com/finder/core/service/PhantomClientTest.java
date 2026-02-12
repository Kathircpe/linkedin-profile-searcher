package com.finder.core.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PhantomClientTest {

    private PhantomClient client;

    @BeforeEach
    void setUp() throws Exception {
        client = new PhantomClient();
        // Mock config values and RestTemplate using reflection
        setPrivateField(client, "restTemplate", mockRestTemplate());
        setPrivateField(client, "apiKey", "test-key");
        setPrivateField(client, "BASE_URL", "https://api.phantom");
        setPrivateField(client, "linkedInCookie", "test-cookie");
    }


    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private RestTemplate mockRestTemplate() {
        RestTemplate mock = mock(RestTemplate.class);
        // Mock common responses
        return mock;
    }
}

