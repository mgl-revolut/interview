package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class Json {

    public static <T> T deserialize(String jsonString, Class<T> clazz) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(jsonString, clazz);

        } catch (IOException e) {


            throw new RuntimeException("Error while deserialization", e);
        }
    }


    public static <T> String serialize(T t){

        try {

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(t);

        } catch (IOException e) {

            throw new RuntimeException("Error while deserialization.", e);
        }
    }
}
