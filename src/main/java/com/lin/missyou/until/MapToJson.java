package com.lin.missyou.until;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.missyou.exception.http.ServiereErrorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

@Converter
public class MapToJson implements AttributeConverter<Map<String,Object>,String> {
    @Autowired
    private ObjectMapper mapper;
    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        try {
            return mapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw  new ServiereErrorException(9999);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertToEntityAttribute(String s) {
       if(s  == null){
           return null;
       }
        try {
            return mapper.readValue(s, HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new ServiereErrorException(9999);
        }
    }
}
