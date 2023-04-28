package com.example.diplomproject.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
@Component
public class MarcetJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    protected MarcetJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }
    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }

}
