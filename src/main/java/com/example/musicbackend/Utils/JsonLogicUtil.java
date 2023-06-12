package com.example.musicbackend.Utils;

import com.example.musicbackend.exception.custom.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonLogicUtil {

    public static String convertObjectToJson(Object myObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(myObject);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("object truyền vào không pass được sang json"+ myObject.getClass());
        }
        return json;
    }

    public static Object convertJsonToObject(String json, Class myClass)  {
        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = null;
        try {
            obj = objectMapper.readValue(json, myClass);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("json truyên lên đang bị sai format: "+ json);
        }
        return obj;
    }
}
