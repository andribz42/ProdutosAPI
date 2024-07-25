package br.com.apr.infrastucture.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        StreamWriteConstraints constraints = StreamWriteConstraints.builder()
            .maxNestingDepth(2000)
            .build();
        
        mapper.getFactory().setStreamWriteConstraints(constraints);
        
        return mapper;
    }
}
