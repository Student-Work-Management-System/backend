package edu.guet.studentworkmanagementsystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

final public class JsonUtil {
    public static ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
}
