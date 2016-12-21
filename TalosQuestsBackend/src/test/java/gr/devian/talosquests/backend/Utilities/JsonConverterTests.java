package gr.devian.talosquests.backend.Utilities;


import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Nikolas on 21/12/2016.
 */
public class JsonConverterTests {
    JsonConverter<testPrivate> jCUf;
    @Before
    public void setUp() {
         jCUf = new JsonConverter<testPrivate>(testPrivate.class) {
            @Override
            public String convertToDatabaseColumn(testPrivate meta) {
                return super.convertToDatabaseColumn(meta);
            }

            @Override
            public testPrivate convertToEntityAttribute(String dbData) {
                return super.convertToEntityAttribute(dbData);
            }
        };
    }

    @Test
    public void testJsonConverterOnNullModelToJsonValue() {
        assertEquals(jCUf.convertToDatabaseColumn(null), String.valueOf("null"));
    }

    @Test
    public void testJsonConverterOnModelWithNoPublicMethodsOrFieldsToJsonValue() {
        assertEquals(jCUf.convertToDatabaseColumn(new testPrivate()), null);
    }

    @Test
    public void testJsonConverterOnNullJsonToModelValue() {
        assertNull(jCUf.convertToEntityAttribute(null));
    }
    @Test
    public void testJsonConverterOnInvalidJsonToModelValue() {
        assertNull(jCUf.convertToEntityAttribute("{\"testPrivate\":\"testPrivate\"}"));
    }

    private class testPrivate {
        private String test;
    }

}
