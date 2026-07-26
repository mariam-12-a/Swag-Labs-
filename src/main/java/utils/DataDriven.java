package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class DataDriven {

    public static JsonNode jsonReader() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File("testData/testData.json");

            return objectMapper.readTree(file);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}