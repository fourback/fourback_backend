package com.fourback.bemajor.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class BeanConfig {

    @Bean
    public Map<Long, Set<WebSocketSession>> studyGrupIdSessionsMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<WebSocketSession, Pair<Long, Long>> sessionIdsMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}