package com.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDados implements IConverteDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> anyClass) {
        try {
            return mapper.readValue(json, anyClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error, can't process JSON.", e);
        }
    }
}
