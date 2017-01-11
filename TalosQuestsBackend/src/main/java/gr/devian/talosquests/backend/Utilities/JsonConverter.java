package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import javax.persistence.AttributeConverter;
import java.io.IOException;

/**
 * Created by Nikolas on 17/12/2016.
 */

public abstract class JsonConverter<T> implements AttributeConverter<T,String> {
    protected final static ObjectMapper objectMapper = new ObjectMapper();

    protected Class<T> clazz;

    public JsonConverter(Class<T> clazz) {

        this.clazz = clazz;
    }


    @Override
    public String convertToDatabaseColumn(T meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        try {
            if (Strings.isNullOrEmpty(dbData))
                return null;
            return objectMapper.readValue(dbData, clazz);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
