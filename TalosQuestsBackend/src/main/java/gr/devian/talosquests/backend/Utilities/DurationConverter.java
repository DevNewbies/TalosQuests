package gr.devian.talosquests.backend.Utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import gr.devian.talosquests.backend.LocationProvider.Duration;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

/**
 * Created by Nikolas on 17/12/2016.
 */
@Converter
public class DurationConverter extends JsonConverter<Duration> implements AttributeConverter<Duration,String> {
    public DurationConverter() {
        super(Duration.class);
    }

}
